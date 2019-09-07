package org.ships.implementation.sponge.scheduler;

import org.core.platform.Plugin;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;
import org.spongepowered.api.scheduler.Task;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    protected Task task;

    public SScheduler(SchedulerBuilder builder, Plugin plugin){
        this.taskToRun = builder.getExecutor();
        this.iteration = builder.getIteration().orElse(null);
        this.iterationTimeUnit = builder.getIterationUnit().orElse(null);
        this.delayCount = builder.getDelay().orElse(0);
        this.delayTimeUnit = builder.getDelayUnit().orElse(null);
        this.plugin = plugin;
        builder.getToRunAfter().ifPresent(s -> this.runAfter = s);
    }

    public Optional<Task> getSpongeTask(){
        return Optional.ofNullable(this.task);
    }

    @Override
    public void run() {
        Task.Builder builder = Task.builder().execute(new SScheduler.RunAfterScheduler());
        if(this.delayCount != null){
            if(this.delayTimeUnit == null){
                builder = builder.delayTicks(this.delayCount);
            }else{
                builder = builder.delay(this.delayCount, this.delayTimeUnit);
            }
        }
        if(this.iteration != null){
            if(this.iterationTimeUnit == null){
                builder = builder.intervalTicks(this.iteration);
            }else{
                builder = builder.interval(this.iteration, this.iterationTimeUnit);
            }
        }
        this.task = builder.submit(this.plugin.getLauncher());

    }

    @Override
    public void cancel() {
        getSpongeTask().ifPresent(Task::cancel);
    }

    @Override
    public Runnable getExecutor() {
        return this.taskToRun;
    }
}
