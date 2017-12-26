package online.pangge.exam.service;

import online.pangge.exam.domain.Subject;

import java.util.List;
import java.util.Map;

public interface EmailService {
    public boolean sendEmailWhitSubjectChange(List<Subject> subject,String change);

    public boolean sendEmailWhitSubjectCount(Map<String,Object> datas);
}
