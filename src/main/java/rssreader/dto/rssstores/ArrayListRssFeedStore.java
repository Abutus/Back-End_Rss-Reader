package rssreader.dto.rssstores;

import rssreader.dto.RssFeedDto;

import java.util.ArrayList;
import java.util.List;

public class ArrayListRssFeedStore implements RssFeedStore{
    List<RssFeedDto> RssFeedList = new ArrayList<>();

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