package tbalogh.bvtwitterclient.domain;

import java.util.List;

/**
 * Created by tbalogh on 16/08/16.
 */
public interface TweetStreamSubscriber {

    void onException(Exception ex);

    void onStreamConnected();

    void onStreamDisconnected();

    void onTweetReceived(Tweet tweet);

    void onTweetsReceived(List<Tweet> tweets);
}
