package tbalogh.bvtwitterclient.data.wrapper;

import android.util.Log;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

/**
 * Created by tbalogh on 17/08/16.
 */
public abstract class DefaultStatusListener implements StatusListener {

    private static final String TAG = DefaultStatusListener.class.getSimpleName();

    @Override
    public void onStatus(Status status) {
        Log.d(TAG, "onStatus: ");
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        Log.d(TAG, "onDeletionNotice: ");
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        Log.d(TAG, "onTrackLimitationNotice: ");
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        Log.d(TAG, "onScrubGeo: ");
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        Log.d(TAG, "onStallWarning: ");
    }
}
