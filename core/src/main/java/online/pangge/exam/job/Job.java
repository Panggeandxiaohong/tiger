package online.pangge.exam.job;


import java.util.List;

/**
 * Created by Pangge on 2017/11/5.
 */
public interface Job<T> {
    /**
     * get data
     * @return
     */
    List<T> dataSource();

    /**
     * execute job
     * @param t
     */
    void process(T t);
}
