package com.jbhunt.infrastructure.universityhackathon.data.objects.tasks;

import com.jbhunt.infrastructure.universityhackathon.exceptions.TaskSchedulingException;
import lombok.Getter;

import java.util.Calendar;

public class RunOnceTask implements SleeperThreadListener {
    @Getter
    private final String name;
    private final Runnable onRunTask;
    private Runnable onInterruptTask;
    private SleeperThreadRunner sleeperThreadRunner;

    public RunOnceTask(String name, Runnable task) {
        this.name = name;
        this.onRunTask = task;
    }

    public RunOnceTask schedule(Calendar calendar){
        long sleepTime = calculateSleepTime(calendar);
        sleeperThreadRunner = createSleeperThread(sleepTime);
        sleeperThreadRunner.start();

        return this;
    }

    public void interrupt(){
        if(isScheduled()){
            sleeperThreadRunner.interrupt();
        } else {
            throw new IllegalStateException("Task is not scheduled");
        }
    }

    public RunOnceTask onInterrupt(Runnable onInterruptTask){
        if(isScheduled()) throw new IllegalStateException("Cannot add interrupt action for a task that is already scheduled");

        this.onInterruptTask = onInterruptTask;
        return this;
    }

    public boolean isScheduled(){
        return sleeperThreadRunner != null;
    }

    private long calculateSleepTime(Calendar calendar){
        //Make sure that the calendar is in the future (+5ms to account for processing time)
        Calendar earliest = Calendar.getInstance();
        earliest.add(Calendar.MILLISECOND, 5);
        if(calendar.before(earliest)) throw new TaskSchedulingException("Task schedule date is in the past {scheduled for " + calendar.getTime() + "}");

        return calendar.getTimeInMillis() - System.currentTimeMillis();
    }

    private SleeperThreadRunner createSleeperThread(long sleepTime){
        if(isScheduled()) throw new TaskSchedulingException("Task already scheduled");
        return new SleeperThreadRunner(this, sleepTime);
    }

    @Override
    public void onAwake() {
        onRunTask.run();
    }

    @Override
    public void onInterrupt(InterruptedException e) {
        if (onInterruptTask != null) onInterruptTask.run();
    }
}

