package RssReaderAPI.DTO;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(schema = "RSSREADER", name = "RssFeeds")
public class RssFeedDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feed_id", unique = true, nullable = false)
    private long id;
    @Column(name = "feed_title")
    private String title;
    @Column(name = "feed_link")
    private String link;

    public RssFeedDto() {
    }

    public RssFeedDto(long id, String title, String link) {
        this.id = id;
        this.title = title;
        this.link = link;
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
