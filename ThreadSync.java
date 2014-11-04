/**
 * CS350
 * Project #4
 * Austin Schey
 * ThreadSync: synchronizes threads to print out characters
 * in a certain order
 */
import java.lang.Thread;
import java.util.concurrent.*;

public class ThreadSync
{

    private static boolean runFlag = true;

    private static Semaphore cSemaphore = new Semaphore(1);
    private static Semaphore dSemaphore = new Semaphore(0);
    private static Semaphore pSemaphore = new Semaphore(0);

    public static void main( String[] args ) {

        // create and start each runnable
        Runnable task1 = new TaskPrintC();
        Runnable task2 = new TaskPrintD();
        Runnable task3 = new TaskPrintP();

        Thread thread1 = new Thread( task1 );
        Thread thread2 = new Thread( task2 );
        Thread thread3 = new Thread( task3 );

        thread1.start();
        thread2.start();
        thread3.start();

        // Let them run for 500ms
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // put up the stop sign
        runFlag=false;

        thread3.interrupt();
        thread2.interrupt();
        thread1.interrupt();
    }

    public static class TaskPrintC implements Runnable
    {
        public void run(){
            while (runFlag) {
                try {
                    cSemaphore.acquire();
                    System.out.printf( "%s\n", ":");
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if (cSemaphore.availablePermits() == 0) {
                    dSemaphore.release(3);
                }
            }
        }
    }

    public static class TaskPrintD implements Runnable
    {
        public void run(){
            while (runFlag) {
                try {
                    dSemaphore.acquire();
                    System.out.printf( "%s\n", "-");
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if (dSemaphore.availablePermits() == 0) {
                    pSemaphore.release(5);
                }
            }
        }
    }

    public static class TaskPrintP implements Runnable
    {
        public void run(){
            while (runFlag) {
                try {
                    pSemaphore.acquire();
                    System.out.printf( "%s\n", ")");
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if (pSemaphore.availablePermits() == 0) {
                    cSemaphore.release();
                }
            }
        }
    }
}