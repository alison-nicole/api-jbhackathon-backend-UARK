package com.jbhunt.infrastructure.universityhackathon.data.objects.tasks;

import org.awaitility.Awaitility;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(MockitoJUnitRunner.class)
public class RunOnceTaskTest {
    @Test
    public void testRunTask(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, 50);

        AtomicBoolean ran = new AtomicBoolean(false);

        RunOnceTask task = new RunOnceTask("test", () -> ran.set(true))
                .onInterrupt(Assert::fail)
                .schedule(calendar);

        Awaitility.await()
                .atMost(100, java.util.concurrent.TimeUnit.MILLISECONDS)
                .pollDelay(50, java.util.concurrent.TimeUnit.MILLISECONDS)
                .until(ran::get);

        Assert.assertTrue(ran.get());
        Assert.assertEquals("test", task.getName());
    }

    @Test
    public void testInterruptTaskDoNothing(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, 50);

        RunOnceTask task = new RunOnceTask("test", Assert::fail)
                .schedule(calendar);

        task.interrupt();

        Awaitility.await()
                .atMost(100, java.util.concurrent.TimeUnit.MILLISECONDS)
                .pollDelay(50, java.util.concurrent.TimeUnit.MILLISECONDS)
                .until(() -> true);
    }

    @Test
    public void testInterruptTaskDoSomething(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, 50);

        AtomicBoolean interrupted = new AtomicBoolean(false);

        RunOnceTask task = new RunOnceTask("test", Assert::fail)
                .onInterrupt(() -> interrupted.set(true))
                .schedule(calendar);

        task.interrupt();

        Awaitility.await()
                .atMost(100, java.util.concurrent.TimeUnit.MILLISECONDS)
                .pollDelay(50, java.util.concurrent.TimeUnit.MILLISECONDS)
                .until(interrupted::get);

        Assert.assertTrue(interrupted.get());
    }

    @Test(expected = RuntimeException.class)
    public void testScheduleTwice(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, 100);

        RunOnceTask task = new RunOnceTask("test", () -> {}).schedule(calendar);
        task.schedule(calendar);
    }

    @Test(expected = IllegalStateException.class)
    public void testInterruptNotScheduled(){
        new RunOnceTask("test", () -> {}).interrupt();
    }

    @Test(expected = RuntimeException.class)
    public void testScheduleInPast(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, -100);

        new RunOnceTask("test", () -> {}).schedule(calendar);
    }

    @Test(expected = RuntimeException.class)
    public void testScheduleAfterInterrupt(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, 50);

        new RunOnceTask("test", () -> {})
                .schedule(calendar)
                .onInterrupt(() -> {});
    }
}
