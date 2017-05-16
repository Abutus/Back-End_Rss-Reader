package rssreader.rssstores.impl;

import rssreader.entity.RssNewsItemEntity;
import rssreader.rssstores.RssNewsStore;

import java.util.ArrayList;
import java.util.List;

public class ArrayListRssNewsStore implements RssNewsStore {
    List<RssNewsItemEntity> RssList = new ArrayList<>();

    @Override
    public void clear() {
        RssList.clear();
    }

    @Override
    public void add(RssNewsItemEntity currentRss) {
        RssList.add(currentRss);
    }

    @Override
    public List<RssNewsItemEntity> getRssList() {
        return RssList;
    }
}
