package com.github.dalaidunc;

import com.google.common.collect.Lists;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

class TwitterPropertiesReader {

    private String propertiesPath;
    private Properties twitterProperties;

    public TwitterPropertiesReader(String propertiesFile) {
//        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String rootPath = System.getProperty("user.dir");
        System.out.println("Root path" + rootPath);
        this.propertiesPath = Paths.get(rootPath, propertiesFile).toString();
        this.twitterProperties = new Properties();
    }
    public Properties read() throws IOException {
        System.out.println(this.propertiesPath);
        this.twitterProperties.load(new FileInputStream(this.propertiesPath));
        return this.twitterProperties;
    }
}

public class AppInit {

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        TwitterPropertiesReader propertiesReader = new TwitterPropertiesReader("twitter.properties");
        BlockingQueue<String> msgQueue = new LinkedBlockingDeque<String>(1000);
        List<String> terms = Lists.newArrayList("java", "javascript");
        try {
            Properties twitterProperties = propertiesReader.read();
            System.out.println(twitterProperties.toString());
            Thread twitterStream = new Thread(new TwitterSearchStream(twitterProperties, msgQueue, terms));
            Thread reader = new Thread(new TwitterSearchStreamReader(msgQueue));
            twitterStream.start();
            reader.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
