package rssreader.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "RSSREADER", name = "RssFeeds")
public class RssFeedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feed_id", unique = true, nullable = false)
    private long id;
    @Column(name = "feed_title")
    private String title;
    @Column(name = "feed_link")
    private String link;
    @Column(name = "lastUpdate")
    private Instant lastUpdate;
    @OneToMany(mappedBy = "rssFeed", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RssNewsItemEntity> news = new HashSet<>();

    public RssFeedEntity() {
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

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<RssNewsItemEntity> getNews(){
        return this.news;
    }

    public void setNews(Set<RssNewsItemEntity> news){
        this.news = news;
    }

    public void addNews(RssNewsItemEntity news){
        news.setRssFeed(this);
        this.news.add(news);
    }
}
