package com.github.dalaidunc;

import java.util.concurrent.BlockingQueue;

public class TwitterSearchStreamReader implements Runnable {

    private BlockingQueue<String> msgQueue;
    private TweetProducer tweetProducer;

    public TwitterSearchStreamReader(BlockingQueue<String> msgQueue, TweetProducer tweetProducer) {
        this.msgQueue = msgQueue;
        this.tweetProducer = tweetProducer;
    }

    @Override
    public void run() {
        try {
            String msg;
            while((msg = this.msgQueue.take()) != null) {
                System.out.println("New message received");
                System.out.println(msg);
                System.out.println("Sending tweet through Kafka producer");
                this.tweetProducer.produceTweet(msg);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
