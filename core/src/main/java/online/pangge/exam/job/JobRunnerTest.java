package online.pangge.exam.job;

/**
 * Created by Pangge on 2017/11/5.
 */
public class JobRunnerTest {
    public static void main(String[] args) {
        System.out.println("start job ...");
        for(String s : args){
            System.out.println(s);
        }
    }
}