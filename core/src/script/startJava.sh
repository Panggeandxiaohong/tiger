#!/bin/sh
JAVA_HOME="/root/jdk1.7.0_79"


RUNNING_USER=root

APP_HOME=/root/weixin/pangge/target/wechat/WEB-INF
APP_MAINCLASS=online.pangge.HelloOSS
CLASSPATH=$APP_HOME/classes
for i in "$APP_HOME"/lib/*.jar; do
	CLASSPATH="$CLASSPATH":"$i"
done
JAVA_OPTS="-ms512m -mx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m"
psid=0

checkpid(){
	javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAINCLASS`
	if [ -n "$javaps"]; then
		psid=`echo $javaps | awk 'print $1'`
	else
		psid=0
	fi
}

start(){
	checkpid

	if [ $psid -ne 0]; then
		echo "==============================="
		echo "warn:$APP_MAINCLASS alread started ! [pid = $psid]"
		echo "==============================="
	else
		echo -n "starting $APP_MAINCLASS..."
		JAVA_CMO="nohup $JAVA_HOME/bin/java $JAVA_OPT -classpath $CLASSPATH $APP_MAINCLASS $3 > dev/null 2>&1 &"
		su -$RUNNING_USER -c "$JAVA_CMO"
		checkpid
		if [$psid -ne 0]; then
			echo "{pid =$psid}[OK]"
		else
			echo "failed"
		fi
	fi
}
###################################
#(函数)打印系统环境参数
###################################
info() {
   echo "System Information:"
   echo "****************************"
   echo `head -n 1 /etc/issue`
   echo `uname -a`
   echo
   echo "JAVA_HOME=$JAVA_HOME"
   echo `$JAVA_HOME/bin/java -version`
   echo
   echo "APP_HOME=$APP_HOME"
   echo "APP_MAINCLASS=$APP_MAINCLASS"
   echo "****************************"
}
 
###################################
#读取脚本的第一个参数($1)，进行判断
#参数取值范围：{start|stop|restart|status|info}
#如参数不在指定范围之内，则打印帮助信息
###################################
echo $1
start
case "$1" in
   'start')
	info
      start
      ;;
   'stop')
     stop
     ;;
   'restart')
     stop
     start
     ;;
   'status')
     status
     ;;
   'info')
     info
     ;;
) 
