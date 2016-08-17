package tbalogh.bvtwitterclient.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import tbalogh.bvtwitterclient.domain.Tweet;

/**
 * Created by tbalogh on 17/08/16.
 */
public class TweetMapper {

    @Inject
    public TweetMapper() {}

    public List<Tweet> transform(List<TweetEntity> tweetEntities) {
        List<Tweet> tweets = new ArrayList<>();
        if (tweetEntities != null) {
            for (TweetEntity tweetEntity : tweetEntities) {
                tweets.add(transform(tweetEntity));
            }
        }
        return tweets;
    }

    public Tweet transform(TweetEntity tweetEntity) {
        return new Tweet(
                tweetEntity.getText(),
                tweetEntity.getUserName(),
                tweetEntity.getScreenName(),
                tweetEntity.getPhotoUrl(),
                tweetEntity.getRetweetCount(),
                tweetEntity.getLikesCount());
    }
}
