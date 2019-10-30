docker run -d -v $('pwd')/datahub-fs:/datahub-fs --name=twitter-kafka -p 7997-8003:7997-8003 -p 8010-8014:8010-8014 -p 8120-8124:8120-8124 -p 8040-8041:8040-8041 -p 8063:8063 marklogic:10.0-1
