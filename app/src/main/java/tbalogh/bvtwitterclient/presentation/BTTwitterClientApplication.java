package tbalogh.bvtwitterclient.presentation;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import tbalogh.bvtwitterclient.presentation.di.component.ApplicationComponent;
import tbalogh.bvtwitterclient.presentation.di.component.DaggerApplicationComponent;
import tbalogh.bvtwitterclient.presentation.di.module.ApplicationModule;
import tbalogh.bvtwitterclient.presentation.di.module.DatabaseModule;
import tbalogh.bvtwitterclient.presentation.di.module.NetworkModule;
import tbalogh.bvtwitterclient.presentation.di.module.TwitterModule;

/**
 * Created by tbalogh on 17/08/16.
 */
public class BTTwitterClientApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
        initInjector();
    }

    private void initRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }

    private void initInjector() {
        this.applicationComponent =
                DaggerApplicationComponent.builder()
                                          .applicationModule(new ApplicationModule(this))
                                          .networkModule(new NetworkModule())
                                          .databaseModule(new DatabaseModule())
                                          .twitterModule(new TwitterModule())
                                          .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
