package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import fm.douban.model.Favorite;
import fm.douban.service.FavoriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(FavoriteServiceImpl.class);

    @Override
    public Favorite add(Favorite fav) {
        if (fav == null) {
            LOG.error("favorite is null!");
            return null;
        }
        if (mongoTemplate.findById(fav.getId(),Favorite.class)!=null || fav.getId()==null) {
            LOG.error("favorite id is null or has existed!");
        }
        return mongoTemplate.insert(fav);
    }

    @Override
    public List<Favorite> list(Favorite favParam) {
        if (favParam == null) {
            LOG.error("input favParam is blank!");
            return null;
        }
        Criteria subCriteria = new Criteria();
        List<Criteria> criterias = getCriteriaList(favParam);
        if (criterias == null) {
            LOG.error("查询条件不正确");
            return null;
        }
        subCriteria.andOperator(criterias.toArray(new Criteria[]{}));
        Query query = new Query(subCriteria);
        query.limit(20);
        return mongoTemplate.find(query, Favorite.class);
    }

    @Override
    public boolean delete(Favorite favParam) {
        if (favParam == null || !StringUtils.hasText(favParam.getId())) {
            LOG.error("input favParam is blank!");
            return false;
        }
        Favorite fav = new Favorite();
        fav.setId(favParam.getId());
        fav.setItemType(favParam.getItemType());
        fav.setItemId(favParam.getItemId());

        DeleteResult result = mongoTemplate.remove(fav);
        return result.getDeletedCount()>0;
    }

    private List<Criteria> getCriteriaList(Favorite favParam) {

        List<Criteria> criterias = new ArrayList<>();
        if (StringUtils.hasText(favParam.getType())) {
            criterias.add(Criteria.where("type").is(favParam.getType()));
        }
        if (StringUtils.hasText(favParam.getItemType())) {
            criterias.add(Criteria.where("itemType").is(favParam.getItemType()));
        }
        if (StringUtils.hasText(favParam.getItemId())) {
            criterias.add(Criteria.where("itemId").is(favParam.getItemId()));
        }
        if (criterias.isEmpty()) {
            return null;
        }
        return criterias;
    }

}
