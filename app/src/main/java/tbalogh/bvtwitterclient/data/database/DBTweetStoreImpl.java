package tbalogh.bvtwitterclient.data.database;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import tbalogh.bvtwitterclient.data.entity.TweetEntity;
import tbalogh.bvtwitterclient.data.executor.BackgroundExecutorThread;

/**
 * Created by tbalogh on 17/08/16.
 */
@Singleton
public class DBTweetStoreImpl implements DBTweetStore, TweetEntitiesListener {

    private final DbBackgroundExecutorThread executorThread;
    private       TweetEntitiesListener      tweetEntitiesListener;

    @Inject
    public DBTweetStoreImpl() {
        this.executorThread = new DbBackgroundExecutorThread(BackgroundExecutorThread.class.getSimpleName());
        this.executorThread.setTweetEntitiesListener(this);
        this.executorThread.start();
        this.executorThread.initialize();
    }

    @Override
    public void setTweetEntitiesListener(TweetEntitiesListener tweetEntitiesListener) {
        this.tweetEntitiesListener = tweetEntitiesListener;
    }

    @Override
    public void removeTweetEntitiesListener(TweetEntitiesListener tweetEntitiesListener) {
        if (this.tweetEntitiesListener == tweetEntitiesListener) {
            this.tweetEntitiesListener = null;
        }
    }

    @Override
    public void open() {
        this.executorThread.openRealm();
    }

    @Override
    public void close() {
        this.executorThread.closeRealm();
    }

    @Override
    public void save(TweetEntity tweetEntity) {
        this.executorThread.save(tweetEntity);
    }

    @Override
    public void clearTweetEntities() {
        this.executorThread.clearTweetEntities();
    }

    @Override
    public void tweetEntities() {
        this.executorThread.tweetEntities();
    }

    @Override
    public void removeOldTweetEntities() {
        this.executorThread.removeOldTweetEntities();
    }

    @Override
    public void onTweetEntities(List<TweetEntity> tweetEntities) {
        if (this.tweetEntitiesListener != null) {
            tweetEntitiesListener.onTweetEntities(tweetEntities);
        }
    }

    private class DbBackgroundExecutorThread extends BackgroundExecutorThread {

        private static final String TAG = "DbBackgroundExecutorThread";

        private Realm                 realm;
        private TweetEntitiesListener tweetEntitiesListener;

        public DbBackgroundExecutorThread(String name) {
            super(name);
        }

        public void setTweetEntitiesListener(TweetEntitiesListener tweetEntitiesListener) {
            this.tweetEntitiesListener = tweetEntitiesListener;
        }

        public void removeTweetEntitiesListener(TweetEntitiesListener tweetEntitiesListener) {
            if (this.tweetEntitiesListener == tweetEntitiesListener) {
                this.tweetEntitiesListener = null;
            }
        }

        @Override
        public void initialize() {
            super.initialize();
            openRealm();
        }

        public void openRealm() {
            execute(() -> {
                if (this.realm == null) {
                    this.realm = Realm.getDefaultInstance();
                }
            });
        }

        public void closeRealm() {
            execute(() -> {
                this.realm.close();
                this.realm = null;
            });
        }

        public void save(TweetEntity tweetEntity) {
            execute(() -> {
                this.realm.beginTransaction();
                this.realm.copyToRealm(tweetEntity);
                this.realm.commitTransaction();
            });
        }

        public void clearTweetEntities() {
            execute(() -> {
                this.realm.beginTransaction();
                this.realm.delete(TweetEntity.class);
                this.realm.commitTransaction();
            });
        }

        public void tweetEntities() {
            execute(() -> {
                try {
                    RealmResults<TweetEntity> tweetEntities = this.realm.where(TweetEntity.class)
                                                                        .findAll()
                                                                        .sort(TweetEntity.INVALIDATION_TIMESTAMP_FIELD, Sort.DESCENDING);
                    if (this.tweetEntitiesListener != null) {
                        this.tweetEntitiesListener.onTweetEntities(tweetEntities);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

        public void removeOldTweetEntities() {
            execute(() -> {
                RealmResults<TweetEntity> oldEntities = this.realm.where(TweetEntity.class)
                                                                  .lessThan(TweetEntity.INVALIDATION_TIMESTAMP_FIELD,
                                                                          System.currentTimeMillis())
                                                                  .findAll();
                if (!oldEntities.isEmpty()) {
                    Log.d(TAG, oldEntities.size() + " tweets deleted!");
                }
                this.realm.beginTransaction();
                oldEntities.deleteAllFromRealm();
                this.realm.commitTransaction();
            });
        }
    }
}
