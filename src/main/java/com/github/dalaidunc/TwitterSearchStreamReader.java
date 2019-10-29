package com.github.dalaidunc;

import java.util.concurrent.BlockingQueue;

public class TwitterSearchStreamReader implements Runnable {

    private BlockingQueue<String> msgQueue;

    public TwitterSearchStreamReader(BlockingQueue<String> msgQueue) {
        this.msgQueue = msgQueue;
    }

    @Override
    public void run() {
        try {
            String msg;
            while((msg = this.msgQueue.take()) != null) {
                System.out.println("New message received");
                System.out.println(msg);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
