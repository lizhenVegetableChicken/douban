package fm.douban.service;

import fm.douban.model.Subject;

import java.util.List;

public interface SubjectService {

    Subject addSubject(Subject subject);
    Subject get(String subjectId);
    List<Subject>getAll();
    List<Subject> getSubjects(String type);
    List<Subject> getSubjects(String type, String subType);
    List<Subject> getSubjects(Subject subjectParam);
    boolean modify(Subject subject);
    boolean delete(String subjectId);

}
