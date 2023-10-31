package com.stardevllc.starmclib.task;

import com.stardevllc.starlib.task.Task;
import org.bukkit.scheduler.BukkitTask;

public class SpigotTask implements Task {

    private BukkitTask task;

    public SpigotTask(BukkitTask task) {
        this.task = task;
    }

    @Override
    public int getTaskId() {
        return task.getTaskId();
    }

    @Override
    public boolean isSync() {
        return task.isSync();
    }

    @Override
    public boolean cancel() {
        task.cancel();
        return true;
    }
}
