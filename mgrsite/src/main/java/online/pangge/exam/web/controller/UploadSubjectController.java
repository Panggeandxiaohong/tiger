package online.pangge.exam.web.controller;

import net.sf.jxls.transformer.XLSTransformer;
import online.pangge.exam.domain.Subject;
import online.pangge.exam.page.PageResult;
import online.pangge.exam.query.SubjectQueryObject;
import online.pangge.exam.service.IClassesService;
import online.pangge.exam.service.ISubjectService;
import online.pangge.exam.service.ISubjectTypeService;
import online.pangge.exam.util.*;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jie34 on 2017/4/30.
 */
@Controller
@RequestMapping("/subject")
public class UploadSubjectController {
    private static Logger logger = Logger.getLogger(UploadSubjectController.class);
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private ISubjectTypeService subjectTypeService;
    @Autowired
    private IClassesService classesService;
    @Autowired
    private OSSUtil ossUtil;

    @RequestMapping("/index.do")
    @RequiredPermission("管理试题")
    public String subject() {
        return "subject";
    }

    @RequestMapping("/list.do")
    @ResponseBody
    public PageResult<Subject> subjectList(HttpServletRequest req) {
        SubjectQueryObject qo = new SubjectQueryObject();
        qo.setPage(Integer.valueOf(StringUtils.isEmpty(req.getParameter("page")) ? "5" : req.getParameter("page")));
        qo.setRows(Integer.valueOf(StringUtils.isEmpty(req.getParameter("rows")) ? "5" : req.getParameter("rows")));
        qo.setClasses_combobox(Long.valueOf(StringUtils.isEmpty(req.getParameter("classes_combobox")) ? "-1" : req.getParameter("classes_combobox")));
        qo.setSubject_type_combobox(Long.valueOf(StringUtils.isEmpty(req.getParameter("subject_type_combobox")) ? "-1" : req.getParameter("subject_type_combobox")));
        qo.setMedia_type_combobox(StringUtils.isEmpty(req.getParameter("media_type_combobox")) ? null : req.getParameter("media_type_combobox"));
        qo.setProcess_status_combobox(StringUtils.isEmpty(req.getParameter("process_status_combobox")) ? null : req.getParameter("process_status_combobox"));
        qo.setKeyword(req.getParameter("keyword"));
        PageResult<Subject> page = subjectService.page(qo);
        return page;
    }

    private String getFileType(MultipartFile myfiles) {
        String fileNameRar = myfiles.getOriginalFilename();
        String[] fileTypesRar = fileNameRar.split("\\.");
        return StringUtils.lowerCase(fileTypesRar[fileTypesRar.length - 1]);
    }


