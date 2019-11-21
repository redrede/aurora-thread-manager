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
package com.redrede.aurora.thread.manager.test;

import com.redrede.aurora.thread.manager.SampleTask;
import com.redrede.aurora.thread.manager.TaskMonitor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcelo Canhamero <marcelo at redrede.net>
 */
public class NewClass {

    public static void main(String[] args) {
        try {
            //try {
            ExecutorService executor = Executors.newFixedThreadPool(10);
            for (int i = 1; i < 10; i++) {
                executor.execute(new SampleTask());
            }
            String name = Thread.currentThread().getName();
            Thread.sleep(11000l);
            System.out.println("Done: " + name);
            TaskMonitor.getInstance().stopMonitor();            
            //executor.awaitTermination(1, TimeUnit.SECONDS);
            executor.shutdown();
            //} catch (InterruptedException ex) {
            //    Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
            //}
        } catch (InterruptedException ex) {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
