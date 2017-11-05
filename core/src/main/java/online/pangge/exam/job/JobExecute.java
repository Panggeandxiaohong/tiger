package online.pangge.exam.job;

import java.util.List;

/**
 * Created by Pangge on 2017/11/5.
 */
public class JobExecute<T> {
    public void start(Job job){
        List<T> datas =job.dataSource();
        for (T t: datas){
            job.process(t);
        }
    }
}
