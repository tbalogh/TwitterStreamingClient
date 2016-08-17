package tbalogh.bvtwitterclient.presentation.di.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import tbalogh.bvtwitterclient.data.network.NetworkManager;
import tbalogh.bvtwitterclient.domain.TweetRepository;
import tbalogh.bvtwitterclient.presentation.di.module.ApplicationModule;
import tbalogh.bvtwitterclient.presentation.di.module.DatabaseModule;
import tbalogh.bvtwitterclient.presentation.di.module.NetworkModule;
import tbalogh.bvtwitterclient.presentation.di.module.TwitterModule;

/**
 * Created by tbalogh on 17/08/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class, DatabaseModule.class, TwitterModule.class})
public interface ApplicationComponent {
    Context context();
    TweetRepository tweetRepository();
    NetworkManager networkManager();
}
