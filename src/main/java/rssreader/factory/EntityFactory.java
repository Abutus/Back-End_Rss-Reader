package rssreader.factory;

import rssreader.dto.RssDto;
import rssreader.dto.RssFeedDto;
import rssreader.entity.RssEntity;
import rssreader.entity.RssFeedEntity;
import java.util.ArrayList;
import java.util.List;

public class EntityFactory {
    private static EntityFactory instance = null;

    public static synchronized EntityFactory getInstance(){
        if(instance == null){
            instance = new EntityFactory();
        }
        return instance;
    }

    public RssEntity getRssEntity(RssDto dto){
        RssEntity rssEntity = new RssEntity();
        rssEntity.setId(dto.getId());
        rssEntity.setTitle(dto.getTitle());
        rssEntity.setLink(dto.getLink());
        rssEntity.setDescription(dto.getDescription());
        rssEntity.setGuid(dto.getGuid());
        rssEntity.setPubDate(dto.getPubDate());
        return rssEntity;
    }

    public RssFeedEntity getRssFeedEntity(RssFeedDto dto){
        RssFeedEntity rssFeedEntity = new RssFeedEntity();
        rssFeedEntity.setId(dto.getId());
        rssFeedEntity.setTitle(dto.getTitle());
        rssFeedEntity.setLink(dto.getLink());
        return rssFeedEntity;
    }

    public List<RssEntity> getRssEntityList(List<RssDto> dtos){
        List<RssEntity> rssEntities = new ArrayList<>();
        for(RssDto dto: dtos){
            rssEntities.add(getRssEntity(dto));
        }
        return rssEntities;
    }

    public List<RssFeedEntity> getRssFeedEntityList(List<RssFeedDto> dtos){
        List<RssFeedEntity> rssFeedEntities = new ArrayList<>();
        for(RssFeedDto dto: dtos){
            rssFeedEntities.add(getRssFeedEntity(dto));
        }
        return rssFeedEntities;
    }
}
