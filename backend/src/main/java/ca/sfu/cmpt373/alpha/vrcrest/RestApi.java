package ca.sfu.cmpt373.alpha.vrcrest;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcrest.routes.RestRouter;
import ca.sfu.cmpt373.alpha.vrcrest.security.RouteSignature;
import com.google.gson.JsonObject;
import org.apache.shiro.authz.AuthorizationException;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Spark;

import java.util.List;

public class RestApi {

    public static final String ROUTE_WILDCARD = "/*";

    public static final String HEADER_ACCESS = "Access-Control-Allow-Origin";
    public static final String HEADER_ACCESS_VALUE = "*";

    public static final String JSON_RESPONSE_TYPE = "application/json";

    private static final String ERROR_MISSING_AUTHORIZATION_TOKEN = "The request is missing the Authorization header.";

    private ApplicationManager appManager;
    private List<RestRouter> routers;

    public RestApi(ApplicationManager appManager, List<RestRouter> routers) {
        this.appManager = appManager;
        this.routers = routers;
        initialize();
    }

    public void shutDown() {
        Spark.stop();
        appManager.shutDown();
    }

    private void initialize() {
        configure();
        attachRouters();
        attachAuthorization();
        attachResponseFilters();
    }

    private void configure() {
        // TODO - Add specific configurations for Spark.
    }

    private void attachRouters() {
        routers.forEach(RestRouter::attachRoutes);
    }

    private void attachAuthorization() {
        Spark.before(ROUTE_WILDCARD, (request, response) -> {
          if (request.requestMethod().equals("OPTIONS")) {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            Spark.halt(HttpStatus.OK_200, "");
          }
        });
        Spark.before(ROUTE_WILDCARD, (request, response) -> {
            RouteSignature routeSignature = new RouteSignature(request);

            try {
                for (RestRouter currentRouter : routers) {
                    List<RouteSignature> currentRouterSignatures = currentRouter.getPublicRouteSignatures();
                    if (currentRouterSignatures.contains(routeSignature)) {
                        return;
                    }
                }

                String authorizationToken = extractAuthorizationToken(request);
                appManager.getSecurityManager().parseToken(authorizationToken);

            } catch (AuthorizationException ex) {
                JsonObject responseBody = new JsonObject();
                responseBody.addProperty(RestRouter.JSON_PROPERTY_ERROR, ex.getMessage());
                response.header(HEADER_ACCESS, HEADER_ACCESS_VALUE);
                response.type(JSON_RESPONSE_TYPE);
                Spark.halt(HttpStatus.UNAUTHORIZED_401, responseBody.toString());
            }

        });
    }

    private String extractAuthorizationToken(Request request) {
        String authorizationHeader = request.headers(RestRouter.HEADER_AUTHORIZATION);
        if (authorizationHeader == null) {
            throw new AuthorizationException(ERROR_MISSING_AUTHORIZATION_TOKEN);
        }

        return authorizationHeader;
    }

    private void attachResponseFilters() {
        Spark.after(ROUTE_WILDCARD, (request, response) -> {
            response.header(HEADER_ACCESS, HEADER_ACCESS_VALUE);
            response.type(JSON_RESPONSE_TYPE);
        });
    }

}
