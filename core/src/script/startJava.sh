#!/bin/sh
#java所在目录
JAVA_HOME="/root/jdk1.7.0_45"
#执行的角色，一般不推荐使用root
RUNNING_USER=root
#class文件所在目录的上一级
APP_HOME=/root/weixin/tiger/website/target/website/WEB-INF
#job入口类带全限定名的名字，例如online.pangge.exam.job.JobRunner
APP_MAINCLASS=$1
CLASSPATH=$APP_HOME/classes
#复制jar包一定要在循环前面，否则复制的jar包无法加载
cp /root/jars/* /root/weixin/tiger/website/target/website/WEB-INF/lib/
#拼接classpath变量，这里会逐个jar包都拼接进去
for i in "$APP_HOME"/lib/*.jar; do
	CLASSPATH="$CLASSPATH":"$i"
done
#运行时的参数，可以不提供，按照默认的参数运行，如果不提供注意下面JAVA_CMO也应该去掉$JAVA_OPTS参数
JAVA_OPTS="-ms512m -mx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m"
#需要执行的job名称，不需要全限定名，因为是通过Spring获取的，知道名字即可
PARAM=$2
#拼接执行的参数，>/root/sh/$PARAM-`date +%Y-%m-%d-%H%M%S`.log 2>&1 & 是重定向程序输出到/root/sh目录下，对应的job名称的.log文件中
JAVA_CMO="nohup $JAVA_HOME/bin/java $JAVA_OPT -classpath $CLASSPATH $APP_MAINCLASS $PARAM >/root/sh/$PARAM-`date +%Y-%m-%d-%H%M%S`.log 2>&1 &"
#执行的命令,注意- $RUNNING_USER中间是有空格的
su - $RUNNING_USER -c "$JAVA_CMO"




#su -root -c "nohup /root/jdk1.7.0_45/bin/java /root/weixin/tiger/core/target/classes/online/pangge/exam/job/JobRunnerTest  >/root/sh/s-`date +%Y-%m-%d-%H%M%S`.log 2>&1 &"