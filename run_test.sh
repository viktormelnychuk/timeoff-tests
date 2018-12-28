#!/usr/bin/bash
cd application/
echo "Looking for app that is running"
INUSEPID="$(netstat -a -n -o | grep 0.0.0.0:3000 | awk '{print $5}')"
taskkill //F //PID $INUSEPID
echo "Starting application under test"
npm start > startuplog.txt 2> stderr.txt &
echo "Sleeping 10s to give app time to start"
sleep 10s
OUT="$(ps -ef | grep node | awk '{print $2}')"
echo "Node pid = $OUT"
cd ../tests
gradle test
echo "Finished gradle tests"
KILL_OUT="$(kill -9 $OUT)"
echo "Kill out: $KILL_OUT"