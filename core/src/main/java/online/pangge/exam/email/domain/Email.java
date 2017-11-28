package online.pangge.exam.email.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Email implements Serializable{
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String content;
    private String[] attchFileName;
    private Map<String,Object> paramMap=new HashMap<>();
    public Email(){}
    public Email(String from,String to,String subject,String content){
        this.from=from;
        this.to=to;
        this.subject=subject;
        this.content=content;
    }
}
