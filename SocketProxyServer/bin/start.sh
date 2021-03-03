#!/bin/bash
nohup java -jar -Ddock.server.port=  -Dmsg.server.port=   -Dproxy.server.port=  -Dcert.password=    socketProxyServer.jar  >/dev/null 2>&1 & tail -10f ./logs/repeater_server.log