package tbalogh.bvtwitterclient.presentation;

import java.util.List;

import tbalogh.bvtwitterclient.domain.Tweet;

/**
 * Created by tbalogh on 16/08/16.
 */
public interface TweetView extends LoadingView {
    void showNewTweet(Tweet tweet);

    void showTweets(List<Tweet> tweets);

    void clearTweets();

    void showConnectedToStream();

    void showDisconnectedFromStream();

    void showConnectedToNetwork();

    void showDisconnectedFromNetwork();

    void showError(String errorMessage);

    String getKeyword();
}
