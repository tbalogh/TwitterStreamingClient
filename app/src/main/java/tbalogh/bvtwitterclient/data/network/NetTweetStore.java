package tbalogh.bvtwitterclient.data.network;

import twitter4j.ConnectionLifeCycleListener;
import twitter4j.StatusListener;

/**
 * Created by tbalogh on 16/08/16.
 */
public interface NetTweetStore {
    void filter(String keyword);

    void closeStreams();

    void setStatusListener(StatusListener statusListener);

    void removeStatusListener(StatusListener statusListener);

    void setConnectionLifeCycleListener(ConnectionLifeCycleListener connectionLifeCycleListener);
}
