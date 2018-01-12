package online.pangge.exam.job;

import online.pangge.exam.domain.Subject;
import online.pangge.exam.service.IEmailService;
import online.pangge.exam.service.ISubjectService;
import online.pangge.exam.util.ExamContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("SendSubjectDetail")
public class SendSubjectDetail implements Job<Map<String, Object>> {

    private static Logger log = LoggerFactory.getLogger(online.pangge.exam.job.SendSubjectDetail.class);

    @Autowired
    private ISubjectService subjectService;

    @Autowired
    private IEmailService emailService;

    @Override
    public List<Map<String, Object>> dataSource() {
        List<Map<String, Object>> datas = new ArrayList<>();
        List<Subject> allSubject = subjectService.selectAll();
        List<Subject> choise = new ArrayList<>();
        List<Subject> empty = new ArrayList<>();
        List<Subject> judge = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        for (Subject s : allSubject) {
            switch (s.getSubjectType().getTypeCode()) {
                case ExamContext.EXAM_CHOICE:
                    choise.add(s);
                    break;
                case ExamContext.EXAM_EMPTY:
                    empty.add(s);
                    break;
                case ExamContext.EXAM_JUDGE:
                    judge.add(s);
                    break;
            }
        }
        data.put("allSubject", allSubject.size());
        data.put("choiseSubject", choise.size());
        data.put("emptySbject", empty.size());
        data.put("judgeSbject", judge.size());
        System.out.println("datasources---------"+data.toString());
        log.info(data.toString());
        datas.add(data);
        return datas;
    }

    @Override
    public void process(Map<String, Object> s) {
        System.out.println("this is SendSubjectDetail");
        emailService.sendEmailWhitSubjectCount(s);
    }
}
