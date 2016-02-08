#!/bin/bash          

ssh -i /Users/hochi/.keys/hochreiner.pem ubuntu@128.130.172.225 'redis-cli flushall'
rm /Users/hochi/vispDB.mv.db
rm /Users/hochi/vispDB.mv.trace.db

mvn spring-boot:run
