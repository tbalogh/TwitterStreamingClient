package tbalogh.bvtwitterclient.data.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by tbalogh on 17/08/16.
 */
@Singleton
public class NetworkManager implements NetworkStateListener {

    private final Context              context;
    private final ConnectivityManager  connectivityManager;
    private final NetworkStateReceiver networkStateReceiver;

    private NetworkStateListener networkStateListener;

    @Inject
    public NetworkManager(Context context, NetworkStateReceiver networkStateReceiver) {
        this.context = context;
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.networkStateReceiver = networkStateReceiver;
        this.networkStateReceiver.setNetworkListener(this);
    }

    public void setNetworkListener(NetworkStateListener networkStateListener) {
        this.networkStateListener = networkStateListener;
    }

    public void removeNetworkStateListener(NetworkStateListener networkStateListener) {
        if (this.networkStateListener == networkStateListener) {
            this.networkStateListener = null;
        }
    }

    public void registerNetworkStateReceiver() {
        this.context.registerReceiver(this.networkStateReceiver,
                NetworkStateReceiver.createIntentFilter());
    }

    public void unregisterNetworkStateReceiver() {
        this.context.unregisterReceiver(this.networkStateReceiver);
    }


    public boolean isConnected() {
        NetworkInfo networkInfo = this.connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onConnected() {
        if (this.networkStateListener != null) {
            this.networkStateListener.onConnected();
        }
    }

    @Override
    public void onDisconnected() {
        if (this.networkStateListener != null) {
            this.networkStateListener.onDisconnected();
        }
    }
}
