package org.core.implementation.sponge.scheduler;

import org.core.schedule.ScheduleManager;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.LinkedTransferQueue;

public class SScheduleManager implements ScheduleManager {

    private final Collection<Scheduler> schedules = new LinkedTransferQueue<>();

    @Override
    public SchedulerBuilder schedule() {
        return new SSchedulerBuilder();
    }

    @Override
    public Collection<Scheduler> getSchedules() {
        return Collections.unmodifiableCollection(this.schedules);
    }

    public void register(@NotNull SScheduler scheduler) {
        this.schedules.add(scheduler);
    }

    public void unregister(@NotNull SScheduler scheduler){
        this.schedules.remove(scheduler);
    }
}
