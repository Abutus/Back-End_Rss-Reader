package RssReaderAPI.ResourceControllers;

import RssReaderAPI.DTO.RssDto;
import RssReaderAPI.DTO.RssFeedDto;
import RssReaderAPI.Exceptions.FeedNotFoundException;
import RssReaderAPI.Services.RssFeedsService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.json.JsonArray;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/RssFeeds")
public class RssFeedsController {
    private RssFeedsService rssFeedsService = RssFeedsService.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{title}:{start}-{end}")
    public Response getFeedsPage(@PathParam("title") String title, @PathParam("start") long start, @PathParam("end") long end) throws FeedNotFoundException {
        List<RssFeedDto> rssFeeds = rssFeedsService.getFeedsByTitle(title, start, end);
        GenericEntity<List<RssFeedDto>> feeds = new GenericEntity<List<RssFeedDto>>(rssFeeds){};
        return Response.ok(feeds).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{feedId}/{start}-{end}")
    public Response getRssPage(@PathParam("feedId") long feedId, @PathParam("start") long start, @PathParam("end") long end) throws FeedNotFoundException {
        List<RssDto> rssDtos = rssFeedsService.getRssByFeed(feedId, start, end);
        GenericEntity<List<RssDto>> rss = new GenericEntity<List<RssDto>>(rssDtos){};
        return Response.ok(rss).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{feedId}/{rssId}")
    public Response getSingleRss(@PathParam("feedId") long feedId, @PathParam("rssId") long rssId) throws FeedNotFoundException {
        RssDto rssDto = rssFeedsService.getRssFromFeedById(feedId, rssId);
        return Response.ok(rssDto).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getMostUsedWords/{feedId}/{rssId}")
    public Response getMostUsedWords(@PathParam("feedId") long feedId, @PathParam("rssId") long rssId) throws FeedNotFoundException {
        String[] mostUsedWords = rssFeedsService.getMostUsedWords(feedId, rssId);
        String words = new GsonBuilder().create().toJson(mostUsedWords);
        return Response.ok(words).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add")
    public Response addFeed(RssFeedDto rssFeedDto){
        rssFeedsService.addFeed(rssFeedDto);
        return Response.ok().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/update/{id}")
    public Response updateFeed(@PathParam("id") long id, RssFeedDto rssFeed) throws FeedNotFoundException {
        rssFeedsService.updateFeed(id, rssFeed);
        return Response.ok().build();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteFeed(@PathParam("id") long id) throws FeedNotFoundException {
        rssFeedsService.deleteFeed(id);
        return Response.ok().build();
    }
}
