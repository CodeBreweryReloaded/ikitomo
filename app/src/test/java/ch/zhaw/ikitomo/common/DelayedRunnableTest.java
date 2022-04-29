package ch.zhaw.ikitomo.common;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link DelayedRunnable} class
 */
public class DelayedRunnableTest {
    /**
     * Tests if {@link #DelayedRunnableTest()} constructor throws the correct errors
     */
    @Test
    void testErrors() {
        assertThrows(NullPointerException.class, () -> new DelayedRunnable(null, 0));
        assertThrows(IllegalArgumentException.class, () -> new DelayedRunnable(() -> {
        }, -1));
        assertThrows(IllegalArgumentException.class, () -> new DelayedRunnable(() -> {
        }, -20));
    }

    /**
     * Tests if the task is canceled when {@link DelayedRunnable#run()} is invoked
     * multiple times. For this a sleep is required.
     * 
     * @throws InterruptedException
     */
    @Test
    void testCanceling() throws InterruptedException {
        var latch = new CountDownLatch(1);
        long start = System.currentTimeMillis();
        var runnable = new DelayedRunnable(() -> {
            long duration = System.currentTimeMillis() - start;
            assertTrue(duration >= 800, "Timer was not reset (duration: " + duration + ")");
            latch.countDown();
        }, 500);
        runnable.run();
        sleep(300);
        runnable.run();
        // make sure that the run method is called eventually or else an exception would
        // be thrown
        assertTrue(latch.await(1, TimeUnit.SECONDS), "run was not invoked");
    }

    /**
     * Tests if the {@link DelayedRunnable} can be used multiple times
     * 
     * @throws InterruptedException
     */
    @Test
    void testRepeatedCalling() throws InterruptedException {
        var latch1 = new CountDownLatch(1); // reaches zero after the first call
        var latch2 = new CountDownLatch(2); // reaches zero after the second call

        var runnable = new DelayedRunnable(() -> {
            latch1.countDown();
            latch2.countDown();
        }, 100);

        long start = System.currentTimeMillis();
        runnable.run();
        assertTrue(latch1.await(150, TimeUnit.MILLISECONDS), "run was not invoked");
        assertTrue(System.currentTimeMillis() - start >= 100, "Timer was too fast");

        // second call
        start = System.currentTimeMillis();
        runnable.run();
        assertTrue(latch2.await(150, TimeUnit.MILLISECONDS), "run was not invoked");
        assertTrue(System.currentTimeMillis() - start >= 100, "Timer was too fast");
    }

    /**
     * An internal method to sleep for a given amount of time. The method makes sure
     * that the current thread doesn't wake up before the time is up
     * 
     * @param timeout the timeout
     * @throws InterruptedException
     */
    private static void sleep(long timeout) throws InterruptedException {
        long start = System.currentTimeMillis();
        long timeElapsed = 0;
        while ((timeElapsed = System.currentTimeMillis() - start) < timeout) {
            Thread.sleep(timeout - timeElapsed);
        }
    }
}
