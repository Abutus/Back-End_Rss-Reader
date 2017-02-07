package rssreader.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@XmlRootElement
public class RssNewsItemDto {
    private long id;
    private String title;
    private String link;
    private String description;
    private String guid;
    private Instant pubDate;

    public RssNewsItemDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Instant getPubDate() {
        return pubDate;
    }

    public void setPubDate(Instant pubDate) {
        this.pubDate = pubDate;
    }

    public void setPubDate(String pubDate) {
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        Instant dateTime = OffsetDateTime.parse(pubDate, formatter).toInstant();
        this.pubDate = dateTime;
    }
}
