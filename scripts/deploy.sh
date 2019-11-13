#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/authorized-keys.pem \
        target/sweater-0.1.1.jar \
        ubuntu@35.158.177.216:/home/ubuntu/

echo 'Restart server...'

ssh -i ~/.ssh/authorized-keys.pem ubuntu@35.158.177.216 << EOF

pgrep java | xargs kill -9
nohup java -jar sweater-0.1.1.jar > log.txt &

EOF

echo 'Bye'