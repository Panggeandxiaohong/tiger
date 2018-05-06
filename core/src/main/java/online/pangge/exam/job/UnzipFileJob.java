package online.pangge.exam.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component("UnzipFileJob")
public class UnzipFileJob implements Job<File> {

    private static Logger log = LoggerFactory.getLogger(UnzipFileJob.class);
    private static String descPath = "G:\\ideaworkspace\\weixin\\mgrsite\\src\\main\\webapp\\upload";

    @Override
    public List<File> dataSource() {
        File dir = new File(descPath);
        List<File> allUnzipFiles = new ArrayList<>();
        if(dir.isDirectory()){
            for(File f : dir.listFiles()){
                allUnzipFiles.add(f);
            }
        }
        return allUnzipFiles;
    }

    @Override
    public void process(File s) {
        ExecutorService exec= Executors.newFixedThreadPool(8);
        List<Future<String>> results=new ArrayList<Future<String>>();
        results.add(exec.submit(new unzipFileThread(s)));
        for(Future<String> f : results){
            try {
                System.out.println(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        exec.shutdownNow();
    }
}
