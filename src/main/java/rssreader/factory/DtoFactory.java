package rssreader.factory;

import rssreader.dto.RssDto;
import rssreader.dto.RssFeedDto;
import rssreader.entity.RssEntity;
import rssreader.entity.RssFeedEntity;

import java.util.ArrayList;
import java.util.List;

public class DtoFactory {
    private static DtoFactory instance = null;

    public static synchronized DtoFactory getInstance(){
        if(instance == null){
            instance = new DtoFactory();
        }
        return instance;
    }

    public RssDto getRssDto(RssEntity entity){
        RssDto rssDto = new RssDto();
        rssDto.setId(entity.getId());
        rssDto.setTitle(entity.getTitle());
        rssDto.setLink(entity.getLink());
        rssDto.setDescription(entity.getDescription());
        rssDto.setGuid(entity.getGuid());
        rssDto.setPubDate(entity.getPubDate());
        return rssDto;
    }

    public RssFeedDto getRssFeedDto(RssFeedEntity entity){
        RssFeedDto rssFeedDto = new RssFeedDto();
        rssFeedDto.setId(entity.getId());
        rssFeedDto.setTitle(entity.getTitle());
        rssFeedDto.setLink(entity.getLink());
        return rssFeedDto;
    }

    public List<RssDto> getRssDtoList(List<RssEntity> entities){
        List<RssDto> rssDtos = new ArrayList<>();
        for(RssEntity entity: entities){
            rssDtos.add(getRssDto(entity));
        }
        return rssDtos;
    }

    public List<RssFeedDto> getRssFeedDtoList(List<RssFeedEntity> entities){
        List<RssFeedDto> rssFeedDtos = new ArrayList<>();
        for(RssFeedEntity entity: entities){
            rssFeedDtos.add(getRssFeedDto(entity));
        }
        return rssFeedDtos;
    }
}
