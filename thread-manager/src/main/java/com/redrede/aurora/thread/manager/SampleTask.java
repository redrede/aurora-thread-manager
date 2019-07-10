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

/**
 *
 * @author Marcelo Canhamero <marcelo at redrede.net>
 */
public class SampleTask extends Task {

    public SampleTask() {
        super("Sample", new TaskContext(), 10l);
    }

    @Override
    public void execute() throws InterruptedException {
        long o = 0;        
        while(!this.isInterrupt() && o < 1_000_000_000){
            o+=1*(1/1);      
            if (this.isInterrupt()){
                System.out.println(this.getId() + " isInterrupt + " + o + " interr: "+  this.isInterrupt());
            }
        }
        if (!this.isInterrupt()){        
            System.out.println(this.getId() + " done + " + o + " interr: "+  this.isInterrupt());
        }
        
        
    }

}
