package online.pangge.exam.job;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by Pangge on 2017/11/5.
 */
public class JobRunner {
    public static void main(String[] args) {
        ApplicationContext beanFactory = new FileSystemXmlApplicationContext("classpath:application-mvc.xml");
        Job job = (Job) beanFactory.getBean(args[0]);
        JobExecute execute = new JobExecute();
        execute.start(job);
    }
}
