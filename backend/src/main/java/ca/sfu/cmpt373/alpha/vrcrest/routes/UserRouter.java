package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;

// TODO - Implement private route methods.
//      - Update buildGson method if necessary.
public class UserRouter extends RestRouter {

    public static final String ROUTE_USERS = "/users";
    public static final String ROUTE_USER_ID = "/user/:id";

    private UserManager userManager;
    private static ApplicationManager application = new ApplicationManager();


    public UserRouter(UserManager userManager) {
        super();
        this.userManager = userManager;
    }

    @Override
    public void attachRoutes() {
        Spark.get(ROUTE_USERS, (request, response) -> handleGetUsers(request, response));
        Spark.post(ROUTE_USERS, (request, response) -> handleCreateUser(request, response));
        Spark.get(ROUTE_USER_ID, (request, response) -> handleGetUserById(request, response));
        Spark.put(ROUTE_USER_ID, (request, response) -> handleUpdateUserById(request, response));
        Spark.put(ROUTE_USER_ID, (request, response) -> handleDeleteUserById(request, response));
    }

    @Override
    protected Gson buildGson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(User.class, new UserGsonSerializer());
        return gson.create();

    }


    private String handleGetUsers(Request request, Response response) {
        Gson userGson = new Gson();
        String json;
        try{
            json = userGson.toJson(application.getUserManager().getAll());
            return json;
        }catch (Exception e){
            e.printStackTrace();
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            return userGson.toJson("Unable to pull users:\n" + e.getMessage());
        }
    }


    private String handleCreateUser(Request request, Response response) {
        Gson userGson = new Gson();
        String json = request.body();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            User newUser = objectMapper.readValue(json, User.class);
            application.getUserManager().create(newUser);
            Log.info("New User has been created!");
            Log.info(userGson.toJson(newUser));
            Log.info(buildGson().toJson(newUser));
            return userGson.toJson(newUser);
        }catch (IOException e){
            e.printStackTrace();
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            return userGson.toJson("Unable to create user");
        }
    }

    private String handleGetUserById(Request request, Response response) {
        return null;
    }

    private String handleUpdateUserById(Request request, Response response) {
        return null;
    }

    private String handleDeleteUserById(Request request, Response response) {
        return null;
    }

}
