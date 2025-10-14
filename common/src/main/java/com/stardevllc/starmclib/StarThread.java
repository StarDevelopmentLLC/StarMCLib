package com.stardevllc.starmclib;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public abstract class StarThread<T extends JavaPlugin> extends BukkitRunnable {
    public static final Set<StarThread<?>> THREADS = Collections.synchronizedSet(new HashSet<>());

    protected T plugin;
    protected String name;
    protected ThreadOptions threadOptions;

    //This is the performance metrics
    private long minTime, maxTime;
    private long totalRuns, successfulRuns, failedRuns;
    private long[] msMostRecent = new long[100];
    private long[] nsMostRecent = new long[100];
    private int mostRecentCounter;

    private boolean cancelled;

    public StarThread(T plugin, long period, long delay, boolean async) {
        this(plugin, "", new ThreadOptions().period(period).delay(delay).async(async).repeating(true));
    }

    public StarThread(T plugin, long period, boolean async) {
        this(plugin, period, 0L, async);
    }

    public StarThread(T plugin, String name, ThreadOptions threadOptions) {
        this.plugin = plugin;
        this.threadOptions = threadOptions;
        this.name = name;
        THREADS.add(this);
    }

    public final void run() {
        long msStart = System.currentTimeMillis();
        long nsStart = System.nanoTime();
        try {
            this.onRun();
            this.successfulRuns++;
        } catch (Throwable throwable) {
            this.failedRuns++;
            plugin.getLogger().severe("Thread " + getClass().getName() + " had an error while running.");
            throwable.printStackTrace();
        }
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

        if (this.threadOptions.getPeriod() == 0L) {
            cancelled = true;
        }

        if (isCancelled()) {
            THREADS.remove(this);
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

    public long getPeriod() {
        return this.threadOptions.getPeriod();
    }

    public long getSuccessfulRuns() {
        return successfulRuns;
    }

    public long getFailedRuns() {
        return failedRuns;
    }

    //Millisecond Mean Time
    public long getMeanTime() {
        int counter = 0;
        long total = 0L;
        for (long l : this.msMostRecent) {
            if (l > 0) {
                total += l;
                counter++;
            }
        }

        return total / counter;
    }

    //Millisecond Median time
    public long getMedianTime() {
        long[] times = new long[this.msMostRecent.length];
        System.arraycopy(this.msMostRecent, 0, times, 0, times.length);
        Arrays.sort(times);
        return times[times.length / 2];
    }

    //Millisecond Mode time
    public long getModeTime() {
        Map<Long, Integer> counts = new HashMap<>();
        for (long time : this.msMostRecent) {
            if (time > 0) {
                if (counts.containsKey(time)) {
                    counts.put(time, counts.get(time) + 1);
                } else {
                    counts.put(time, 1);
                }
            }
        }

        long mostAmount = 0L;
        for (Map.Entry<Long, Integer> entry : counts.entrySet()) {
            Long time = entry.getKey();
            Integer count = entry.getValue();
            if (count > mostAmount) {
                mostAmount = time;
            }
        }

        return mostAmount;
    }

    public synchronized boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        cancelled = true;
        THREADS.remove(this);
        super.cancel();
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
