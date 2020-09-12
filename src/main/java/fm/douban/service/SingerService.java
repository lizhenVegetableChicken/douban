package fm.douban.service;

import fm.douban.model.Singer;

import java.util.List;

public interface SingerService {

    Singer addSinger(Singer singer);
    Singer get(String singerId);
    List<Singer> getAll();
    boolean modify(Singer singer);
    boolean delete(String singerId);


}
