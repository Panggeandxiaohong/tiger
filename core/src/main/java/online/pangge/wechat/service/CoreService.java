package online.pangge.wechat.service;

import com.google.gson.Gson;
import online.pangge.exam.domain.Student;
import online.pangge.exam.domain.Subject;
import online.pangge.exam.domain.WrongSubjectLink;
import online.pangge.exam.service.IStudentService;
import online.pangge.exam.service.ISubjectService;
import online.pangge.exam.service.IWrongSubjectService;
import online.pangge.exam.util.ExamConst;
import online.pangge.exam.util.OSSUtil;
import online.pangge.exam.util.RedisUtil;
import online.pangge.wechat.damain.XmlMessageEntity;
import online.pangge.wechat.damain.message.resp.Article;
import online.pangge.wechat.damain.message.resp.NewsMessage;
import online.pangge.wechat.damain.message.resp.TextMessage;
import online.pangge.wechat.util.MessageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 核心服务类,由此类去调用其它类
 *
 * @author Jimmy
 * @date 2017-04-04
 */
@Service
public class CoreService {
    /**
     * 处理微信发来的请求
     *
     * @return xml
     */
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OSSUtil ossUtil;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private IWrongSubjectService wrongSubjectService;

    public String processRequest(XmlMessageEntity entity) {
        // xml格式的消息数据
        String respXml = null;
        // 默认返回的文本消息内容
        String respContent = "未知的消息类型！";
        try {
            // 调用parseXml方法解析请求消息
//            Map<String, String> requestMap = MessageUtil.parseXml(request);
            // 发送方帐号
            String fromUserName = entity.getFromUserName();
            // 开发者微信号
            String toUserName = entity.getToUserName();
            // 消息类型
            String msgType = entity.getMsgType();
            //消息内容
            String msg = entity.getContent();
            // 回复文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(System.currentTimeMillis());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            String key = null;
            String responseStr = null;
            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                String redisKey = (String) redisUtil.get(fromUserName +"key");
                if (StringUtils.isEmpty(redisKey)) {
                    if (msg.contains("自测")) {
                        redisUtil.set(fromUserName +"key", "exam", 3600L);
                        responseStr = "开始自测。。。";
                    } else if (msg.contains("统计")) {
                        redisUtil.set(fromUserName +"key", "count", 3600L);
                        responseStr = "真正的开始统计。。。";
                    }else if(msg.contains("绑定")){
                        redisUtil.set(fromUserName +"key", "bind", 3600L);
                        responseStr = "开始绑定，请输入学号#密码进行绑定，比如：000#000";
                    } else if (msg.contains("练习")) {
                        redisUtil.remove(fromUserName + ExamConst.exam_type_exercise);
                        redisUtil.set(fromUserName +"key", "exercise", 3600L);
                        List<Subject> allSubject = subjectService.selectAll();
                        System.out.println(allSubject.size()+"===============================");
                        for (Subject s : allSubject) {
                            System.out.println("set subject ========="+fromUserName + ExamConst.exam_type_exercise);
                            redisUtil.setSubject(fromUserName + ExamConst.exam_type_exercise, s);
                        }
                        responseStr = "开始练习。。。";
                    } else {
                        responseStr = "请按套路出牌";
                    }
                } else {
                    if ("退出".equals(msg)) {
                        redisUtil.remove(fromUserName +"key");
                        redisUtil.remove(fromUserName + ExamConst.exam_type_exercise);
                        redisUtil.remove(fromUserName + "subjectNumber");
                        responseStr = "退出成功。。。";
                    } else if ("count".equals(redisKey)) {
                        responseStr = "统计中。。。";
                    }else if ("bind".equals(redisKey)) {
                        String[] userNameAndPassword = msg.split("#");
                        System.out.println("stunum = "+Long.valueOf(userNameAndPassword[0]));
                        Student stu = studentService.selectByStunum(Long.valueOf(userNameAndPassword[0])).get(0);
                        if(stu.getPassword().equals(userNameAndPassword[1])){
                            stu.setWechatname(fromUserName);
                            studentService.updateByPrimaryKey(stu);
                            responseStr = "綁定成功。。。";
                        }else{
                            responseStr = "綁定失敗。。。";
                        }
                        redisUtil.remove(fromUserName +"key");
                    }  else if ("exercise".equals(redisKey)) {
                        if (!redisUtil.exists(fromUserName + ExamConst.exam_type_exercise)) {
                            redisUtil.remove(fromUserName+"key");
                            redisUtil.remove(fromUserName + "subjectNumber");
                            respContent = "你的分数是";
                            List<Subject> answerSubjects = redisUtil.getSubjects(fromUserName + ExamConst.exam_type_answer);
                            int score = Correcting(answerSubjects,fromUserName);
                            // 设置文本消息的内容
                            textMessage.setContent(respContent+score);
                            // 将文本消息对象转换成xml
                            respXml = MessageUtil.messageToXml(textMessage);
                            return respXml;
                        }

                        if(redisUtil.exists(fromUserName + ExamConst.exam_type_temp)){
                            Subject beforeSubject = redisUtil.getSubject(fromUserName + ExamConst.exam_type_temp);
                            beforeSubject.setUserAnswer(msg);
                            redisUtil.setSubject(fromUserName + ExamConst.exam_type_answer, beforeSubject);
                        }
                        Subject subject = redisUtil.getSubject(fromUserName + ExamConst.exam_type_exercise);
                        redisUtil.setSubject(fromUserName + ExamConst.exam_type_temp,subject);
//                        redisUtil.set(fromUserName + "subjectNumber", Integer.valueOf(redisUtil.get(fromUserName+"subjectNumber").toString())+1);
                        String subjectStr = new Gson().toJson(subject,Subject.class);
                        return getNewsMessageXML(fromUserName, toUserName, subjectStr);
                    } else if ("exam".equals(redisKey)) {
                        responseStr = "考试中。。。";
                    }
                }
                respContent = responseStr;
            }
            // 图片消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
            }
            // 语音消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
                respContent = "您发送的是语音消息！消息id是" + entity.getRecognition();
            }
            // 视频消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
                respContent = "您发送的是视频消息！";
            }
            // 地理位置消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                respContent = "您发送的是地理位置消息！";
            }
            // 链接消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
                respContent = "您发送的是链接消息！";
            }
            // 事件推送
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = entity.getEvent();//requestMap.get("Event");
                // 关注
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    respContent = "谢谢您的关注！";
                }
                // 取消关注
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    // TODO 取消订阅后用户不会再收到公众账号发送的消息，因此不需要回复
                }
                // 扫描带参数二维码
                else if (eventType.equals(MessageUtil.EVENT_TYPE_SCAN)) {
                    // TODO 处理扫描带参数二维码事件
                }
                // 上报地理位置
                else if (eventType.equals(MessageUtil.EVENT_TYPE_LOCATION)) {
                    //处理上报地理位置事件
                }
                // 自定义菜单
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                    // TODO 处理菜单点击事件

                }
            }
            // 设置文本消息的内容
            textMessage.setContent(respContent);
            // 将文本消息对象转换成xml
            respXml = MessageUtil.messageToXml(textMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respXml;
    }

    private String getNewsMessageXML(String fromUserName, String toUserName, String subjectString) throws UnsupportedEncodingException {
        Article article = new Article();
        Subject s = new Gson().fromJson(subjectString,Subject.class);
        int subjectNumber = 1;
        if (!redisUtil.exists(fromUserName + "subjectNumber")) {
            redisUtil.set(fromUserName + "subjectNumber", 1);
        } else {
            subjectNumber = Integer.valueOf(redisUtil.get(fromUserName +"subjectNumber").toString());
        }
        article.setTitle("这是第"+subjectNumber+"题：");
        redisUtil.set(fromUserName + "subjectNumber", subjectNumber+1);
        article.setDescription(s.getQuestion());
        article.setPicUrl("");
        subjectString = URLEncoder.encode(subjectString,"utf-8");
        article.setUrl("http://39.108.2.41/exam.do?subjectString="+subjectString);
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
    private int Correcting(List<Subject> subjects,String fromusername){
        int score = 0;
        List<WrongSubjectLink> wrongSubjects = new ArrayList<>();
        for(int i = 0 ; i < subjects.size();i++){
            Subject subject = subjects.get(i);
            if(subject.getAnswer().equals(subject.getUserAnswer())){
                //reght
                score +=1;
            }else{
                //wrong
                WrongSubjectLink wrongSubjectLink = new WrongSubjectLink();
                wrongSubjectLink.setSubId(subject.getId());
                wrongSubjectLink.setUserAnswer(subject.getUserAnswer());
                wrongSubjectLink.setLastUpdateDate(new Date());
                wrongSubjectLink.setUserId(studentService.selectByWechatName(fromusername).get(0).getStunum());
                wrongSubjects.add(wrongSubjectLink);
            }
        }
        wrongSubjectService.insertWrongSubjectLinks(wrongSubjects);
        return score;
    }
}
