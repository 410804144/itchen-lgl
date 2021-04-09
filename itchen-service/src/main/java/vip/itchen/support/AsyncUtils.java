package vip.itchen.support;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author alabimofa
 */
@Slf4j
public class AsyncUtils {

    private static final ExecutorService executorService;
    static {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("async-pool-%d").build();
        executorService = new ThreadPoolExecutor(50, 80, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 异步执行任务
     *
     * @param task 任务
     */
    public static void execute(Task task) {
        executorService.execute(() -> {
            try {
                task.doTask();
            } catch (Exception e) {
                log.error("[task execute error]", e);
            }
        });
    }

    public interface Task {
        void doTask();
    }
}
