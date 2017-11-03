package online.pangge.exam.util;

import online.pangge.exam.job.unzipFileThread;
import online.pangge.exam.job.uploadFileThread;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by jie34 on 2017/7/16.
 */
public class UploadFIleUtil {
    private UploadFIleUtil() {
    }

    //org.apache.commons.lang3.concurrent.BasicThreadFactory
    static ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());

    public static void uploadFilesToSubject(File file, OSSUtil ossutil) throws Exception {
        if (!file.isDirectory()) {
            throw new Exception("is not a directory!");
        }
        File[] allFiles = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".zip");
            }
        });
        File srcDir = new File("D:\\unzipSubject");
        File[] srcFiles = srcDir.listFiles();
        for (File f : srcFiles) {
            if (f.exists()) {
                f.delete();
            }
        }
        List<uploadFileThread> allUploadThread = new ArrayList<>();
        List<unzipFileThread> allUnzipThread = new ArrayList<>();
        for (File oneFile : allFiles) {
            unzipFileThread oneThread = new unzipFileThread(oneFile);
            allUnzipThread.add(oneThread);
        }
        List<Future<String>> unzipReturnData = service.invokeAll(allUnzipThread);
        for (Future f:unzipReturnData) {
            System.out.println(f.get().toString());
        }
        srcFiles = srcDir.listFiles();
        for (File oneFile : srcFiles) {
            uploadFileThread oneThread = new uploadFileThread(oneFile, ossutil);
            allUploadThread.add(oneThread);
        }
        List<Future<String>> returnData = service.invokeAll(allUploadThread);
        for (Future f : returnData) {
            System.out.println(f.get().toString());
        }
    }
}
