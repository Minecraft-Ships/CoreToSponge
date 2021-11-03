package org.core.implementation.sponge.scheduler;

import net.kyori.adventure.util.Ticks;
import org.core.TranslateCore;
import org.core.implementation.sponge.platform.SpongePlatformServer;
import org.core.platform.plugin.Plugin;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;
import org.core.schedule.unit.TimeUnit;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.plugin.PluginContainer;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class SScheduler implements Scheduler {

    protected final Runnable taskToRun;
    protected Scheduler runAfter;
    protected final Integer delayCount;
    protected final TimeUnit delayTimeUnit;
    protected final Integer iteration;
    protected final TimeUnit iterationTimeUnit;
    protected final Plugin plugin;
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
        if (unit1==TimeUnit.SECONDS) {
            unit = java.util.concurrent.TimeUnit.SECONDS;
        } else if (unit1==TimeUnit.MINUTES) {
            unit = java.util.concurrent.TimeUnit.MINUTES;
        } else {
            throw new IllegalStateException("Unknown unit");
        }
        return unit;
    }

    @Override
    public void run() {
        Object spongePlugin = this.plugin.getPlatformLauncher();
        PluginContainer container;
        try {
            container = (PluginContainer) spongePlugin.getClass().getMethod("getContainer").invoke(spongePlugin);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        Task.Builder builder = Task.builder().plugin(container).execute(new SScheduler.RunAfterScheduler());
        if (this.delayCount!=null) {
            if (this.delayTimeUnit==null || this.delayTimeUnit==TimeUnit.MINECRAFT_TICKS) {
                builder = builder.delay(Ticks.duration(this.delayCount));
            } else {
                builder = builder.delay(this.delayCount, this.to(this.delayTimeUnit));
            }
        }
        if (this.iteration!=null) {
            if (this.iterationTimeUnit==null || this.iterationTimeUnit==TimeUnit.MINECRAFT_TICKS) {
                builder = builder.interval(Ticks.duration(this.iteration));
            } else {
                builder = builder.interval(this.iteration, this.to(this.iterationTimeUnit));
            }
        }
        this.task = ((SpongePlatformServer) TranslateCore.getServer()).getServer().scheduler().submit(builder.build());
    }

    @Override
    public void cancel() {
        this.getSpongeTask().ifPresent(ScheduledTask::cancel);
    }

    @Override
    public Runnable getExecutor() {
        return this.taskToRun;
    }

    private class RunAfterScheduler implements Runnable {

        @Override
        public void run() {
            SScheduler.this.taskToRun.run();
            Scheduler scheduler = SScheduler.this.runAfter;
            if (scheduler!=null) {
                scheduler.run();
            }
        }
    }
}
