package tbalogh.bvtwitterclient.presentation.presenter;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import tbalogh.bvtwitterclient.data.network.NetworkManager;
import tbalogh.bvtwitterclient.data.network.NetworkStateListener;
import tbalogh.bvtwitterclient.domain.Tweet;
import tbalogh.bvtwitterclient.domain.TweetRepository;
import tbalogh.bvtwitterclient.domain.TweetStreamSubscriber;
import tbalogh.bvtwitterclient.presentation.TweetView;
import tbalogh.bvtwitterclient.presentation.di.PerActivity;

/**
 * Created by tbalogh on 16/08/16.
 */
@PerActivity
public class TweetPresenter implements TweetStreamSubscriber, NetworkStateListener {

    private static final String TAG = "TweetPresenter";

    private final TweetRepository tweetRepository;
    private final NetworkManager  networkManager;
    private       TweetView       tweetView;

    @Inject
    public TweetPresenter(TweetRepository tweetRepository, NetworkManager networkManager) {
        this.tweetRepository = tweetRepository;
        this.networkManager = networkManager;
        this.networkManager.setNetworkListener(this);
        this.networkManager.registerNetworkStateReceiver();
    }

    public void setTweetView(TweetView tweetView) {
        this.tweetView = tweetView;
    }

    public void removeTweetView(TweetView tweetView) {
        if (this.tweetView == tweetView) {
            this.tweetView = null;
        }
    }

    public void subscribeTweets() {
        Log.d(TAG, "subscribeTweets: ");
        this.tweetView.showLoading();
        this.tweetRepository.subscribeTweets(this, this.tweetView.getKeyword(),
                this.networkManager.isConnected());
    }

    @Override
    public void onException(Exception ex) {
        if (this.tweetView != null) {
            this.tweetView.hideLoading();
            this.tweetView.showError(ex.getMessage());
        }
    }

    @Override
    public void onStreamConnected() {
        this.tweetRepository.removeTweets();
        if (this.tweetView != null) {
            this.tweetView.hideLoading();
            this.tweetView.showConnectedToStream();
            this.tweetView.clearTweets();
        }
        Log.d(TAG, "onStreamConnected: ");
    }

    @Override
    public void onStreamDisconnected() {
        if (this.tweetView != null) {
            this.tweetView.showDisconnectedFromStream();
        }
    }

    @Override
    public void onTweetReceived(Tweet tweet) {
        if (this.tweetView != null) {
            this.tweetView.showNewTweet(tweet);
        }
    }

    @Override
    public void onTweetsReceived(List<Tweet> tweets) {
        if (this.tweetView != null) {
            this.tweetView.showTweets(tweets);
        }
    }

    @Override
    public void onConnected() {
        if (this.tweetView != null) {
            this.tweetView.showConnectedToNetwork();
        }
        Log.d(TAG, "onConnected: ");
    }

    @Override
    public void onDisconnected() {
        if (this.tweetView != null) {
            this.tweetView.showDisconnectedFromNetwork();
        }
    }

    public void removeOldTweets() {
        this.tweetRepository.removeOldTweets();
        this.tweetRepository.tweets(this);
    }

    public void onResume() {
        if (!this.networkManager.isConnected()) {
            this.tweetRepository.tweets(this);
        }
    }

    public void onDestroy() {
        this.tweetRepository.unsusbscribeTweets(this);
    }
}
