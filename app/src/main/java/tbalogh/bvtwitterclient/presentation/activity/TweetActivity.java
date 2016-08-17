package tbalogh.bvtwitterclient.presentation.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import tbalogh.bvtwitterclient.R;
import tbalogh.bvtwitterclient.domain.Tweet;
import tbalogh.bvtwitterclient.presentation.BTTwitterClientApplication;
import tbalogh.bvtwitterclient.presentation.TweetView;
import tbalogh.bvtwitterclient.presentation.di.component.ApplicationComponent;
import tbalogh.bvtwitterclient.presentation.di.component.DaggerTweetComponent;
import tbalogh.bvtwitterclient.presentation.di.component.TweetComponent;
import tbalogh.bvtwitterclient.presentation.presenter.TweetPresenter;

/**
 * Created by
 * tbalogh on 15/08/16.
 */
public class TweetActivity extends AppCompatActivity implements TweetView {

    private static final int OLD_TWEET_CHECK_PERIOD_IN_SEC = 3;
    private static final int OLD_TWEET_CHECK_DELAY_IN_SEC  = 10;

    @BindView(R.id.rootlayout) View         rootLayout;
    @BindView(R.id.rv_tweets)  RecyclerView tweetsRecyclerView;
    @BindView(R.id.toolbar)    Toolbar      toolbar;
    @BindView(R.id.et_search)  EditText     searchEditText;

    @Inject TweetPresenter tweetPresenter;

    public  TweetAdapter             tweetAdapter;
    private ProgressDialog           progressDialog;
    private ScheduledExecutorService scheduler;
    private TweetComponent           tweetComponent;
    private String                   keyword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
        initializeInjector();
        this.tweetComponent.inject(this);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        setupViews();
        scheduleRemovingOldTweets();
    }

    private void initializeInjector() {
        ApplicationComponent applicationComponent =
                ((BTTwitterClientApplication) getApplication()).getApplicationComponent();
        this.tweetComponent = DaggerTweetComponent.builder()
                                                  .applicationComponent(applicationComponent)
                                                  .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.tweetPresenter.setTweetView(this);
        this.tweetPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.tweetPresenter.removeTweetView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.tweetPresenter.onDestroy();
    }

    @Override
    public void showNewTweet(Tweet tweet) {
        runOnUiThread(new TweetRunnable(tweetAdapter, tweet));
    }

    @Override
    public void showError(final String errorMessage) {
        runOnUiThread(() -> Toast.makeText(TweetActivity.this, errorMessage, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public String getKeyword() {
        return this.keyword;
    }

    @Override
    public void showConnectedToStream() {
        runOnUiThread(() -> showMessage(R.string.connected_to_stream));
    }

    @Override
    public void showDisconnectedFromStream() {
        runOnUiThread(() -> showMessage(R.string.disconnected_from_stream));
    }

    @Override
    public void clearTweets() {
        runOnUiThread(() -> this.tweetAdapter.clear());
    }

    @Override
    public void showConnectedToNetwork() {
        runOnUiThread(() -> showMessage(R.string.connected_to_network));
    }

    @Override
    public void showDisconnectedFromNetwork() {
        runOnUiThread(() -> showMessage(R.string.disconnected_from_network));
    }

    @Override
    public void showTweets(List<Tweet> tweets) {
        runOnUiThread(() -> this.tweetAdapter.update(tweets));
    }

    @Override
    public void showLoading() {
        hideLoading();
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this);
            this.progressDialog.setMessage(getString(R.string.loading));
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setCanceledOnTouchOutside(false);
        }
        this.progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    private void setupViews() {
        setSupportActionBar(toolbar);
        setupRecyclerView();
        setupSearchBar();
    }

    private void setupRecyclerView() {
        this.tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.tweetsRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        this.tweetAdapter = new TweetAdapter(this);
        this.tweetsRecyclerView.setAdapter(this.tweetAdapter);
    }

    private void setupSearchBar() {
        this.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (this.searchEditText.getText().toString().isEmpty()) {
                    showEmptySearchBarMessage();
                } else {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    this.keyword = this.searchEditText.getText().toString();
                    this.searchEditText.clearFocus();
                    this.tweetPresenter.subscribeTweets();
                }
                return true;
            }
            return false;
        });
    }

    private void showEmptySearchBarMessage() {
        showMessage(R.string.empty_search_bar_message);
    }

    private void showMessage(int resId) {
        showMessage(getString(resId));
    }

    private void showMessage(String message) {
        Snackbar.make(this.rootLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void scheduleRemovingOldTweets() {
        scheduler.scheduleAtFixedRate(() -> this.tweetPresenter.removeOldTweets(),
                OLD_TWEET_CHECK_DELAY_IN_SEC, OLD_TWEET_CHECK_PERIOD_IN_SEC, TimeUnit.SECONDS);
    }

    public static class TweetRunnable implements Runnable {

        private final TweetAdapter tweetAdapter;
        private final Tweet        tweet;

        public TweetRunnable(TweetAdapter tweetAdapter, Tweet tweet) {
            this.tweetAdapter = tweetAdapter;
            this.tweet = tweet;
        }

        @Override
        public void run() {
            this.tweetAdapter.add(this.tweet);
        }
    }
}
