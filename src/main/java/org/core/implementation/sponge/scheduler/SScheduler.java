package org.core.implementation.sponge.scheduler;

import net.kyori.adventure.util.Ticks;
import org.core.TranslateCore;
import org.core.implementation.sponge.platform.SpongePlatformServer;
import org.core.platform.plugin.Plugin;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;
import org.core.schedule.unit.TimeUnit;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.plugin.PluginContainer;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;
import java.util.function.Consumer;

public class SScheduler implements Scheduler.Native {

    private class RunAfterScheduler implements Runnable {

        @Override
        public void run() {
            SScheduler.this.taskToRun.accept(SScheduler.this);
            Scheduler scheduler = SScheduler.this.runAfter;
            if (scheduler != null) {
                scheduler.run();
            }
        }
    }

    private final Consumer<Scheduler> taskToRun;
    private final Integer delayCount;
    private final TimeUnit delayTimeUnit;
    private final Integer iteration;
    private final TimeUnit iterationTimeUnit;
    private final Plugin plugin;
    private Scheduler runAfter;
    private ScheduledTask task;
    private String displayName;
    private boolean async;
    private boolean isDelayedOnly;
    private LocalTime startTime;
    private Duration delayDuration;

    public SScheduler(SchedulerBuilder builder, Plugin plugin, boolean isDelayedOnly) {
        this.taskToRun = builder.getRunner();
        this.iteration = builder.getIteration().orElse(null);
        this.iterationTimeUnit = builder.getIterationUnit().orElse(null);
        this.delayCount = builder.getDelay().orElse(0);
        this.delayTimeUnit = builder.getDelayUnit().orElse(null);
        this.plugin = plugin;
        this.async = builder.isAsync();
        this.displayName = builder.getDisplayName().orElseThrow(() -> new IllegalStateException("No display name set"));
        this.isDelayedOnly = isDelayedOnly;
        builder.getToRunAfter().ifPresent(s -> this.runAfter = s);
    }

    public Optional<ScheduledTask> getSpongeTask() {
        return Optional.ofNullable(this.task);
    }

    private java.util.concurrent.TimeUnit to(TimeUnit unit1) {
        java.util.concurrent.TimeUnit unit;
        if (unit1 == TimeUnit.SECONDS) {
            unit = java.util.concurrent.TimeUnit.SECONDS;
        } else if (unit1 == TimeUnit.MINUTES) {
            unit = java.util.concurrent.TimeUnit.MINUTES;
        } else {
            throw new IllegalStateException("Unknown unit");
        }
        return unit;
    }

    @Override
    public Optional<LocalTime> getStartScheduleTime() {
        return Optional.empty();
    }

    @Override
    public Optional<LocalTime> getStartRunnerTime() {
        return Optional.empty();
    }

    @Override
    public Optional<LocalTime> getEndTime() {
        if (!this.isDelayedOnly) {
            return Optional.empty();
        }
        if (this.startTime == null) {
            return Optional.empty();
        }
        return Optional.of(this.startTime.plus(this.delayDuration));
    }

    @Override
    public boolean isAsync() {
        return this.async;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
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
        if (this.delayCount != null) {
            if (this.delayTimeUnit == null || this.delayTimeUnit == TimeUnit.MINECRAFT_TICKS) {
                this.delayDuration = Ticks.duration(this.delayCount);
                builder = builder.delay(delayDuration);
            } else {
                java.util.concurrent.TimeUnit unit = this.to(this.delayTimeUnit);
                builder = builder.delay(this.delayCount, unit);
                this.delayDuration = Duration.of(this.delayCount, unit.toChronoUnit());
            }
        }
        if (this.iteration != null && !this.isDelayedOnly) {
            if (this.iterationTimeUnit == null || this.iterationTimeUnit == TimeUnit.MINECRAFT_TICKS) {
                builder = builder.interval(Ticks.duration(this.iteration));
            } else {
                builder = builder.interval(this.iteration, this.to(this.iterationTimeUnit));
            }
        }
        Server server = ((SpongePlatformServer) TranslateCore.getServer()).getServer();
        this.startTime = LocalTime.now();
        if (this.async) {
            this.task = Sponge.asyncScheduler().submit(builder.build());
        } else {
            this.task = server.scheduler().submit(builder.build());
        }
    }

    @Override
    public Consumer<Scheduler> getRunner() {
        return this.taskToRun;
    }

    @Override
    public void cancel() {
        this.getSpongeTask().ifPresent(ScheduledTask::cancel);
    }
}
