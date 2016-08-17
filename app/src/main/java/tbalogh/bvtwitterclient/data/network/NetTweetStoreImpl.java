package tbalogh.bvtwitterclient.data.network;

import javax.inject.Inject;
import javax.inject.Singleton;

import tbalogh.bvtwitterclient.data.executor.BackgroundExecutorThread;
import twitter4j.ConnectionLifeCycleListener;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

/**
 * Created by tbalogh on 16/08/16.
 */
@Singleton
public class NetTweetStoreImpl implements NetTweetStore {

    private BackgroundExecutorThread executorThread;
    private TwitterStream            stream;

    @Inject
    public NetTweetStoreImpl(TwitterStream stream) {
        this.executorThread = new BackgroundExecutorThread("TweetThread");
        this.executorThread.start();
        this.executorThread.initialize();
        this.stream = stream;
    }

    public void setStatusListener(StatusListener statusListener) {
        if (this.stream != null) {
            this.stream.addListener(statusListener);
        }
    }

    public void removeStatusListener(StatusListener statusListener) {
        if (this.stream != null) {
            this.stream.removeListener(statusListener);
        }
    }

    public void setConnectionLifeCycleListener(ConnectionLifeCycleListener connectionLifeCycleListener) {
        if (this.stream != null) {
            this.stream.addConnectionLifeCycleListener(connectionLifeCycleListener);
        }
    }

    public void filter(String keyword) {
        executorThread.execute(() -> {
            this.stream.cleanUp();
            this.stream.filter(keyword);
        });
    }

    @Override
    public void closeStreams() {
        executorThread.execute(() -> this.stream.cleanUp());
    }
}
