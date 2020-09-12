package fm.douban.app.control.test;

import fm.douban.model.Subject;
import fm.douban.service.SubjectService;
import fm.douban.util.SubjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test/subject")
public class SubjectTestControl {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/add")
    public Subject testAdd(){
        Subject subject = new Subject();
        subject.setId("1");
        subject.setSubjectType(SubjectUtil.TYPE_MHZ);
        subject.setSubjectSubType(SubjectUtil.TYPE_SUB_MOOD);
        return subjectService.addSubject(subject);
    }

    @GetMapping("/get")
    public Subject testGet(){
        return subjectService.get("1");
    }

    @GetMapping("/getByType")
    public List<Subject> testGetByType(){
        return subjectService.getSubjects(SubjectUtil.TYPE_MHZ);
    }

    @GetMapping("/getBySubType")
    public List<Subject> testGetBySubType(){
        return subjectService.getSubjects(SubjectUtil.TYPE_MHZ, SubjectUtil.TYPE_SUB_MOOD);
    }

    @GetMapping("/del")
    public boolean testDelete(){
        return subjectService.delete("1");
    }

}
