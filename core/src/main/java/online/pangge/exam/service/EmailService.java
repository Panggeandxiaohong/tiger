package online.pangge.exam.service;

import online.pangge.exam.domain.Subject;

import java.util.List;

public interface EmailService {
    public boolean sendEmailWhitSubjectChange(List<Subject> subject,String change);
}
