package rssreader.rssstores.impl;

import rssreader.dto.RssFeedDto;
import rssreader.rssstores.RssFeedStore;

import java.util.ArrayList;
import java.util.List;

public class ArrayListRssFeedStore implements RssFeedStore {
    private List<RssFeedDto> RssFeedList = new ArrayList<>();

    @Override
    public void clear() {
        RssFeedList.clear();
    }

    @Override
    public void add(RssFeedDto currentRssFeed) {
        RssFeedList.add(currentRssFeed);
    }

    @Override
    public List<RssFeedDto> getRssFeedList() {
        return RssFeedList;
    }
}