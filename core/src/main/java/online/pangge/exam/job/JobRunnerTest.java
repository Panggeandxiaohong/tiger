package online.pangge.exam.job;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by Pangge on 2017/11/5.
 */
public class JobRunnerTest {
    public static void main(String[] args) {
        ApplicationContext beanFactory = new FileSystemXmlApplicationContext("classpath:application.xml");
        Job job = (Job) beanFactory.getBean("SendSubjectDetail");
        JobExecute execute = new JobExecute();
        execute.start(job);
    }
}