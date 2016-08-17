package tbalogh.bvtwitterclient.presentation.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tbalogh.bvtwitterclient.data.repository.TweetRepositoryImpl;
import tbalogh.bvtwitterclient.domain.TweetRepository;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by tbalogh on 17/08/16.
 */
@Module
public class TwitterModule {

    @Singleton
    @Provides
    TweetRepository provideTweetRepository(TweetRepositoryImpl tweetRepository) {
        return tweetRepository;
    }

    @Provides
    TwitterStream provideTwitterStream(Configuration configuration) {
        return new TwitterStreamFactory(configuration).getInstance();
    }

    @Provides
    Configuration provideConfiguration() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("H4ldpeGzJo3mmVBlJ5GjB5F1S")
          .setOAuthConsumerSecret("xgTTJUjCCRdA5hRwh2v25thYrT4qLhsW75aKnV9QqBOtle1QhX")
          .setOAuthAccessToken("3506311875-dDfKORR6iiSyv7pH7L5q8XQNliDgBPa2dtYFVHX")
          .setOAuthAccessTokenSecret("f7qQGCzW7f3TPzlzpHacczRQ1AudNwk2xpLScLaPB3Yef");
        return cb.build();
    }
}
