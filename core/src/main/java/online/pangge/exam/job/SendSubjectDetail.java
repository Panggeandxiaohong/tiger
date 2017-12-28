package online.pangge.exam.job;

import online.pangge.exam.domain.Subject;
import online.pangge.exam.service.EmailService;
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
    private EmailService emailService;

    @Override
    public List<Map<String, Object>> dataSource() {
        List<Map<String, Object>> datas = Collections.EMPTY_LIST;
        List<Subject> allSubject = subjectService.selectAll();
        List<Subject> choise = Collections.EMPTY_LIST;
        List<Subject> empty = Collections.EMPTY_LIST;
        List<Subject> judge = Collections.EMPTY_LIST;
        Map<String, Object> data = new HashMap<>();
        for (Subject s : allSubject) {
            switch (s.getSubjectType().getTypeName()) {
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
        System.out.println(data.toString());
        log.info(data.toString());
        return datas;
    }

    @Override
    public void process(Map<String, Object> s) {
        System.out.println("this is SendSubjectDetail");
        emailService.sendEmailWhitSubjectCount(s);
    }
}
