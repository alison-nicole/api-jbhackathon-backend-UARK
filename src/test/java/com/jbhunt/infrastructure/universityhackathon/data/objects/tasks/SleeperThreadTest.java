package com.jbhunt.infrastructure.universityhackathon.data.objects.tasks;

import lombok.Getter;
import org.awaitility.Awaitility;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class SleeperThreadTest {
    class TestSleeperThreadListener implements SleeperThreadListener {
        @Getter
        private boolean ran = false;
        @Getter
        private boolean interrupted = false;

        @Override
        public void onAwake() {
            ran = true;
        }

        @Override
        public void onInterrupt(InterruptedException e) {
            interrupted = true;
        }
    }

    @Test
    public void testSleeperThreadRun(){
        TestSleeperThreadListener listener = new TestSleeperThreadListener();
        SleeperThreadRunner sleeperThreadRunner = new SleeperThreadRunner(listener, 50);
        sleeperThreadRunner.start();

        Awaitility.await()
                .atMost(100, TimeUnit.MILLISECONDS)
                .pollDelay(50, TimeUnit.MILLISECONDS)
                .until(listener::isRan);

        Assert.assertTrue(listener.isRan());
        Assert.assertFalse(listener.isInterrupted());
    }

    @Test
    public void testSleeperThreadInterrupt(){
        TestSleeperThreadListener listener = new TestSleeperThreadListener();
        SleeperThreadRunner sleeperThreadRunner = new SleeperThreadRunner(listener, 50);

        sleeperThreadRunner.start();
        sleeperThreadRunner.interrupt();

        Awaitility.await()
                .atMost(100, TimeUnit.MILLISECONDS)
                .pollDelay(50, TimeUnit.MILLISECONDS)
                .until(listener::isInterrupted);

        Assert.assertFalse(listener.isRan());
        Assert.assertTrue(listener.isInterrupted());
    }
}
