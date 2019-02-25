package me.ift8.basic.trace.core;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.NullMessage;
import com.dianping.cat.message.spi.MessageTree;
import lombok.extern.slf4j.Slf4j;
import me.ift8.basic.trace.core.enums.TraceTypeEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by IFT8 on 2019-02-01.
 */
@Slf4j
public class Trace {

    public static Transaction newTransaction(TraceTypeEnum type, String name) {
        return Cat.newTransaction(type.getCode(), name);
    }

    public static Event newEvent(TraceTypeEnum type, String name) {
        return Cat.newEvent(type.getCode(), name);
    }

    public static void logError(Throwable cause) {
        Cat.logError(cause);
    }

    public static void logError(String name, Throwable cause) {
        Cat.logError(name, cause);
    }

    /**
     * 只获取不生成
     */
    public static String getCurrentMessageIdOnly() {
        MessageTree tree = Cat.getManager().getThreadLocalMessageTree();

        if (tree != null) {
            return tree.getMessageId();
        } else {
            return null;
        }
    }

    /**
     * 创建并保存
     */
    public static Transaction startNewTrace(TraceTypeEnum traceType, String name) {
        MessageTree tree = Cat.getManager().getThreadLocalMessageTree();

        if (tree != null) {
            tree.setParentMessageId(null);
            String messageId = Cat.createMessageId();
            tree.setRootMessageId(messageId);
            tree.setMessageId(messageId);
            if (traceType != null) {
                return Trace.newTransaction(traceType, name);
            }
        }
        return NullMessage.TRANSACTION;
    }

    /**
     * 继续Trace
     */
    public static Transaction continueTrace(String rootId, String parentId, TraceTypeEnum traceType, String name) {
        MessageTree tree = Cat.getManager().getThreadLocalMessageTree();

        if (tree != null) {
            String childId = Cat.createMessageId();
            tree.setRootMessageId(rootId);
            tree.setParentMessageId(parentId);
            tree.setMessageId(childId);
        }
        if (traceType != null) {
            return Trace.newTransaction(traceType, name);
        }
        return NullMessage.TRANSACTION;
    }

    public static String getRootMessageId() {
        MessageTree tree = Cat.getManager().getThreadLocalMessageTree();
        if (tree != null) {
            return tree.getRootMessageId();
        }
        return null;
    }

    public static String getParentMessageId() {
        MessageTree tree = Cat.getManager().getThreadLocalMessageTree();
        if (tree != null) {
            return tree.getParentMessageId();
        }
        return null;
    }

    public static void logMetricForCount(String name) {
        logMetricForCount(name, 1);
    }

    public static void logMetricForCount(String name, int quantity) {
        logMetricForCount(name, quantity, new HashMap<>());
    }

    public static void logMetricForCount(String name, int quantity, Map<String, String> tagMap) {
        MessageTree tree = Cat.getManager().getThreadLocalMessageTree();
        String rootMessageId = tree.getRootMessageId();
        String parentMessageId = tree.getParentMessageId();
        String messageId = tree.getMessageId();

        Cat.logMetricForCount(name, quantity, tagMap);

        //回写 (logMetricForCount会清空Trace)
        tree = Cat.getManager().getThreadLocalMessageTree();
        if (tree != null) {
            tree.setRootMessageId(rootMessageId);
            tree.setParentMessageId(parentMessageId);
            tree.setMessageId(messageId);
        }
    }

    public static void logMetricForDuration(String name, long durationInMillis) {
        logMetricForDuration(name, durationInMillis, new HashMap<>());
    }

    public static void logMetricForDuration(String name, long durationInMillis, Map<String, String> tags) {
        Cat.logMetricForDuration(name, durationInMillis, tags);
    }

    public static <T> T newEvent(TraceTypeEnum type, String name, DoInTraceAndRetFun<T> fun) throws Throwable {
        Event event = Trace.newEvent(type, name);

        try {
            T result = fun.doInTraceAndRet();
            event.setStatus(Message.SUCCESS);

            return result;
        } catch (Throwable e) {
            Trace.logError(e);
            event.setStatus(e);
            throw e;
        } finally {
            event.complete();
        }
    }

    public static void logEvent(TraceTypeEnum type, String name) {
        if (type == null) {
            return;
        }
        Cat.logEvent(type.getCode(), name);
    }

    public static <T> T newTransaction(TraceTypeEnum type, String name, DoInTraceAndRetFun<T> fun) throws Throwable {
        Transaction transaction = Trace.newTransaction(type, name);

        try {
            T result = fun.doInTraceAndRet();
            transaction.setStatus(Message.SUCCESS);

            return result;
        } catch (Throwable e) {
            Trace.logError(e);

            transaction.setStatus(e);
            throw e;
        } finally {
            transaction.complete();
        }
    }

    public static <T> T continueTransaction(String rootId, String parentId, TraceTypeEnum traceType, String name, DoInTraceAndRetFun<T> fun) throws Exception {
        Transaction transaction = continueTrace(rootId, parentId, traceType, name);

        try {
            T result = fun.doInTraceAndRet();
            transaction.setStatus(Message.SUCCESS);

            return result;
        } catch (Throwable e) {
            Trace.logError(e);

            transaction.setStatus(e);
            throw e;
        } finally {
            transaction.complete();
        }
    }

    public static Runnable wrapTraceRunnable(Runnable task) {
        String rootMessageId = Trace.getRootMessageId();
        String currentMessageIdOnly = Trace.getCurrentMessageIdOnly();
        class CurrentTraceContextRunnable implements Runnable {
            @Override
            public void run() {
                //将父线程中的Trace复制到子线程中
                try {
                    Trace.continueTransaction(rootMessageId, currentMessageIdOnly, TraceTypeEnum.POOL, Thread.currentThread().getName(), () -> {
                        task.run();
                        return null;
                    });
                } catch (Throwable throwable) {
                    log.error("走不到这里 ", throwable);
                }
            }
        }
        return new CurrentTraceContextRunnable();
    }

    public static <T> Callable<T> wrapTraceCallable(Callable<T> callable) {
        String rootMessageId = Trace.getRootMessageId();
        String currentMessageIdOnly = Trace.getCurrentMessageIdOnly();
        class CurrentTraceContextCallable implements Callable<T> {
            @Override
            public T call() throws Exception {
                //将父线程中的Trace复制到子线程中
                return Trace.continueTransaction(rootMessageId, currentMessageIdOnly, TraceTypeEnum.POOL, Thread.currentThread().getName(), callable::call);
            }
        }
        return new CurrentTraceContextCallable();
    }
}
