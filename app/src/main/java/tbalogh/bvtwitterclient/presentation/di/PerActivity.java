package tbalogh.bvtwitterclient.presentation.di;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by tbalogh on 17/08/16.
 */
@Scope
@Retention(RUNTIME)
public @interface PerActivity {
}
