package RssReaderAPI.ResourceControllers;

import RssReaderAPI.DTO.RssDto;
import RssReaderAPI.DTO.RssFeedDto;
import RssReaderAPI.Exceptions.BadRequestException;
import RssReaderAPI.Exceptions.InternalServerError;
import RssReaderAPI.Exceptions.ResourceNotFoundException;
import RssReaderAPI.Services.RssFeedsService;
import com.google.gson.GsonBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/RssFeeds/feeds/")
public class RssFeedsController {
    private RssFeedsService rssFeedsService = RssFeedsService.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFeeds(@QueryParam("title") String title, @QueryParam("start") long start, @QueryParam("end") long end) throws ResourceNotFoundException, BadRequestException {
        List<RssFeedDto> rssFeeds;
        if(title == null){
            if(start == 0 && end == 0) {
                rssFeeds = rssFeedsService.getAllFeeds();
            } else {
                rssFeeds = rssFeedsService.getFeedsPage(start, end);
            }
        } else if(start == 0 && end == 0){
            rssFeeds = rssFeedsService.getFeedsByTitle(title);
        } else {
            rssFeeds = rssFeedsService.getFeedsPageByTitle(title, start, end);
        }
        GenericEntity<List<RssFeedDto>> feeds = new GenericEntity<List<RssFeedDto>>(rssFeeds){};
        return Response.ok(feeds).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{feedId}/news/")
    public Response getNewsPage(@PathParam("feedId") long feedId, @QueryParam("start") long start, @QueryParam("end") long end) throws ResourceNotFoundException, BadRequestException, InternalServerError {
        List<RssDto> rssDtos;
        if(start == 0 && end == 0){
            rssDtos = rssFeedsService.getAllNews(feedId);
        } else {
            rssDtos = rssFeedsService.getNewsPage(feedId, start, end);
        }
        GenericEntity<List<RssDto>> rss = new GenericEntity<List<RssDto>>(rssDtos){};
        return Response.ok(rss).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{feedId}/news/{newsId}/")
    public Response getSingleNews(@PathParam("feedId") long feedId, @PathParam("newsId") long rssId) throws ResourceNotFoundException, InternalServerError {
        RssDto rssDto = rssFeedsService.getSingleNews(feedId, rssId);
        return Response.ok(rssDto).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{feedId}/news/{newsId}/statistics")
    public Response getMostUsedWords(@PathParam("feedId") long feedId, @PathParam("newsId") long rssId) throws ResourceNotFoundException, InternalServerError {
        int count = 5;
        String[] mostUsedWords = rssFeedsService.getMostUsedWords(feedId, rssId, count);
        String words = new GsonBuilder().create().toJson(mostUsedWords);
        return Response.ok(words).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFeed(RssFeedDto rssFeedDto){
        rssFeedsService.addFeed(rssFeedDto);
        return Response.ok().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateFeed(@QueryParam("ID") long feed_id, RssFeedDto rssFeed) throws ResourceNotFoundException {
        rssFeedsService.updateFeed(feed_id, rssFeed);
        return Response.ok().build();
    }

    @DELETE
    public Response deleteFeed(@QueryParam("ID") long id) throws ResourceNotFoundException {
        rssFeedsService.deleteFeed(id);
        return Response.ok().build();
    }
}
