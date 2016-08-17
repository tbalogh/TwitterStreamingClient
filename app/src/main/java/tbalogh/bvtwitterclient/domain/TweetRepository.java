package tbalogh.bvtwitterclient.domain;

/**
 * Created by tbalogh on 16/08/16.
 */
public interface TweetRepository {
    void subscribeTweets(TweetStreamSubscriber tweetStreamSubscriber, String filter, boolean isNetworkAvailable);

    void unsusbscribeTweets(TweetStreamSubscriber tweetStreamListener);

    void tweets(TweetStreamSubscriber tweetStreamSubscriber);

    void removeOldTweets();

    void setTweetLifeSpan(long lifeSpan);

    void removeTweets();
}
