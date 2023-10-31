package com.stardevllc.starmclib;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class to allow tracking performance of a task. <br>
 * This essentially is a Wrapper for BukkitRunnable to add some extra stuff to track things.
 * @param <T> The JavaPlugin owner of the thread
 */
public abstract class StarThread<T extends JavaPlugin> extends BukkitRunnable {
    private static final List<StarThread<? extends JavaPlugin>> STAR_THREADS = new ArrayList<>();

    protected T plugin;
    protected ThreadOptions threadOptions;

    //This is the performance metrics
    private long minTime, maxTime;
    private long totalRuns;
    private long[] msMostRecent = new long[100];
    private long[] nsMostRecent = new long[100];
    private int mostRecentCounter;

    public StarThread(T plugin, long period, long delay, boolean async) {
        this(plugin, new ThreadOptions().period(period).delay(delay).async(async).repeating(true));
    }

    public StarThread(T plugin, long period, boolean async) {
        this(plugin, period, 0L, async);
    }

    public StarThread(T plugin, ThreadOptions threadOptions) {
        this.plugin = plugin;
        this.threadOptions = threadOptions;
        STAR_THREADS.add(this);
    }

    public final void run() {
        long msStart = System.currentTimeMillis();
        long nsStart = System.nanoTime();
        this.onRun();
        long nsEnd = System.nanoTime();
        long msEnd = System.currentTimeMillis();

        this.totalRuns++;

        long msRuntime = msEnd - msStart;
        long nsRuntime = nsEnd - nsStart;

        if (this.minTime == 0 || msRuntime < this.minTime) {
            this.minTime = msRuntime;
        }

        this.maxTime = Math.max(this.maxTime, msRuntime);
        
        if (mostRecentCounter < 99) {
            mostRecentCounter++;
            msMostRecent[mostRecentCounter] = msRuntime;
            if (msRuntime > 0) {
                nsMostRecent[mostRecentCounter] = -1;
            } else {
                nsMostRecent[mostRecentCounter] = nsRuntime;
            }
        } else {
            long[] msMostRecentCopy = new long[100];
            System.arraycopy(this.msMostRecent, 1, msMostRecentCopy, 0, 99);
            this.msMostRecent = msMostRecentCopy;
            this.msMostRecent[99] = msRuntime;
            long[] nsMostRecentCopy = new long[100];
            System.arraycopy(this.nsMostRecent, 1, nsMostRecentCopy, 0, 99);
            this.nsMostRecent = nsMostRecentCopy;
            if (msRuntime > 0) {
                this.nsMostRecent[99] = -1;
            } else {
                this.nsMostRecent[99] = nsRuntime;
            }
        }
    }

    public abstract void onRun();

    public StarThread<T> start() {
        if (!this.threadOptions.isRepeating()) {
            if (this.threadOptions.isAsync()) {
                runTaskAsynchronously(plugin);
            } else {
                runTask(plugin);
            }
        } else {
            if (this.threadOptions.isAsync()) {
                runTaskTimerAsynchronously(plugin, threadOptions.getDelay(), threadOptions.getPeriod());
            } else {
                runTaskTimer(plugin, threadOptions.getDelay(), threadOptions.getPeriod());
            }
        }
        return this;
    }

    public T getPlugin() {
        return plugin;
    }

    public long getMinTime() {
        return minTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public long getTotalRuns() {
        return totalRuns;
    }

    public long[] getMsMostRecent() {
        return msMostRecent;
    }

    public long[] getNsMostRecent() {
        return nsMostRecent;
    }

    public int getMostRecentCounter() {
        return mostRecentCounter;
    }

    public long getTotalAverage() {
        return (this.maxTime + this.minTime) / 2;
    }

    public long getRecentAverage() {
        long totalTime = 0;
        long total = 0;
        for (long time : this.msMostRecent) {
            if (time > 0) {
                totalTime += time;
                total++;
            }
        }

        if (total == 0) {
            return 0;
        }

        return totalTime / total;
    }

    public ThreadOptions getThreadOptions() {
        return threadOptions;
    }

    public boolean isAsync() {
        return threadOptions.isAsync();
    }

    public static List<StarThread<?>> getThreads() {
        return new ArrayList<>(STAR_THREADS);
    }

    public long getPeriod() {
        return this.threadOptions.getPeriod();
    }

    public static class ThreadOptions {
        private boolean async, repeating;
        private long period, delay;

        public ThreadOptions async(boolean async) {
            this.async = async;
            return this;
        }

        public ThreadOptions repeating(boolean repeating) {
            this.repeating = repeating;
            return this;
        }

        public ThreadOptions period(long period) {
            this.period = period;
            return this;
        }

        public ThreadOptions delay(long delay) {
            this.delay = delay;
            return this;
        }

        public boolean isAsync() {
            return async;
        }

        public boolean isRepeating() {
            return repeating;
        }

        public long getPeriod() {
            return period;
        }

        public long getDelay() {
            return delay;
        }
    }
}
