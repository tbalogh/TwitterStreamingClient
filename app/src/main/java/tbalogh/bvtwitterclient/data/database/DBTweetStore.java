package tbalogh.bvtwitterclient.data.database;

import tbalogh.bvtwitterclient.data.entity.TweetEntity;

/**
 * Created by tbalogh on 16/08/16.
 */
public interface DBTweetStore {
    void open();

    void close();

    void save(TweetEntity tweetEntity);

    void clearTweetEntities();

    void tweetEntities();

    void removeOldTweetEntities();

    void setTweetEntitiesListener(TweetEntitiesListener tweetEntitiesListener);

    void removeTweetEntitiesListener(TweetEntitiesListener tweetEntitiesListener);
}
