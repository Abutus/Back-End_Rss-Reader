package rssreader.resourcecontrollers;

import com.google.gson.GsonBuilder;
import com.sun.jersey.multipart.FormDataParam;
import rssreader.dto.RssNewsItemDto;
import rssreader.dto.RssFeedDto;
import rssreader.exceptions.BadRequestException;
import rssreader.exceptions.InternalServerError;
import rssreader.exceptions.ResourceNotFoundException;
import rssreader.services.RssFeedsService;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
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
        List<RssNewsItemDto> rssNewsItemDtos;
        if(start == 0 && end == 0){
            rssNewsItemDtos = rssFeedsService.getAllNews(feedId);
        } else {
            rssNewsItemDtos = rssFeedsService.getNewsPage(feedId, start, end);
        }
        GenericEntity<List<RssNewsItemDto>> rss = new GenericEntity<List<RssNewsItemDto>>(rssNewsItemDtos){};
        return Response.ok(rss).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{feedId}/news/{newsId}/")
    public Response getSingleNews(@PathParam("feedId") long feedId, @PathParam("newsId") long rssId) throws ResourceNotFoundException, InternalServerError {
        RssNewsItemDto rssNewsItemDto = rssFeedsService.getSingleNews(feedId, rssId);
        return Response.ok(rssNewsItemDto).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{feedId}/news/{newsId}/statistics")
    public Response getMostUsedWords(@PathParam("feedId") long feedId, @PathParam("newsId") long rssId) throws ResourceNotFoundException, InternalServerError {
        int count = 5;
        List<String> mostUsedWords = rssFeedsService.getMostUsedWords(feedId, rssId, count);
        String words = new GsonBuilder().create().toJson(mostUsedWords.toArray());
        return Response.ok(words).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFeed(RssFeedDto rssFeedDto){
        rssFeedsService.addFeed(rssFeedDto);
        return Response.status(Response.Status.CREATED).entity(rssFeedDto).build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("addFeeds/")
    public Response addFeeds(@FormDataParam("file") InputStream inputStream) throws InternalServerError {
        List<RssFeedDto> rssFeeds = rssFeedsService.addFeeds(inputStream);
        GenericEntity<List<RssFeedDto>> feeds = new GenericEntity<List<RssFeedDto>>(rssFeeds){};
        return Response.status(Response.Status.CREATED).entity(feeds).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateFeed(RssFeedDto rssFeed) throws ResourceNotFoundException {
        rssFeedsService.updateFeed(rssFeed);
        return Response.ok().build();
    }

    @DELETE
    public Response deleteFeed(@QueryParam("id") long id) throws ResourceNotFoundException {
        rssFeedsService.deleteFeed(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
