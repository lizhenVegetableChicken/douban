package fm.douban.service.impl;

import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fm.douban.model.Singer;
import fm.douban.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.mongodb.core.query.Query;


import java.util.List;

@Service
public class SingerServiceImpl implements SingerService {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(SingerServiceImpl.class);

    @Override
    public Singer addSinger(Singer singer) {
        if (singer == null) {
            LOG.error("input singer data is null!");
            return null;
        }
        return mongoTemplate.save(singer);
//        if (get(singer.getId()) == null || !StringUtils.hasText(singer.getId())) {
//            System.out.println(singer);
//            return mongoTemplate.save(singer);
//        }
    }

    @Override
    public Singer get(String singerId) {
        if (!StringUtils.hasText(singerId)) {
            LOG.error("input singerId data is blank!");
            return null;
        }
        Singer singer = mongoTemplate.findById(singerId, Singer.class);
        return singer;

    }

    @Override
    public List<Singer> getAll() {
        List<Singer> singers = mongoTemplate.findAll(Singer.class);
        return singers;
    }

    @Override
    public boolean modify(Singer singer) {
        // 作为服务，要对入参进行判断，不能假设被调用时，入参一定正确
        if (singer == null || !StringUtils.hasText(singer.getId())) {
            LOG.error("input singer data is not correct.");
            return false;
        }
        // 主键不能修改
        Query query = new Query(Criteria.where("id").is(singer.getId()));

        Update updateData = new Update();
        // 值为 null 表示不修改。值为长度为 0 的字符串 "" 表示清空此字段
        if (singer.getName() != null) {
            updateData.set("name", singer.getName());
        }

        if (singer.getAvatar() != null) {
            updateData.set("lyrics", singer.getAvatar());
        }

        if (singer.getHomepage() != null) {
            updateData.set("subjectId", singer.getHomepage());
        }

        if (singer.getSimilarSingerIds() != null) {
            updateData.set("similarSingerIds", singer.getSimilarSingerIds());
        }

        if (singer.getSongIds() != null) {
            updateData.set("songIds", singer.getSongIds());
        }

        UpdateResult result = mongoTemplate.updateFirst(query, updateData, Singer.class);
        return result.getModifiedCount() > 0;

    }

    @Override
    public boolean delete(String singerId) {
        if (!StringUtils.hasText(singerId)) {
            LOG.error("input singerId is blank!");
            return false;
        }

        Singer singer =new Singer();
        singer.setId(singerId);
        DeleteResult result = mongoTemplate.remove(singer);
        return result.getDeletedCount()>0;

    }

}
