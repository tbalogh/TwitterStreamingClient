package tbalogh.bvtwitterclient.data.repository;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import tbalogh.bvtwitterclient.data.database.DBTweetStore;
import tbalogh.bvtwitterclient.data.database.TweetEntitiesListener;
import tbalogh.bvtwitterclient.data.entity.TweetEntity;
import tbalogh.bvtwitterclient.data.entity.TweetEntityMapper;
import tbalogh.bvtwitterclient.data.entity.TweetMapper;
import tbalogh.bvtwitterclient.data.network.NetTweetStore;
import tbalogh.bvtwitterclient.data.wrapper.DefaultStatusListener;
import tbalogh.bvtwitterclient.domain.TweetRepository;
import tbalogh.bvtwitterclient.domain.TweetStreamSubscriber;
import twitter4j.ConnectionLifeCycleListener;
import twitter4j.Status;

/**
 * Created by tbalogh on 16/08/16.
 */
@Singleton
public class TweetRepositoryImpl extends DefaultStatusListener
        implements TweetRepository, ConnectionLifeCycleListener, TweetEntitiesListener {

    private static final String CONNECTIVITY_LISTENER_TAG = "ConnectivityListener";
    private static final String TAG                       = TweetRepositoryImpl.class.getSimpleName();

    private final NetTweetStore     netTweetStore;
    private final DBTweetStore      dbTweetStore;
    private final TweetEntityMapper tweetEntityMapper;
    private final TweetMapper       tweetMapper;

    private TweetStreamSubscriber tweetStreamSubscriber;
    private long                  previousStatusId;

    @Inject
    public TweetRepositoryImpl(NetTweetStore netTweetStore,
                               DBTweetStore dbTweetStore,
                               TweetEntityMapper tweetEntityMapper,
                               TweetMapper tweetMapper) {
        this.netTweetStore = netTweetStore;
        this.dbTweetStore = dbTweetStore;
        this.tweetEntityMapper = tweetEntityMapper;
        this.tweetMapper = tweetMapper;
        initialize();
    }

    @Override
    public void subscribeTweets(TweetStreamSubscriber tweetStreamSubscriber, String filter,
                                boolean isNetworkAvailable) {
        this.tweetStreamSubscriber = tweetStreamSubscriber;
        this.netTweetStore.setStatusListener(this);
        this.dbTweetStore.setTweetEntitiesListener(this);
        this.dbTweetStore.open();
        if (isNetworkAvailable) {
            this.netTweetStore.filter(filter);
        } else {
            this.dbTweetStore.tweetEntities();
        }
    }

    @Override
    public void unsusbscribeTweets(TweetStreamSubscriber tweetStreamSubscriber) {
        if (this.tweetStreamSubscriber == tweetStreamSubscriber) {
            this.tweetStreamSubscriber = null;
            this.netTweetStore.removeStatusListener(this);
            this.netTweetStore.closeStreams();
            this.dbTweetStore.removeTweetEntitiesListener(this);
            this.dbTweetStore.close();
        }
    }


    @Override
    public void tweets(TweetStreamSubscriber tweetStreamSubscriber) {
        this.tweetStreamSubscriber = tweetStreamSubscriber;
        this.netTweetStore.setStatusListener(this);
        this.dbTweetStore.setTweetEntitiesListener(this);
        this.dbTweetStore.open();
        this.dbTweetStore.tweetEntities();
    }

    @Override
    public void removeOldTweets() {
        this.dbTweetStore.removeOldTweetEntities();
    }

    @Override
    public void setTweetLifeSpan(long lifeSpan) {
        this.tweetEntityMapper.setLifeSpan(lifeSpan);
    }

    @Override
    public void removeTweets() {
        this.dbTweetStore.clearTweetEntities();
    }

    @Override
    public void onConnect() {
        if (this.tweetStreamSubscriber != null) {
            this.tweetStreamSubscriber.onStreamConnected();
        }
    }

    @Override
    public void onDisconnect() {
        if (this.tweetStreamSubscriber != null) {
            this.tweetStreamSubscriber.onStreamDisconnected();
        }
    }

    @Override
    public void onCleanUp() {
        Log.d(CONNECTIVITY_LISTENER_TAG, "onCleanUp: ");
    }

    @Override
    public void onStatus(Status status) {
        TweetEntity entity = this.tweetEntityMapper.mapStatus(status);
        Log.d(TAG, "onStatus with statusId: " + status.getId());
        if (status.getId() != previousStatusId) {
            this.previousStatusId = status.getId();
            this.dbTweetStore.save(entity);
            if (this.tweetStreamSubscriber != null) {
                this.tweetStreamSubscriber.onTweetReceived(
                        this.tweetMapper.transform(entity));
            }
        }
    }

    @Override
    public void onException(Exception ex) {
        if (this.tweetStreamSubscriber != null) {
            this.tweetStreamSubscriber.onException(ex);
        }
    }

    @Override
    public void onTweetEntities(List<TweetEntity> tweetEntities) {
        if (this.tweetStreamSubscriber != null) {
            this.tweetStreamSubscriber.onTweetsReceived(
                    this.tweetMapper.transform(tweetEntities)
            );
        }
    }

    private void initialize() {
        this.netTweetStore.setConnectionLifeCycleListener(this);
        this.netTweetStore.setStatusListener(this);
        this.dbTweetStore.setTweetEntitiesListener(this);
    }
}
