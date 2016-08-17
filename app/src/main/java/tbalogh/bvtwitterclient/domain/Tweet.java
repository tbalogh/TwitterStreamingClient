package tbalogh.bvtwitterclient.domain;

/**
 * Created by tbalogh on 17/08/16.
 */
public class Tweet {

    private final String text;
    private final String userName;
    private final String screenName;
    private final String photoUrl;
    private final int retweetCount;
    private final int likesCount;

    public Tweet(String text, String userName, String screenName, String photoUrl, int retweetCount, int likesCount) {
        this.text = text;
        this.userName = userName;
        this.screenName = screenName;
        this.photoUrl = photoUrl;
        this.retweetCount = retweetCount;
        this.likesCount = likesCount;
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
