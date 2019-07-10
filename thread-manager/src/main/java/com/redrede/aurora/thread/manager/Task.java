/*
 * Copyright (C) 2019 Marcelo Canhamero <marcelo at redrede.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redrede.aurora.thread.manager;

import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Marcelo Canhamero <marcelo at redrede.net>
 */
public abstract class Task implements Runnable, Delayed {

    private final Long id;

    private final String name;

    private final Long maximumDuration;

    private Long finishTime;
    
    private Long startTime;

    private Thread worker;

    private boolean interrupt = false;

    public Long getDuration() {
        return System.currentTimeMillis() - startTime;
    }

    private final TaskContext context;

    public Task(String name, TaskContext context) {
        this.id = TaskMonitor.getInstance().generateIdentifier();
        this.name = name;
        this.context = context;
        this.maximumDuration = Long.MAX_VALUE;
    }

    public Task(String name, TaskContext context, Long maximumDuration) {
        this.id = TaskMonitor.getInstance().generateIdentifier();
        this.name = name;
        this.context = context;
        this.maximumDuration = maximumDuration;
    }

    @Override
    public void run() {
        startTask();
        try {
            this.execute();
        } catch (InterruptedException ex) {
            System.err.println("Err: " + ex.getMessage() + " id: "+ this.getId());
        } finally {
            endTask();
        }
    }

    private void startTask() {
        startTime = System.currentTimeMillis();
        worker = Thread.currentThread();
        this.finishTime = startTime + maximumDuration;
        TaskMonitor.getInstance().startTask(this);
    }

    protected void interrupt() {
        this.interrupt = true;
        this.worker.interrupt();
        System.out.println("Interrupt " + this.getName() + " id: " + this.getId() + " thread: " + this.worker.getName() + " duration:" + this.getDuration() );
    }

    private void endTask() {
        TaskMonitor.getInstance().endTask(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TaskContext getContext() {
        return context;
    }

    public Long getMaximumDuration() {
        return maximumDuration;
    }

    public abstract void execute() throws InterruptedException;

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = finishTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed task) {
        return (int) (this.finishTime - ((Task) task).getDuration());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    public boolean isInterrupt() {
        return interrupt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Task other = (Task) obj;
        return Objects.equals(this.id, other.id);
    }

}
