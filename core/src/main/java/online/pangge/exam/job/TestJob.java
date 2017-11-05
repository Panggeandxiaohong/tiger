package online.pangge.exam.job;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pangge on 2017/11/5.
 */
@Component("TestJob")
public class TestJob implements Job<String> {
    @Override
    public List dataSource() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        return list;
    }

    @Override
    public void process(String s) {
        System.out.println("test job = = = = = "+s);
    }
}
