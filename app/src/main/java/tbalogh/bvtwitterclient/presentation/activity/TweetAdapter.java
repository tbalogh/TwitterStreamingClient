package tbalogh.bvtwitterclient.presentation.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tbalogh.bvtwitterclient.R;
import tbalogh.bvtwitterclient.domain.Tweet;

/**
 * Created by tbalogh on 15/08/16.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Tweet>    tweets;
    private final Context        context;


    public TweetAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.tweets = new ArrayList<>();
    }

    public void add(Tweet tweet) {
        this.tweets.add(0, tweet);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.tweets.clear();
        notifyDataSetChanged();
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View tweetView = this.layoutInflater.inflate(R.layout.item_tweet, parent, false);
        return new TweetViewHolder(tweetView, this.context);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        holder.update(this.tweets.get(position));
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void update(List<Tweet> tweets) {
        this.tweets.clear();
        this.tweets.addAll(tweets);
        notifyDataSetChanged();
    }

    public static class TweetViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_tweet)         TextView  tweetTextView;
        @BindView(R.id.tv_username)      TextView  userNameTextView;
        @BindView(R.id.img_user)         ImageView userPhotoImageView;
        @BindView(R.id.tv_retweet_count) TextView  retweetCountTextView;
        @BindView(R.id.tv_likes_count)   TextView  likesCountTextView;

        private final Context context;

        public TweetViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;
        }

        public void update(Tweet tweet) {
            this.tweetTextView.setText(tweet.getText());
            this.userNameTextView.setText(tweet.getUserName());
            this.retweetCountTextView.setText(String.valueOf(tweet.getRetweetCount()));
            this.likesCountTextView.setText(String.valueOf(tweet.getLikesCount()));
            Picasso.with(context)
                   .load(tweet.getPhotoUrl())
                   .into(this.userPhotoImageView);
        }
    }
}
