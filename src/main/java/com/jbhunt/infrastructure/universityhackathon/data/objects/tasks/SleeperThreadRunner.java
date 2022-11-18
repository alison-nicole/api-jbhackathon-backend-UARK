package com.jbhunt.infrastructure.universityhackathon.data.objects.tasks;

//Manages a thread which sleeps for a specified amount of time, and then notifies the listener when it is complete(or interrupted)
public class SleeperThreadRunner implements Runnable {
    private final SleeperThreadListener listener;
    private final long sleepForMillis;
    private final Thread thread;

    public SleeperThreadRunner(SleeperThreadListener listener, long sleepTime) {
        this.listener = listener;
        this.sleepForMillis = sleepTime;
        this.thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    public void interrupt() {
        thread.interrupt();
    }

    public void run() {
        try {
            Thread.sleep(sleepForMillis);
            listener.onAwake();
        } catch (InterruptedException e) {
            listener.onInterrupt(e);

            //Sonarqube forces the below line to be added since it doesn't know that the listener will handle the interrupt
            thread.interrupt();
        }
    }
}
