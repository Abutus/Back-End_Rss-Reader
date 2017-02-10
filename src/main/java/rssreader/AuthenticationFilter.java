package rssreader;

import org.glassfish.jersey.internal.util.Base64;

import javax.annotation.security.PermitAll;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter{
    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHORIZATION_SCHEME = "Basic";
    private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED).entity("You cannot access this resource").build();


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();

        if(!method.isAnnotationPresent(PermitAll.class)){
            MultivaluedMap<String, String> headers = requestContext.getHeaders();
            List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

            if(authorization == null || authorization.isEmpty()){
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }

            String encodedUserAndPassword = authorization.get(0).replaceFirst(AUTHORIZATION_SCHEME + " ", "");
            String userAndPassword = new String(Base64.decode(encodedUserAndPassword.getBytes()));

            StringTokenizer tokenizer = new StringTokenizer(userAndPassword, ":");
            String username = tokenizer.nextToken();
            String password = tokenizer.nextToken();

            if(!isUserAllowed(username, password)){
                requestContext.abortWith(ACCESS_DENIED);
            }
        }
    }

    private boolean isUserAllowed(String username, String password){
        boolean isAllowed =  false;

        if(username.equals("Admin") && password.equals("Admin")){
            isAllowed = true;
        }

        return isAllowed;
    }
}
