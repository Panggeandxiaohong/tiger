package online.pangge.exam.service.impl;

import online.pangge.exam.domain.Subject;
import online.pangge.exam.email.domain.Email;
import online.pangge.exam.email.service.TemplateEmailServiceImpl;
import online.pangge.exam.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private TemplateEmailServiceImpl emailSender;

    @Override
    public boolean sendEmailWhitSubjectChange(List<Subject> subject,String change) {
        Email email = new Email();
        Map<String,Object> emailData = new HashMap<>();
        email.setFrom("m13427598892@163.com");
        email.setTo("346264873@qq.com");
        email.setSubject("Subject change alert!");
        emailData.put("change",change);
        emailData.put("subjects",subject);
        System.out.println("send email ....");
        return emailSender.sendEmail(email,"subjectTemplate.html",emailData);
    }

    @Override
    public boolean sendEmailWhitSubjectCount(Map<String,Object> datas) {
        Email email = new Email();
        email.setFrom("m13427598892@163.com");
        email.setTo("346264873@qq.com");
        email.setSubject("subject count!");
        System.out.println("send email ....");
        return emailSender.sendEmail(email,"subjectCount.html",datas);
    }
}
