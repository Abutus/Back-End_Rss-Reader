package rssreader.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RssFeedDto {
    private long id;
    private String title;
    private String link;

    public RssFeedDto() {
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
}