    @RequestMapping("/upload.do")
    @ResponseBody
    @RequiredPermission("上传题目")
    public AjaxResult subjectUpload(MultipartFile subjects, MultipartFile more_myfiles, HttpServletRequest req) {
        if (!ExamConst.subjectsFileType.contains(getFileType(subjects)) || !ExamConst.subjectFilesType.contains(getFileType(more_myfiles))) {
            return new AjaxResult(false, "操作失败！文件类型不正确！");
        }
        try {
            UserContext.setLoacl(req);
            subjectService.importSubject(subjects, more_myfiles);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return new AjaxResult(true, "操作成功！");
    }

    @RequiredPermission("下载现有试题")
    @RequestMapping("/download_subject.do")
    public void downloadSubject(HttpServletResponse resp, HttpServletRequest req) throws IOException {
        String filePath = req.getSession().getServletContext().getRealPath("/download");
        OutputStream out = null;
        InputStream in = null;
        File templateFile = new File(filePath + "/templateForData.xlsx");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", "attachment;filename=" + templateFile.getName());
        XLSTransformer fmt = new XLSTransformer();
        Map<String, List<Subject>> data = new HashMap<>();
        List<Subject> subjects = subjectService.selectAll();
        data.put("subjects", subjects);
        try {
            System.out.println("template path = = = = == = = == "+templateFile.getPath());
            in = new BufferedInputStream(new FileInputStream(templateFile));
            out = resp.getOutputStream();
            Workbook workBook = fmt.transformXLS(in, data);
            workBook.write(out);
            out.flush();
        } catch (Exception e) {
            logger.error("download error : ", e);
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @RequiredPermission("下载模板")
    @RequestMapping("/download_template.do")
    public void subjectDownloadTemplate(HttpServletResponse resp, HttpServletRequest req) throws IOException {
        String filePath = req.getSession().getServletContext().getRealPath("/download");
        OutputStream out = null;
        FileInputStream templateStream = null;
        File templateFile = new File(filePath + "/template.xlsx");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("multipart/form-data");
        resp.setHeader("Content-Disposition", "attachment;filename=" + templateFile.getName());
        try {
            out = resp.getOutputStream();
            templateStream = new FileInputStream(templateFile);
            IOUtils.copy(templateStream, out);
        } catch (IOException e) {
            logger.error("download error : ", e);
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    @RequestMapping("/delete.do")
    @ResponseBody
    @RequiredPermission("删除试题")
    public AjaxResult deleteSubject(@RequestParam("ids[]") List<Long> params, String type) {
        AjaxResult result = null;
        String activeOrDelete = null;
        if (ExamConst.processStatus.Deleted.name().equals(type)) {
            activeOrDelete = ExamConst.processStatus.Deleted.name();
        } else {
            activeOrDelete = ExamConst.processStatus.Active.name();
        }
        int deleteCount = subjectService.delete(params, activeOrDelete);
        if (deleteCount == 0) {
            result = new AjaxResult(false, "操作失败，操作的条目为0！");
        } else {
            result = new AjaxResult(true, "操作成功！");
        }
        return result;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    @RequiredPermission("保存试题")
    public AjaxResult saveSubject(MultipartFile myfiles, HttpServletRequest request) {
        System.out.println("ininininin");
        AjaxResult result = null;
        Subject subject = new Subject();
        subject.setMediaType(request.getParameter("media_type"));
        subject.setSubjectType(subjectTypeService.selectByPrimaryKey(Long.valueOf(request.getParameter("subject_type"))));
        if (ExamConst.wechat_subject_type_choice.equals(subject.getSubjectType().getId())) {
            subject.setAnswerA(request.getParameter("answerA"));
            subject.setAnswerB(request.getParameter("answerB"));
            subject.setAnswerC(request.getParameter("answerC"));
            subject.setAnswerD(request.getParameter("answerD"));
        }
        if (!ExamConst.mediaType.text.name().equals(subject.getMediaType())) {
            if (myfiles.isEmpty()) {
                return new AjaxResult(false, "保存失败，请上传附件！");
            }
            String fileName = myfiles.getOriginalFilename();
            String[] fileTypes = fileName.split("\\.");
            String fileType = StringUtils.lowerCase(fileTypes[fileTypes.length - 1]);
            CommonsMultipartFile cf = (CommonsMultipartFile) myfiles;
            DiskFileItem fi = (DiskFileItem) cf.getFileItem();
            File file = fi.getStoreLocation();
            if (ExamConst.mediaType.image.name().equals(subject.getMediaType())) {
                if (!ExamConst.images.contains(fileType)) {
                    return new AjaxResult(false, "图片格式不正确！");
                }
            } else if (ExamConst.mediaType.video.name().equals(subject.getMediaType())) {
                if (!ExamConst.videos.contains(fileType)) {
                    return new AjaxResult(false, "视频格式不正确！");
                }
                return new AjaxResult(false, "系统暂不支持视频格式~");
            } else if (ExamConst.mediaType.voice.name().equals(subject.getMediaType())) {
                if (!ExamConst.voices.contains(fileType)) {
                    return new AjaxResult(false, "音频格式不正确！");
                }
            }
            if (!ossUtil.isExist(myfiles.getOriginalFilename())) {
                subject.setUrl(ossUtil.uploadObj(null, myfiles.getOriginalFilename(), file));
                logger.info("上传文件成功,key为：" + myfiles.getOriginalFilename() + "，url为:" + subject.getUrl());
            } else {
                logger.info("上传文件失败，已经存在这个文件,key为：" + myfiles.getOriginalFilename());
                return new AjaxResult(false, "该资源已经存在，请确认名字和题目！");
            }
        }
        subject.setQuestion(request.getParameter("question"));
        subject.setScore(Double.valueOf(request.getParameter("score")));
        subject.setClasses(classesService.selectById(Long.valueOf(request.getParameter("classes"))));
        subject.setAnswer(request.getParameter("answer"));
        subject.setExplain(request.getParameter("explain"));
        subject.setAddtime(new Date());
        subject.setProcessStatus(ExamConst.processStatus.Active.name());
        if (subjectService.insert(subject) > 0) {
            logger.info("新增题目成功:" + subject.toString());
            result = new AjaxResult(true, "保存成功");
        } else {
            logger.info("新增题目失败，向数据库中写入题目时成功的条数为0 :" + subject.toString());
            result = new AjaxResult(false, "保存失败");
        }
        return result;
    }
}
