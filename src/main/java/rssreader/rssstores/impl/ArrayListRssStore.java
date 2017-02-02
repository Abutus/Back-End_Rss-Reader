package rssreader.rssstores.impl;

import rssreader.dto.RssDto;
import rssreader.rssstores.RssStore;

import java.util.ArrayList;
import java.util.List;

public class ArrayListRssStore implements RssStore {
    List<RssDto> RssList = new ArrayList<>();

    @Override
    public void clear() {
        RssList.clear();
    }

    @Override
    public void add(RssDto currentRss) {
        RssList.add(currentRss);
    }

    @Override
    public List<RssDto> getRssList() {
        return RssList;
    }
}
