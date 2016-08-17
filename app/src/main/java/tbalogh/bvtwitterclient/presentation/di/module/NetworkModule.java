package tbalogh.bvtwitterclient.presentation.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tbalogh.bvtwitterclient.data.network.NetTweetStore;
import tbalogh.bvtwitterclient.data.network.NetTweetStoreImpl;
import tbalogh.bvtwitterclient.data.network.NetworkManager;
import tbalogh.bvtwitterclient.data.network.NetworkStateReceiver;

/**
 * Created by tbalogh on 17/08/16.
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    NetTweetStore provideNetTweetStore(NetTweetStoreImpl netTweetStore) {
        return netTweetStore;
    }

    @Provides
    @Singleton
    NetworkManager provideNetworkManager(Context context,
                                         NetworkStateReceiver networkStateReceiver) {
        return new NetworkManager(context, networkStateReceiver);
    }
}
