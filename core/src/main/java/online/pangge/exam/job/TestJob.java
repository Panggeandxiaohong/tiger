package online.pangge.exam.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pangge on 2017/11/5.
 */
@Component("TestJob")
public class TestJob implements Job<String> {
    private static Logger log = LoggerFactory.getLogger(TestJob.class);
    @Override
    public List dataSource() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        log.info("add element to list");
        return list;
    }

    @Override
    public void process(String s) {
        log.info("do process");
        System.out.println("test job = = = = =,Time =  "+new Date());
    }
}
