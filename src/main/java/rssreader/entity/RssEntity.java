package rssreader.entity;


import javax.persistence.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(schema = "RSSREADER", name = "News")
public class RssEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", referencedColumnName = "feed_id")
    private RssFeedEntity rssFeed;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "news_id", unique = true, nullable = false)
    private long id;
    @Column(name = "news_title")
    private String title;
    @Column(name = "news_link")
    private String link;
    @Column(name = "news_description")
    private String description;
    @Column(name = "news_guid")
    private String guid;
    @Column(name = "news_pubDate")
    private Instant pubDate;

    public RssEntity() {
    }

    public RssFeedEntity getRssFeed() {
        return rssFeed;
    }

    public void setRssFeed(RssFeedEntity rssFeed) {
        this.rssFeed = rssFeed;
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
