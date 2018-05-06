package online.pangge.exam.service;

import online.pangge.exam.domain.Subject;

import java.util.List;

/**
 * exam service
 */
public interface IExamService {
    public String endExam(String fromUserName);

    public void addAnswer(String fromUserName,String answer);

    public String getNewsMessageXML(String fromUserName, String toUserName, String subjectString);

    public int Correcting(List<Subject> subjects, String fromusername);
}
