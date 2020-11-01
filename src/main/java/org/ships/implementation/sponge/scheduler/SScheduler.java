package org.ships.implementation.sponge.scheduler;

import org.core.platform.Plugin;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;
import org.core.schedule.unit.TimeUnit;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.TemporalUnits;

import java.time.temporal.TemporalUnit;
import java.util.Optional;

public class SScheduler implements Scheduler {

    private class RunAfterScheduler implements Runnable {

        @Override
        public void run() {
            SScheduler.this.taskToRun.run();
            Scheduler scheduler = SScheduler.this.runAfter;
            if(scheduler != null){
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

    public SScheduler(SchedulerBuilder builder, Plugin plugin){
        this.taskToRun = builder.getExecutor();
        this.iteration = builder.getIteration().orElse(null);
        this.iterationTimeUnit = builder.getIterationUnit().orElse(null);
        this.delayCount = builder.getDelay().orElse(0);
        this.delayTimeUnit = builder.getDelayUnit().orElse(null);
        this.plugin = plugin;
        builder.getToRunAfter().ifPresent(s -> this.runAfter = s);
    }

    public Optional<ScheduledTask> getSpongeTask(){
        return Optional.ofNullable(this.task);
    }

    private TemporalUnit to(TimeUnit unit1){
        TemporalUnit unit;
        if(unit1.equals(TimeUnit.MINECRAFT_TICKS)){
            unit = TemporalUnits.MINECRAFT_TICKS;
        }else if(unit1.equals(TimeUnit.SECONDS)){
            unit = TemporalUnits.SECONDS;
        }else if(unit1.equals(TimeUnit.MINUTES)){
            unit = TemporalUnits.MINUTES;
        }else{
            throw new IllegalStateException("Unknown unit");
        }
        return unit;
    }

    @Override
    public void run() {
        Task.Builder builder = Task.builder().execute(new SScheduler.RunAfterScheduler());
        if(this.delayCount != null){
            if(this.delayTimeUnit == null){
                builder = builder.delayTicks(this.delayCount);
            }else{
                builder = builder.delay(this.delayCount, to(this.delayTimeUnit));
            }
        }
        if(this.iteration != null){
            if(this.iterationTimeUnit == null){
                builder = builder.intervalTicks(this.iteration);
            }else{
                builder = builder.interval(this.iteration, to(this.iterationTimeUnit));
            }
        }
        this.task = Sponge.getServer().getScheduler().submit(builder.build());
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
