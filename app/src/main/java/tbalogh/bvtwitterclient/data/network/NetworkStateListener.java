package tbalogh.bvtwitterclient.data.network;

/**
 * Created by tbalogh on 17/08/16.
 */
public interface NetworkStateListener {
    void onConnected();
    void onDisconnected();
}
