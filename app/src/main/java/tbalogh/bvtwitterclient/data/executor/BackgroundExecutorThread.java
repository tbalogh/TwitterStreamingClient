package tbalogh.bvtwitterclient.data.executor;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by tbalogh on 16/08/16.
 */
public class BackgroundExecutorThread extends HandlerThread {

    private Handler workerHandler;

    public BackgroundExecutorThread(String name) {
        super(name);
    }

    public void execute(Runnable task) {
        this.workerHandler.post(task);
    }

    public void initialize() {
        this.workerHandler = new Handler(getLooper());
    }
}
