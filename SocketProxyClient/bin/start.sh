#!/bin/bash
nohup java -jar -Dserver.ip=  -Dtarget.ip=   -Dtarget.port=  -Ddock.port= -Dmsg.port=  -Dcert.password=    socketProxyClient.jar  >/dev/null 2>&1 & tail -10f ./logs/repeater_client.log