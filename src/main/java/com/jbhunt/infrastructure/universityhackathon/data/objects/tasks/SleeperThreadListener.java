package com.jbhunt.infrastructure.universityhackathon.data.objects.tasks;

public interface SleeperThreadListener {
    void onAwake();
    void onInterrupt(InterruptedException e);
}
