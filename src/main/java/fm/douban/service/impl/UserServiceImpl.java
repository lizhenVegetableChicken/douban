package fm.douban.service.impl;

import fm.douban.model.User;
import fm.douban.param.UserQueryParam;
import org.springframework.data.domain.Page;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fm.douban.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public User add(User user){

        if(user == null){
            LOG.error("add error because of Null");
            return null;
        }
        return mongoTemplate.insert(user);

    }

    @Override
    public User get(String id){

        if(!StringUtils.hasText(id)){
            LOG.error("get error because of Null Id");
            return null;
        }
        User user = mongoTemplate.findById(id, User.class);
        return user;

    }

    @Override
    public Page<User> list(UserQueryParam param){

        if(param==null){
            LOG.error("param is null");
            return null;
        }

        Criteria sumCri = new Criteria();
        List<Criteria> subCris = new ArrayList<>();

        if (StringUtils.hasText(param.getLoginName())){
            subCris.add(Criteria.where("loginName").is(param.getLoginName()));
        }
        if (StringUtils.hasText(param.getPassword())){
            subCris.add(Criteria.where("password").is(param.getPassword()));
        }
        if (StringUtils.hasText(param.getMobile())){
            subCris.add(Criteria.where("mobile").is(param.getMobile()));
        }
        if (subCris.size() == 0){
            LOG.error("subCriterias is null");
            return null;
        }
        sumCri.andOperator(subCris.toArray(new Criteria[]{}));
        Query query = new Query(sumCri);

        long count = mongoTemplate.count(query, User.class);

        Pageable pageable = PageRequest.of(param.getPageNum()-1, param.getPageSize());
        query.with(pageable);

        List<User> users = mongoTemplate.find(query, User.class);
        Page<User> userPage = PageableExecutionUtils.getPage(users, pageable, new LongSupplier() {
            @Override
            public long getAsLong() {
                return count;
            }
        });

        return userPage;

    }

    @Override
    public boolean modify(User user){

        if(user == null || !StringUtils.hasText(user.getId())){
            LOG.error("modify error because of Null");
            return false;
        }

        Query query = new Query(Criteria.where("id").is(user.getId()));
        Update updateData = new Update();

        if (StringUtils.hasText(user.getLoginName())) {
            updateData.set("loginName", user.getLoginName());
        }
        if (StringUtils.hasText(user.getPassword())) {
            updateData.set("password", user.getPassword());
        }
        if (StringUtils.hasText(user.getMobile())) {
            updateData.set("mobile", user.getMobile());
        }

        UpdateResult result = mongoTemplate.updateFirst(query, updateData, User.class);
        return result!=null && result.getModifiedCount()>0;

    }

    @Override
    public boolean delete(String id){

        if(!StringUtils.hasText(id)){
            LOG.error("delete error because of Null id");
            return false;
        }

        User user = new User();
        user.setId(id);

        DeleteResult result = mongoTemplate.remove(user);
        return result!=null && result.getDeletedCount()>0;

    }

}
