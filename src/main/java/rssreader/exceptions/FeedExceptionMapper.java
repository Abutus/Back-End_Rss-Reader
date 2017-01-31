package rssreader.exceptions;

import rssreader.dto.ErrorDto;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FeedExceptionMapper implements ExceptionMapper<Exception>{

    @Override
    public Response toResponse(Exception e) {
        Response.Status status;
        String message;

        if(e instanceof ResourceNotFoundException){
            status = Response.Status.NOT_FOUND;
            message = e.getMessage();
        } else if(e instanceof BadRequestException) {
            status = Response.Status.BAD_REQUEST;
            message = e.getMessage();
        } else if(e instanceof InternalServerError){
            status = Response.Status.INTERNAL_SERVER_ERROR;
            message = e.getMessage();
        } else {
            status = Response.Status.BAD_REQUEST;
            message = "Bad Request";
        }

        ErrorDto errorDto = new ErrorDto(status.getStatusCode(), message);

        return Response.status(status).entity(errorDto).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
