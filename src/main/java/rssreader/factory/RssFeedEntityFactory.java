package rssreader.factory;

import rssreader.dto.RssFeedDto;
import rssreader.entity.RssFeedEntity;
import java.util.ArrayList;
import java.util.List;

public class RssFeedEntityFactory {
    private static RssFeedEntityFactory instance = null;

    public static synchronized RssFeedEntityFactory getInstance(){
        if(instance == null){
            instance = new RssFeedEntityFactory();
        }
        return instance;
    }

    public RssFeedEntity createRssFeedEntity(RssFeedDto dto){
        RssFeedEntity rssFeedEntity = new RssFeedEntity();
        rssFeedEntity.setId(dto.getId());
        rssFeedEntity.setTitle(dto.getTitle());
        rssFeedEntity.setLink(dto.getLink());
        return rssFeedEntity;
    }

    public List<RssFeedEntity> getRssFeedEntityList(List<RssFeedDto> dtos){
        List<RssFeedEntity> rssFeedEntities = new ArrayList<>();
        dtos.stream().forEach(dto -> rssFeedEntities.add(createRssFeedEntity(dto)));
        return rssFeedEntities;
    }
}
