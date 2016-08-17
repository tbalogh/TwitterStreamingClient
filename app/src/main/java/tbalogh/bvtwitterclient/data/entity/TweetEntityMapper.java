package tbalogh.bvtwitterclient.data.entity;

import javax.inject.Inject;

import twitter4j.Status;

/**
 * Created by tbalogh on 16/08/16.
 */
public class TweetEntityMapper {

    private static long DEFAULT_LIFESPAN = 25 * 1000;
    private long lifeSpan;

    @Inject
    public TweetEntityMapper() {
        this.lifeSpan = DEFAULT_LIFESPAN;
    }

    public TweetEntity mapStatus(Status status) {
        return new TweetEntity(
                status.getText(),
                status.getUser().getName(),
                status.getUser().getScreenName(),
                status.getUser().getProfileImageURL(),
                status.getRetweetCount(),
                status.getFavoriteCount(),
                System.currentTimeMillis() + lifeSpan
        );
    }

    public void setLifeSpan(long lifeSpan) {
        this.lifeSpan = lifeSpan;
    }
}
