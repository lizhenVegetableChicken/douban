package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fm.douban.model.Song;
import fm.douban.param.SongQueryParam;
import fm.douban.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class SongServiceImpl implements SongService {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(SongServiceImpl.class);

    @Override
    public Song add(Song song) {
        if (song == null) {
            LOG.error("input singer data is null!");
            return null;
        }
        if (get(song.getId()) == null || !StringUtils.hasText(song.getId())) {
            return mongoTemplate.insert(song);
        }
        return null;
    }

    @Override
    public Song get(String songId) {
        if (!StringUtils.hasText(songId)) {
            LOG.error("input singerId data is blank!");
            return null;
        }
        return mongoTemplate.findById(songId, Song.class);
    }


    @Override
    public List<Song> getAll() {
        return mongoTemplate.findAll(Song.class);
    }

    @Override
    public Page<Song> list(SongQueryParam songQueryParam) {
        if (songQueryParam == null) {
            LOG.error("input song data is not correct");
            return null;
        }
        Criteria criteria = new Criteria();
        List<Criteria> subCris = new ArrayList<>();
        if(StringUtils.hasText(songQueryParam.getName())) {
            subCris.add(Criteria.where("name").is(songQueryParam.getName()));
        }
        if(StringUtils.hasText(songQueryParam.getLyrics())) {
            subCris.add(Criteria.where("lyrics").is(songQueryParam.getLyrics()));
        }

        List<Song> songs;
        long count;
        Pageable pageable = PageRequest.of(songQueryParam.getPageNum(), songQueryParam.getPageSize());
        Query query;

        if (subCris.isEmpty()) {
            songs = mongoTemplate.findAll(Song.class);
            count = songs.size();
        } else {
            criteria.orOperator(subCris.toArray(new Criteria[]{}));
            query = new Query(criteria);
            count = mongoTemplate.count(query,Song.class);
            query.with(pageable);
            songs = mongoTemplate.find(query, Song.class);
        }
        long finalCount = count;
        Page<Song> pageResult = PageableExecutionUtils.getPage(songs, pageable, new LongSupplier() {
            @Override
            public long getAsLong() {
                return finalCount;
            }
        });
        return pageResult;
    }

    @Override
    public boolean modify(Song song) {
        // 作为服务，要对入参进行判断，不能假设被调用时，入参一定正确
        if (song == null || !StringUtils.hasText(song.getId())) {
            LOG.error("input song data is not correct.");
            return false;
        }

        // 主键不能修改
        Query query = new Query(Criteria.where("id").is(song.getId()));

        Update updateData = new Update();
        if (song.getName() != null) {
            updateData.set("name", song.getName());
        }

        if (song.getLyrics() != null) {
            updateData.set("lyrics", song.getLyrics());
        }

        if (song.getUrl() != null) {
            updateData.set("url", song.getUrl());
        }

        if (song.getCover() != null) {
            updateData.set("cover", song.getCover());
        }

        UpdateResult result = mongoTemplate.updateFirst(query, updateData, Song.class);
        return result.getModifiedCount() > 0;

    }

    @Override
    public boolean delete(String songId) {
        if (!StringUtils.hasText(songId)) {
            LOG.error("input songId is blank!");
            return false;
        }

        Song song =new Song();
        song.setId(songId);
        DeleteResult result = mongoTemplate.remove(song);
        return result.getDeletedCount()>0;

    }

}
