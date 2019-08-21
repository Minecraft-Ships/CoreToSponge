package org.ships.implementation.sponge.scheduler;

import org.core.platform.Plugin;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SSchedulerBuilder implements SchedulerBuilder {

    protected Integer delay;
    protected TimeUnit delayUnit;
    protected Integer iteration;
    protected TimeUnit iterationUnit;
    protected Runnable executor;
    protected Scheduler runAfter;

    @Override
    public Optional<Integer> getDelay() {
        return Optional.ofNullable(this.delay);
    }

    @Override
    public SchedulerBuilder setDelay(Integer value) {
        this.delay = value;
        return this;
    }

    @Override
    public Optional<TimeUnit> getDelayUnit() {
        return Optional.ofNullable(this.delayUnit);
    }

    @Override
    public SchedulerBuilder setDelayUnit(TimeUnit unit) {
        this.delayUnit = unit;
        return this;
    }

    @Override
    public Optional<Integer> getIteration() {
        return Optional.ofNullable(this.iteration);
    }

    @Override
    public SchedulerBuilder setIteration(Integer value) {
        this.iteration = value;
        return this;
    }

    @Override
    public Optional<TimeUnit> getIterationUnit() {
        return Optional.ofNullable(this.iterationUnit);
    }

    @Override
    public SchedulerBuilder setIterationUnit(TimeUnit unit) {
        this.iterationUnit = unit;
        return this;
    }

    @Override
    public SchedulerBuilder setExecutor(Runnable runnable) {
        this.executor = runnable;
        return this;
    }

    @Override
    public Runnable getExecutor() {
        return this.executor;
    }

    @Override
    public SchedulerBuilder setToRunAfter(Scheduler scheduler) {
        this.runAfter = scheduler;
        return this;
    }

    @Override
    public Optional<Scheduler> getToRunAfter() {
        return Optional.ofNullable(this.runAfter);
    }

    @Override
    public Scheduler build(Plugin plugin) {
        if(this.executor == null){
            System.err.println("SchedulerBuilder was attempted to be built but no executor was set");
            new IOException("No Executor in build").printStackTrace();
        }
        return new SScheduler(this, plugin);
    }
}
