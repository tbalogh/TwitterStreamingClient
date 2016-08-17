package tbalogh.bvtwitterclient.presentation.di.component;

import dagger.Component;
import tbalogh.bvtwitterclient.presentation.activity.TweetActivity;
import tbalogh.bvtwitterclient.presentation.di.PerActivity;

/**
 * Created by tbalogh on 17/08/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class)
public interface TweetComponent {
    void inject(TweetActivity tweetActivity);
}
