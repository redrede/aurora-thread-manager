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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcelo Canhamero <marcelo at redrede.net>
 */
public class TaskMonitor implements Runnable {

    private static final Logger LOG = Logger.getLogger(TaskMonitor.class.getName());

    private static final TaskMonitor INSTANCE = new TaskMonitor();

    private final BlockingQueue<Task> RUNNING_TASKS = new DelayQueue<>();
    private final AtomicLong UNIQUE_KEYS = new AtomicLong();
    private static ExecutorService executor;

    private static Boolean started;
    private static Thread worker;

    private volatile boolean interrupt = false;

    private TaskMonitor() {
        started = false;
    }
    
    public void stopMonitor(){
        interrupt = true;
        worker.interrupt();
        executor.shutdown();
    }

    private static void startThread() {
        executor = Executors.newSingleThreadExecutor();
        executor.execute(INSTANCE);
        started = true;
        LOG.log(Level.INFO, "start thread monitor");
    }

    public static TaskMonitor getInstance() {
        if (!started) {
            synchronized (started) {
                startThread();
            }
        }
        return INSTANCE;
    }

    public void startTask(Task task) {
        RUNNING_TASKS.add(task);
    }

    public void endTask(Task task) {
        RUNNING_TASKS.remove(task);
    }

    public Task timeoutTask() throws InterruptedException {
        return RUNNING_TASKS.take();
    }

    public long generateIdentifier() {
        return UNIQUE_KEYS.getAndIncrement();
    }

    @Override
    public void run() {       
        worker = Thread.currentThread();
        while (!interrupt) {
            try {
                Task taskTimeout = timeoutTask();
                taskTimeout.interrupt();
            } catch (InterruptedException ex) {
                interrupt = true;
                LOG.log(Level.INFO, "Task monitor interrupt: {0}", ex.getMessage());
            }
        }
    }

}
