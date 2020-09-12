package fm.douban.model;

import java.util.List;

public class MhzViewModel {

    private String title;
    private List<Subject> subjects;

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }
    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
    public List<Subject> getSubjects() {
        return this.subjects;
    }

}
