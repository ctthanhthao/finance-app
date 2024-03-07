package com.home.financial.app;

public class JavaTester extends Thread {
    static Object lock = new Object();
    static int n;
    int i;
    String name;
    JavaTester(String name, int i) {
        this.name = name;
        this.i = i;
    }
    @Override
    public void run() {
        try {
            System.out.println("n = " + n);
            synchronized (lock) {
                while (i != n) {
                    System.out.println(name + " wait");
                    lock.wait();
                }
                System.out.println(name + " started");
                System.out.println("n = " + n);
                System.out.println("i = " + i);
                n++;
                lock.notifyAll();
            }
            synchronized (lock) {
                while (i != n - 4) {
                    lock.wait();
                }
                System.out.println(name + " finished");
                n++;
                lock.notifyAll();
            }
        } catch (InterruptedException e) {
        }
    }
    public static void main(String[] args) throws Exception {
        new JavaTester("a", 0).start();
        new JavaTester("b", 1).start();
        new JavaTester("c", 2).start();
        new JavaTester("d", 3).start();
    }
}
