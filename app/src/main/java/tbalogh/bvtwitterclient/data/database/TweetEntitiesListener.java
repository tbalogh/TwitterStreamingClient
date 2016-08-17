package tbalogh.bvtwitterclient.data.database;

import java.util.List;

import tbalogh.bvtwitterclient.data.entity.TweetEntity;

/**
 * Created by tbalogh on 17/08/16.
 */
public interface TweetEntitiesListener {
    void onTweetEntities(List<TweetEntity> tweetEntities);
}
