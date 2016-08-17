package tbalogh.bvtwitterclient.presentation.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tbalogh.bvtwitterclient.data.database.DBTweetStore;
import tbalogh.bvtwitterclient.data.database.DBTweetStoreImpl;

/**
 * Created by tbalogh on 17/08/16.
 */
@Module
public class DatabaseModule {
    @Provides
    @Singleton
    DBTweetStore provideDBTweetStore(DBTweetStoreImpl dbTweetStore) {
        return dbTweetStore;
    }
}
