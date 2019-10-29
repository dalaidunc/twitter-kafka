package com.github.dalaidunc;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterSearchStream implements Runnable {

    public BlockingQueue<String> msgQueue;
    public BlockingQueue<Event> eventQueue;
    private StatusesFilterEndpoint hosebirdEndpoint;
    private Authentication hosebirdAuth;
    private Hosts hosebirdHosts;
    private Client hosebirdClient;
    private List<String> terms;

    public TwitterSearchStream(Properties authProps, BlockingQueue<String> msgQueue, List<String> terms) {
        /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        this.msgQueue = msgQueue;
//        this.msgQueue = new LinkedBlockingQueue<String>(100000);
        this.eventQueue = new LinkedBlockingQueue<Event>(1000);

        this.terms = terms;

/** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        this.hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        this.hosebirdEndpoint = new StatusesFilterEndpoint();
        hosebirdEndpoint.trackTerms(this.terms);

// These secrets should be read from a config file
        System.out.println(authProps.toString());
        this.hosebirdAuth = new OAuth1(authProps.getProperty("apiKey"),
                authProps.getProperty("apiSecret"),
                authProps.getProperty("accessToken"),
                authProps.getProperty("accessTokenSecret"));
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
    }

    public void run() {

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(this.hosebirdHosts)
                .authentication(this.hosebirdAuth)
                .endpoint(this.hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(this.msgQueue))
                .eventMessageQueue(this.eventQueue);                          // optional: use this if you want to process client events

        this.hosebirdClient = builder.build();
        // Attempts to establish a connection.
        this.hosebirdClient.connect();
    }

    public void stop() {
        this.hosebirdClient.stop();
        this.hosebirdClient = null;
    }

}
