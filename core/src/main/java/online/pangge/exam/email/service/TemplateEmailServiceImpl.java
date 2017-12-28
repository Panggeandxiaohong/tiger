package online.pangge.exam.email.service;

import freemarker.template.Template;
import online.pangge.exam.email.domain.Email;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.util.Date;
import java.util.Map;
@Component
public class TemplateEmailServiceImpl {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    private boolean validateEmail(Email email, String templateName){
        if(StringUtils.isEmpty(email.getTo())||StringUtils.isEmpty(email.getFrom())){
            return false;
        }
        return true;
    }
    public boolean sendEmail(Email email,String templateName,Map<String,Object> model){
        if(!validateEmail(email, templateName)){
            return false;
        }
        try{
            MimeMessage message = mailSender.createMimeMessage();
//            message.addHeader("Return-path","");
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(email.getFrom());
            helper.setTo(email.getTo().split(","));
            if(!StringUtils.isEmpty(email.getCc())){
                helper.setCc(email.getCc().split(","));
            }
            if(!StringUtils.isEmpty(email.getBcc())){
                helper.setBcc(email.getBcc().split(","));
            }
            helper.setSentDate(new Date());
            helper.setSubject(email.getSubject());
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
            helper.setText(htmlTemplate,true);
            if(email.getParamMap()!=null&&!email.getParamMap().isEmpty()){
                for(Map.Entry<String,Object> header : email.getParamMap().entrySet()){
                    message.addHeader(header.getKey(),header.getValue().toString());
                }
            }
            if(email.getAttchFileName()!=null&&email.getAttchFileName().length>0){
                for(String fileName : email.getAttchFileName()){
                    File file = new File(fileName);
                    helper.addAttachment(MimeUtility.encodeWord(file.getName()),file);
                }
            }
            System.out.println("begin send ...");
            System.out.println("use template..."+templateName);
            mailSender.send(message);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("end send ...");
        return true;
    }
}
