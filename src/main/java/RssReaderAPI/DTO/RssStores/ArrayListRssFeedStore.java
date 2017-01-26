package RssReaderAPI.DTO.RssStores;

import RssReaderAPI.DTO.RssDto;

import java.util.ArrayList;
import java.util.List;

public class ArrayListRssFeedStore implements RssStore{
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
