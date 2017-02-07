package rssreader.factory;

import rssreader.dto.RssFeedDto;
import rssreader.entity.RssFeedEntity;

import java.util.ArrayList;
import java.util.List;

public class RssFeedDtoFactory {
    private static RssFeedDtoFactory instance = null;

    public static synchronized RssFeedDtoFactory getInstance(){
        if(instance == null){
            instance = new RssFeedDtoFactory();
        }
        return instance;
    }

    public RssFeedDto createRssFeedDto(RssFeedEntity entity){
        RssFeedDto rssFeedDto = new RssFeedDto();
        rssFeedDto.setId(entity.getId());
        rssFeedDto.setTitle(entity.getTitle());
        rssFeedDto.setLink(entity.getLink());
        return rssFeedDto;
    }

    public List<RssFeedDto> createRssFeedDtoList(List<RssFeedEntity> entities){
        List<RssFeedDto> rssFeedDtos = new ArrayList<>();
        entities.stream().forEach(entity -> rssFeedDtos.add(createRssFeedDto(entity)));
        return rssFeedDtos;
    }
}
