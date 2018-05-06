package online.pangge.exam.job;

import online.pangge.exam.util.OSSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component("UploadFileJob")
public class UploadFileJob implements Job<File> {

    private static Logger log = LoggerFactory.getLogger(UploadFileJob.class);
    private static String descPath = "D:\\unzipSubject";

    @Autowired
    private OSSUtil ossUtil;

    @Override
    public List<File> dataSource() {
        File dir = new File(descPath);
        List<File> allUploadFiles = new ArrayList<>();
        if(dir.isDirectory()){
            for(File f : dir.listFiles()){
                allUploadFiles.add(f);
            }
        }
        return allUploadFiles;
    }

    @Override
    public void process(File s) {
        ExecutorService exec= Executors.newFixedThreadPool(8);
        List<Future<String>> results=new ArrayList<Future<String>>();
        results.add(exec.submit(new uploadFileThread(s,ossUtil)));
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
