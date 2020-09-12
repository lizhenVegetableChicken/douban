package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fm.douban.model.Subject;
import fm.douban.param.SubjectQueryParam;
import fm.douban.service.SubjectService;
import fm.douban.util.SubjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(SubjectServiceImpl.class);

    @Override
    public Subject addSubject(Subject subject) {
        if (subject == null) {
            LOG.error("input subject is null!");
            return null;
        }
        if (mongoTemplate.findById(subject.getId(), Subject.class) == null) {
            return mongoTemplate.insert(subject);
        }
        return null;
    }

    @Override
    public Subject get(String subjectId){
        if (!StringUtils.hasText(subjectId)){
            LOG.error("input subjectId id blank!");
            return null;
        }
        return mongoTemplate.findById(subjectId, Subject.class);

    }

    @Override
    public List<Subject> getSubjects(String type){
        if (!StringUtils.hasText(type)){
            LOG.error("input type is blank!");
            return null;
        }
        Query query = new Query(Criteria.where("subjectType").is(type));
        return mongoTemplate.find(query, Subject.class);
    }

    @Override
    public List<Subject> getAll() {
        return mongoTemplate.findAll(Subject.class);
    }

    @Override
    public List<Subject> getSubjects(String type, String subType) {
        Subject subjectParam = new Subject();
        subjectParam.setSubjectType(type);
        subjectParam.setSubjectSubType(subType);

        return getSubjects(subjectParam);
    }

    @Override
    public List<Subject> getSubjects(Subject subjectParam){
        if (subjectParam == null) {
            LOG.error("input subjectParam is not correct.");
            return null;
        }

        Criteria andCris = new Criteria();
        List<Criteria> subCris = new ArrayList<>();
        subCris.add(Criteria.where("subjectType").is(subjectParam.getSubjectType()));

        if (subjectParam.getSubjectType().equals(SubjectUtil.TYPE_COLLECTION)) {
            subCris.add(Criteria.where("master").is(subjectParam.getMaster()));
        } else {
            subCris.add(Criteria.where("subjectSubType").is(subjectParam.getSubjectSubType()));
        }
        andCris.andOperator(subCris.toArray(new Criteria[]{}));

        Query query = new Query(andCris);
        return mongoTemplate.find(query, Subject.class);
    }

    @Override
    public boolean modify (Subject subject) {
        // 作为服务，要对入参进行判断，不能假设被调用时，入参一定正确
        if (subject == null || !StringUtils.hasText(subject.getId())) {
            LOG.error("input subject data is not correct.");
            return false;
        }
        // 主键不能修改
        Query query = new Query(Criteria.where("id").is(subject.getId()));

        Update updateData = new Update();
        // 值为 null 表示不修改。值为长度为 0 的字符串 "" 表示清空此字段
        if (subject.getName() != null) {
            updateData.set("name", subject.getName());
        }

        if (subject.getCover() != null) {
            updateData.set("cover", subject.getCover());
        }

        if (subject.getSongIds() != null) {
            updateData.set("songIds", subject.getSongIds());
        }

        UpdateResult result = mongoTemplate.updateFirst(query, updateData, Subject.class);
        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean delete(String subjectId){
        if (!StringUtils.hasText(subjectId)){
            LOG.error("input subjectId id blank!");
            return false;
        }
        Subject subject = new Subject();
        subject.setId(subjectId);
        DeleteResult result = mongoTemplate.remove(subject);

        return result.getDeletedCount()>0;
    }

}
