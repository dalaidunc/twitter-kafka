###### **Dependencies**

This project expects a few things:
    - Kafka installed (I used version kafka_2.12-2.3.1)
    - MarkLogic database running (I used version 10)
    - Twitter app credentials

###### **Usage**

Clone this repository.

In the root folder of this repository (i.e. next to build.gradle, README.md etc.), create a twitter.properties file with your twitter app credentials:

```properties
apiKey=your api key
apiSecret=your api secret
accessToken=your access token
accessTokenSecret=your access token secret
```

Run zookeeper from your Kafka installation:

`bin/zookeeper-server-start.sh config/zookeeper.properties`

Run kafka server from your Kafka installation:

`bin/kafka-server-start.sh config/server.properties`

If you get a lock issue, then empty your data folders for zookeeper and kafka

Clone the marklogic-kafka repository

`https://github.com/BillFarber/kafka-marklogic-connector`

Follow the install instructions for the marklogic-kafka library. I left the topic as the default 'marklogic' - this must match the topic passed to TweetProducer.java from AppInit.java in this project.

Start the marklogic-kafka library in standalone mode (from your Kafka install directory):

`bin/connect-standalone.sh config/marklogic-connect-standalone.properties config/marklogic-sink.properties`

Run the project (AppInit.java). After a few seconds you should start to see tweets arrive and go through the Kafka producer. You can then check your MarkLogic database (Documents database by default) and should see JSON documents of tweets being stored there in real time!

By default we are searching for 'java' and 'javascript' in tweets, but feel free to edit this to whatever you like in AppInit.java