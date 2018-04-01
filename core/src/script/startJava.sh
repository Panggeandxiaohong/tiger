#!/bin/sh
JAVA_HOME="/root/jdk1.7.0_79"
RUNNING_USER=root
APP_HOME=/root/weixin/tiger/website/target/website/WEB-INF
APP_MAINCLASS=$2
CLASSPATH=$APP_HOME/classes
for i in "$APP_HOME"/lib/*.jar; do
	CLASSPATH="$CLASSPATH":"$i"
done
JAVA_OPTS="-ms512m -mx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m"
echo -n "starting $APP_MAINCLASS..."
$PARAM=$3
JAVA_CMO="nohup $JAVA_HOME/bin/java $JAVA_OPT -classpath $CLASSPATH $APP_MAINCLASS $3 >/root/sh/$PARAM-`date +%Y-%m-%d-%H%M%S`.log 2>&1 &"
su -$RUNNING_USER -c "$JAVA_CMO"


su -$RUNNING_USER -c "nohup /root/jdk1.7.0_45/bin/java -ms512m -mx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m  $APP_MAINCLASS  >/root/sh/s-`date +%Y-%m-%d-%H%M%S`.log 2>&1 &"