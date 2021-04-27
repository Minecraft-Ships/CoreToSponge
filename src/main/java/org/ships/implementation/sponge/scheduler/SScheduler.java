package org.ships.implementation.sponge.scheduler;

import net.kyori.adventure.util.Ticks;
import org.core.CorePlugin;
import org.core.platform.Plugin;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;
import org.core.schedule.unit.TimeUnit;
import org.ships.implementation.sponge.platform.SpongePlatformServer;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;

import java.util.Optional;

public class SScheduler implements Scheduler {

    private class RunAfterScheduler implements Runnable {

        @Override
        public void run() {
            SScheduler.this.taskToRun.run();
            Scheduler scheduler = SScheduler.this.runAfter;
            if (scheduler != null) {
                scheduler.run();
            }
        }
    }

    protected Runnable taskToRun;
    protected Scheduler runAfter;
    protected Integer delayCount;
    protected TimeUnit delayTimeUnit;
    protected Integer iteration;
    protected TimeUnit iterationTimeUnit;
    protected Plugin plugin;
    protected ScheduledTask task;

    public SScheduler(SchedulerBuilder builder, Plugin plugin) {
        this.taskToRun = builder.getExecutor();
        this.iteration = builder.getIteration().orElse(null);
        this.iterationTimeUnit = builder.getIterationUnit().orElse(null);
        this.delayCount = builder.getDelay().orElse(0);
        this.delayTimeUnit = builder.getDelayUnit().orElse(null);
        this.plugin = plugin;
        builder.getToRunAfter().ifPresent(s -> this.runAfter = s);
    }

    public Optional<ScheduledTask> getSpongeTask() {
        return Optional.ofNullable(this.task);
    }

    private java.util.concurrent.TimeUnit to(TimeUnit unit1) {
        java.util.concurrent.TimeUnit unit;
        if (unit1.equals(TimeUnit.SECONDS)) {
            unit = java.util.concurrent.TimeUnit.SECONDS;
        } else if (unit1.equals(TimeUnit.MINUTES)) {
            unit = java.util.concurrent.TimeUnit.MINUTES;
        } else {
            throw new IllegalStateException("Unknown unit");
        }
        return unit;
    }

    @Override
    public void run() {
        Task.Builder builder = Task.builder().execute(new SScheduler.RunAfterScheduler());
        if (this.delayCount != null) {
            if (this.delayTimeUnit == null || this.delayTimeUnit == TimeUnit.MINECRAFT_TICKS) {
                builder = builder.delay(Ticks.duration(this.delayCount));
            } else {
                builder = builder.delay(this.delayCount, to(this.delayTimeUnit));
            }
        }
        if (this.iteration != null) {
            if (this.iterationTimeUnit == null || this.iterationTimeUnit == TimeUnit.MINECRAFT_TICKS) {
                builder = builder.interval(Ticks.duration(this.iteration));
            } else {
                builder = builder.interval(this.iteration, to(this.iterationTimeUnit));
            }
        }
        this.task = ((SpongePlatformServer) CorePlugin.getServer()).getServer().scheduler().submit(builder.build());
    }

    @Override
    public void cancel() {
        getSpongeTask().ifPresent(ScheduledTask::cancel);
    }

    @Override
    public Runnable getExecutor() {
        return this.taskToRun;
    }
}
