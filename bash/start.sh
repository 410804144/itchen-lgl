#!/bin/bash

# 目标目录
COPY_TO=/usr/local/share/applications/itchen-lgl-api/

# jenkins打包目录
COPY_FROM=/var/lib/jenkins/workspace/itchen-lgl/itchen-api/target/

# jar文件名
JAR_NAME=itchen-lgl-api-1.0-SNAPSHOT.jar

# 日志文件名
LOG_NAME=spring.log

echo "退出程序"
curl -X POST http://localhost:10000/actuator/shutdown
sleep 5

# 查看程序是否退出
LOOP_TIMES=10
while [ ${LOOP_TIMES} -gt 0 ];
do
  PIDS=`ps -ef | grep java | grep ${JAR_NAME} | awk '{print $2}'`
  if [ -n "${PIDS}" ];
  then
    echo "进程仍然存在，Kill掉进程 ${PIDS}"
    for PID in ${PIDS};
    do
      kill $PID > /dev/null 2>&1
      sleep 5
    done
  else
    break
  fi
  LOOP_TIMES=`expr $LOOP_TIMES - 1`
done

# 进程无法退出，强制退出
PIDS=`ps -ef | grep java | grep ${JAR_NAME} | awk '{print $2}'`
if [ -n "${PIDS}" ];
then
  echo "进程仍然存在，强制Kill掉进程 ${PIDS}"
  for PID in ${PIDS};
  do
    kill -9 $PID > /dev/null 2>&1
    sleep 5
  done
fi

echo "复制jar包"
rm -f ${COPY_TO}${JAR_NAME}
sleep 2
cp ${COPY_FROM}${JAR_NAME} ${COPY_TO}${JAR_NAME}
sleep 2

echo "启动新进程"
nohup java -jar -Xms256m -Xmx512m ${COPY_TO}${JAR_NAME} > /dev/null 2>&1 &

# 检测进程是否启动成功
LOOP_TIMES=10
while [ ${LOOP_TIMES} -gt 0 ];
do
  PIDS=`ps -ef | grep java | grep ${JAR_NAME} | awk '{print $2}'`
  if [ -n "${PIDS}" ];
  then
   break
  else
   echo "进程启动中..."
   sleep 1
  fi
  LOOP_TIMES=`expr $LOOP_TIMES - 1`
done

# 最后确认进程是否启动
sleep 10
PIDS=`ps -ef | grep java | grep ${JAR_NAME} | awk '{print $2}'`
if [ -n "${PIDS}" ];
then
  echo "进程启动成功"
  tail -n 150 ${COPY_TO}${LOG_NAME}
else
  echo "进程启动失败"
  tail -n 150 ${COPY_TO}${LOG_NAME}
  exit 1
fi