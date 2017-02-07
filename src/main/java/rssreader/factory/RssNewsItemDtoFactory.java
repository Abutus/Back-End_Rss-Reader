package rssreader.factory;

import rssreader.dto.RssNewsItemDto;
import rssreader.dto.RssFeedDto;
import rssreader.entity.RssNewsItemEntity;
import rssreader.entity.RssFeedEntity;

import java.util.ArrayList;
import java.util.List;

public class RssNewsItemDtoFactory {
    private static RssNewsItemDtoFactory instance = null;

    public static synchronized RssNewsItemDtoFactory getInstance(){
        if(instance == null){
            instance = new RssNewsItemDtoFactory();
        }
        return instance;
    }

    public RssNewsItemDto createRssNewsItemDto(RssNewsItemEntity entity){
        RssNewsItemDto rssNewsItemDto = new RssNewsItemDto();
        rssNewsItemDto.setId(entity.getId());
        rssNewsItemDto.setTitle(entity.getTitle());
        rssNewsItemDto.setLink(entity.getLink());
        rssNewsItemDto.setDescription(entity.getDescription());
        rssNewsItemDto.setGuid(entity.getGuid());
        rssNewsItemDto.setPubDate(entity.getPubDate());
        return rssNewsItemDto;
    }

    public List<RssNewsItemDto> createRssNewsItemDtoList(List<RssNewsItemEntity> entities){
        List<RssNewsItemDto> rssNewsItemDtos = new ArrayList<>();
        entities.stream().forEach(entity -> rssNewsItemDtos.add(createRssNewsItemDto(entity)));
        return rssNewsItemDtos;
    }
}
