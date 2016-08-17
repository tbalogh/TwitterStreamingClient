package tbalogh.bvtwitterclient.data.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by tbalogh on 17/08/16.
 */
@Singleton
public class NetworkStateReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkStateReceiver";

    private NetworkInfo.State connectionState = NetworkInfo.State.UNKNOWN;
    private NetworkStateListener networkStateListener;

    @Inject
    public NetworkStateReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        switch (action) {
            case ConnectivityManager.CONNECTIVITY_ACTION:
                handleNetworkStateChanged(intent);
                break;
            default:
                break;
        }
    }

    public void setNetworkListener(NetworkStateListener networkStateListener) {
        this.networkStateListener = networkStateListener;
    }

    public void removeNetworkStateListener(NetworkStateListener networkStateListener) {
        if (this.networkStateListener == networkStateListener) {
            this.networkStateListener = null;
        }
    }

    private void handleNetworkStateChanged(Intent intent) {
        boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        switch (this.connectionState) {
            case UNKNOWN:
                this.connectionState = noConnectivity ?
                        NetworkInfo.State.DISCONNECTED : NetworkInfo.State.CONNECTED;
                break;
            case CONNECTED:
                if (noConnectivity) {
                    this.connectionState = NetworkInfo.State.DISCONNECTED;
                    notifyDisconnected();
                }
                break;
            case DISCONNECTED:
                if (!noConnectivity) {
                    this.connectionState = NetworkInfo.State.CONNECTED;
                    notifyConnected();
                }
                break;
            default:
                break;
        }
    }

    private void notifyConnected() {
        Log.d(TAG, "notifyConnected: ");
        if (this.networkStateListener != null) {
            this.networkStateListener.onConnected();
        }
    }

    private void notifyDisconnected() {
        Log.d(TAG, "notifyDisconnected: ");
        if (this.networkStateListener != null) {
            this.networkStateListener.onDisconnected();
        }
    }

    public static IntentFilter createIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        return intentFilter;
    }
}
