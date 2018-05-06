package online.pangge.exam.service.impl;

import com.google.gson.Gson;
import online.pangge.exam.domain.Subject;
import online.pangge.exam.domain.WrongSubjectLink;
import online.pangge.exam.service.IExamService;
import online.pangge.exam.service.IStudentService;
import online.pangge.exam.service.IWrongSubjectService;
import online.pangge.exam.util.ExamConst;
import online.pangge.exam.util.RedisUtil;
import online.pangge.wechat.damain.message.resp.Article;
import online.pangge.wechat.damain.message.resp.NewsMessage;
import online.pangge.wechat.damain.message.resp.TextMessage;
import online.pangge.wechat.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExamServiceImpl implements IExamService {
    private static Logger log = LoggerFactory.getLogger(ExamServiceImpl.class);
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IWrongSubjectService wrongSubjectService;
    @Override
    public String endExam(String fromUserName) {
        List<Subject> answerSubjects = redisUtil.getSubjects(fromUserName + ExamConst.exam_type_answer);
        int score = Correcting(answerSubjects, fromUserName);
        redisUtil.remove(fromUserName + "key");
        redisUtil.remove(fromUserName + ExamConst.exam_type_temp);
        redisUtil.remove(fromUserName + ExamConst.exam_type_answer);
        redisUtil.remove(fromUserName + ExamConst.exam_type_exercise);
        redisUtil.remove(fromUserName + "subjectNumber");
        return returnXML("你的分数是" + score);
    }

    @Override
    public void addAnswer(String fromUserName, String answer) {
        if (redisUtil.exists(fromUserName + ExamConst.exam_type_temp)) {
            Subject beforeSubject = redisUtil.getSubject(fromUserName + ExamConst.exam_type_temp);
            beforeSubject.setUserAnswer(answer);
            redisUtil.setSubject(fromUserName + ExamConst.exam_type_answer, beforeSubject);
        }
    }

    @Override
    public String returnXML(String content) {
        TextMessage textMessage = new TextMessage();
        textMessage.setContent(content);
        return MessageUtil.messageToXml(textMessage);
    }

    @Override
    public String getNewsMessageXML(String fromUserName, String toUserName, String subjectString) {
        Article article = new Article();
        Subject s = new Gson().fromJson(subjectString, Subject.class);
        int subjectNumber = 1;
        if (!redisUtil.exists(fromUserName + "subjectNumber")) {
            redisUtil.set(fromUserName + "subjectNumber", 1);
        } else {
            subjectNumber = Integer.valueOf(redisUtil.get(fromUserName + "subjectNumber").toString());
        }
        article.setTitle("第" + subjectNumber + "题," + s.getSubjectType().getTypeName() + "：");
        redisUtil.set(fromUserName + "subjectNumber", subjectNumber + 1);
        article.setDescription("点此进入正题。");
        article.setPicUrl("");
        try {
            subjectString = URLEncoder.encode(subjectString, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("encode error :" + subjectString);
        }
        article.setUrl("http://teaorcoffee.cn/exam.do?subjectString=" + subjectString);
        List<Article> articleList = new ArrayList<Article>();
        articleList.add(article);
        // 创建图文消息
        NewsMessage newsMessage = new NewsMessage();
        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(System.currentTimeMillis());
        newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
        newsMessage.setArticleCount(articleList.size());
        newsMessage.setArticles(articleList);
        return MessageUtil.messageToXml(newsMessage);
    }

    @Override
    public int Correcting(List<Subject> subjects, String fromusername) {
        int score = 0;
        List<WrongSubjectLink> wrongSubjects = new ArrayList<>();
        for (int i = 0; i < subjects.size(); i++) {
            Subject subject = subjects.get(i);
            if (subject.getAnswer().equals(subject.getUserAnswer())) {
                //right
                score += 1;
            } else {
                //wrong
                WrongSubjectLink wrongSubjectLink = new WrongSubjectLink();
                wrongSubjectLink.setSubId(subject.getId());
                wrongSubjectLink.setUserAnswer(subject.getUserAnswer());
                wrongSubjectLink.setLastUpdateDate(new Date());
                wrongSubjectLink.setUserId(studentService.selectByWechatName(fromusername).getStunum());
                wrongSubjects.add(wrongSubjectLink);
            }
        }
        if (!CollectionUtils.isEmpty(wrongSubjects)) {
            wrongSubjectService.insertWrongSubjectLinks(wrongSubjects);
        }
        return score;
    }
}
