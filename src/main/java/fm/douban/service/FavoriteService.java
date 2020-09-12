package fm.douban.service;

import fm.douban.model.Favorite;

import java.util.List;

public interface FavoriteService {

    Favorite add(Favorite fav);

    List<Favorite> list(Favorite favParam);

    boolean delete(Favorite favParam);

}
