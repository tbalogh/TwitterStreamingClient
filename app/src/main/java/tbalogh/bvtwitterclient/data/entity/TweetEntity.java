package tbalogh.bvtwitterclient.data.entity;

import io.realm.RealmObject;

/**
 * Created by tbalogh on 16/08/16.
 */
public class TweetEntity extends RealmObject {

    public static String INVALIDATION_TIMESTAMP_FIELD = "invalidationTimeStamp";

    private String text;
    private String userName;
    private String screenName;
    private String photoUrl;
    private int    retweetCount;
    private int    likesCount;
    private long   invalidationTimeStamp;

    public TweetEntity() {
        this.text = "";
        this.userName = "";
        this.screenName = "";
        this.photoUrl = "";
        this.retweetCount = 0;
        this.likesCount = 0;
        this.invalidationTimeStamp = 0;
    }

    public TweetEntity(String text, String userName, String screenName, String photoUrl, int retweetCount, int likesCount, long timestamp) {
        this.text = text;
        this.userName = userName;
        this.screenName = screenName;
        this.photoUrl = photoUrl;
        this.retweetCount = retweetCount;
        this.likesCount = likesCount;
        this.invalidationTimeStamp = timestamp;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setInvalidationTimeStamp(long invalidationTimeStamp) {
        this.invalidationTimeStamp = invalidationTimeStamp;
    }

    public long getInvalidationTimeStamp() {
        return invalidationTimeStamp;
    }

    public String getScreenName() {
        return screenName;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public String getText() {
        return text;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
