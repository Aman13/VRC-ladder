package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.DuplicatePropertyException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PropertyInstantiationException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.TemplateNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;
import ca.sfu.cmpt373.alpha.vrcladder.notifications.NotificationManager;
import ca.sfu.cmpt373.alpha.vrcladder.notifications.logic.NotificationType;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.Password;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.SecurityManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.JsonProperties;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.NewUserPayload;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.UpdateUserPayload;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.PlayerGsonSerializer;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.TeamGsonSerializer;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.UserGsonSerializer;
import ca.sfu.cmpt373.alpha.vrcrest.security.RouteSignature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.eclipse.jetty.http.HttpStatus;
import org.hibernate.exception.ConstraintViolationException;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.route.HttpMethod;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ca.sfu.cmpt373.alpha.vrcrest.routes.TeamRouter.JSON_PROPERTY_TEAMS;

public class UserRouter extends RestRouter {

    public static final String ROUTE_USERS = "/users";
    public static final String ROUTE_PLAYERS = "/players";

    public static final String ROUTE_USERS_SELF = ROUTE_USERS + "/self";
    public static final String ROUTE_USERS_SELF_TEAMS = ROUTE_USERS_SELF + "/teams";

    public static final String ROUTE_USER_ID = "/user/" + PARAM_ID;
    private static final String ROUTE_USER_ID_TEAMS = ROUTE_USER_ID + "/teams";

    public static final String JSON_PROPERTY_USERS = "users";
    public static final String JSON_PROPERTY_USER = "user";
    public static final String JSON_PROPERTY_PLAYERS = "players";

    private static final String ERROR_NONEXISTENT_USER = "This user does not exist.";
    private static final String ERROR_INVALID_PROPERTY = "The following property is invalid: ";

    private static final List<RouteSignature> PUBLIC_ROUTE_SIGNATURES = createPublicRouteSignatures();

    private final NotificationManager notify;
    private SecurityManager securityManager;
    private UserManager userManager;
    private TeamManager teamManager;
    private Gson playerGson;

    public UserRouter(ApplicationManager applicationManager) {
        super(applicationManager);
        securityManager = applicationManager.getSecurityManager();
        userManager = applicationManager.getUserManager();
        teamManager = applicationManager.getTeamManager();
        playerGson = buildGsonForPlayer();
        notify = new NotificationManager();
    }

    @Override
    public void attachRoutes() {
        Spark.post(ROUTE_USERS, this::handleCreateUser);

        Spark.get(ROUTE_USERS, this::handleGetUsers);
        Spark.get(ROUTE_PLAYERS, this::handleGetPlayers);

        Spark.get(ROUTE_USERS_SELF, this::handleGetActiveUser);
        Spark.put(ROUTE_USERS_SELF, this::handleUpdateActiveUser);
        Spark.get(ROUTE_USERS_SELF_TEAMS, this::handleGetAllActiveUserTeams);
        Spark.delete(ROUTE_USERS_SELF, this::handleDeleteActiveUser);

        Spark.get(ROUTE_USER_ID, this::handleGetUserById);
        Spark.put(ROUTE_USER_ID, this::handleUpdateUserById);
        Spark.get(ROUTE_USER_ID_TEAMS, this::handleGetAllTeamsByUserId);
        Spark.delete(ROUTE_USER_ID, this::handleDeleteUserById);
    }

    @Override
    public List<RouteSignature> getPublicRouteSignatures() {
        return PUBLIC_ROUTE_SIGNATURES;
    }

    private static List<RouteSignature> createPublicRouteSignatures() {
        List<RouteSignature> routeSignatures = new ArrayList<>();

        RouteSignature createUserSignature = new RouteSignature(ROUTE_USERS, HttpMethod.post);
        routeSignatures.add(createUserSignature);

        return Collections.unmodifiableList(routeSignatures);
    }

    @Override
    protected Gson buildGson() {
        return new GsonBuilder()
            .registerTypeAdapter(Team.class, new TeamGsonSerializer())
            .registerTypeAdapter(User.class, new UserGsonSerializer())
            .registerTypeAdapter(NewUserPayload.class, new NewUserPayload.GsonDeserializer())
            .registerTypeAdapter(UpdateUserPayload.class, new UpdateUserPayload.GsonDeserializer())
            .setPrettyPrinting()
            .create();
    }

    private Gson buildGsonForPlayer() {
        return new GsonBuilder()
            .registerTypeAdapter(User.class, new PlayerGsonSerializer())
            .setPrettyPrinting()
            .create();
    }

    private String handleGetUsers(Request request, Response response) {
        checkForVolunteerRole(request);

        JsonObject responseBody = new JsonObject();

        List<User> users = userManager.getAll();
        responseBody.add(JSON_PROPERTY_USERS, getGson().toJsonTree(users));
        response.status(HttpStatus.OK_200);

        return responseBody.toString();
    }

    private String handleGetPlayers(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        List<User> players = userManager.getAllPlayers();
        responseBody.add(JSON_PROPERTY_PLAYERS, playerGson.toJsonTree(players));
        response.status(HttpStatus.OK_200);

        return responseBody.toString();
    }

    private String handleCreateUser(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        try {
            NewUserPayload newUserPayload = getGson().fromJson(request.body(), NewUserPayload.class);
            if (newUserPayload.getUserRole() == UserRole.VOLUNTEER) {
                checkForVolunteerRole(request);
            }

            Password hashedPassword = securityManager.hashPassword(newUserPayload.getPassword());

            User newUser = userManager.create(
                newUserPayload.getUserId(),
                newUserPayload.getUserRole(),
                newUserPayload.getFirstName(),
                newUserPayload.getMiddleName(),
                newUserPayload.getLastName(),
                newUserPayload.getEmailAddress(),
                newUserPayload.getPhoneNumber(),
                hashedPassword);

            JsonElement jsonUser = getGson().toJsonTree(newUser);
            responseBody.add(JSON_PROPERTY_USER, jsonUser);
            response.status(HttpStatus.CREATED_201);

            notify.notifyUser(newUser, NotificationType.ACCOUNT_ACTIVATED);
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonParseException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        }  catch (PropertyInstantiationException ex){
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_INVALID_PROPERTY);
            responseBody.addProperty(ex.getMessage(), PersistenceConstants.INVALID_PROPERTY_VALUE);
            response.status(HttpStatus.UNPROCESSABLE_ENTITY_422);
        }
        catch (DuplicatePropertyException ex) {
            String duplicatPropertyName = ex.getPropertyName();
            if(duplicatPropertyName.equals(PersistenceConstants.COLUMN_ID)){
                responseBody.addProperty(JSON_PROPERTY_ERROR, PersistenceConstants.EXISTING_USER_ID);
                responseBody.addProperty(JsonProperties.JSON_PROPERTY_USER_ID, false);
            } else if (duplicatPropertyName.equals(PersistenceConstants.COLUMN_EMAIL_ADDRESS)){
                responseBody.addProperty(JSON_PROPERTY_ERROR, PersistenceConstants.EXISTING_USER_EMAIL);
                responseBody.addProperty(JsonProperties.JSON_PROPERTY_EMAIL_ADDRESS, false);
            }

            response.status(HttpStatus.CONFLICT_409);
        } catch (TemplateNotFoundException ex) {
            responseBody.addProperty(PersistenceConstants.NOTIFICATION, ERROR_NOTIFICATION_FAILED);
        }

        return responseBody.toString();
    }

    private String handleGetActiveUser(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        try {
            User activeUser = extractUserFromRequest(request);

            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(activeUser));
            response.status(HttpStatus.OK_200);
        } catch (ValidationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_INVALID_RESOURCE_ID);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        }

        return responseBody.toString();
    }

    private String handleUpdateActiveUser(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        try {
            UserId activeUserId = extractUserIdFromRequest(request);
            User updatedUser = updateUserWithPayload(request, activeUserId);

            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(updatedUser));
            response.status(HttpStatus.OK_200);
        } catch (ValidationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_INVALID_RESOURCE_ID);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonParseException | IllegalArgumentException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        }

        return responseBody.toString();
    }

    private String handleGetUserById(Request request, Response response) {
        checkForVolunteerRole(request);

        JsonObject responseBody = new JsonObject();

        try {
            String requestedId = request.params(PARAM_ID);
            UserId userId = new UserId(requestedId);

            User existingUser = userManager.getById(userId);
            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(existingUser));
            response.status(HttpStatus.OK_200);
        } catch (ValidationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_INVALID_RESOURCE_ID);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        }

        return responseBody.toString();
    }

    private String handleUpdateUserById(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        try {
            String requestedId = request.params(PARAM_ID);
            UserId userId = new UserId(requestedId);

            UpdateUserPayload updateUserPayload = getGson().fromJson(request.body(), UpdateUserPayload.class);
            Password plainTextPassWord = updateUserPayload.getPassword();
            String passwordHash = plainTextPassWord.getHash();
            User existingUser = userManager.update(
                    userId,
                    updateUserPayload.getFirstName(),
                    updateUserPayload.getMiddleName(),
                    updateUserPayload.getLastName(),
                    updateUserPayload.getEmailAddress(),
                    updateUserPayload.getPhoneNumber(),
                    securityManager.hashPassword(passwordHash));


            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(existingUser));
            response.status(HttpStatus.OK_200);
        } catch (ValidationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonParseException | IllegalArgumentException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (PropertyInstantiationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_INVALID_PROPERTY);
            responseBody.addProperty(ex.getMessage(), PersistenceConstants.INVALID_PROPERTY_VALUE);
            response.status(HttpStatus.UNPROCESSABLE_ENTITY_422);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (ConstraintViolationException ex) {
            if(ex.getConstraintName().contains(PersistenceConstants.CONSTRAINT_CONFLICT_ID)){
                responseBody.addProperty(JSON_PROPERTY_ERROR, PersistenceConstants.EXISTING_USER_ID);
                responseBody.addProperty(JsonProperties.JSON_PROPERTY_USER_ID, false);
            }else if(ex.getConstraintName().contains(PersistenceConstants.CONSTRAINT_CONFLICT_EMAIL)){
                responseBody.addProperty(JSON_PROPERTY_ERROR, PersistenceConstants.EXISTING_USER_EMAIL);
                responseBody.addProperty(JsonProperties.JSON_PROPERTY_EMAIL_ADDRESS, false);
            }

            response.status(HttpStatus.CONFLICT_409);
        }

        return responseBody.toString();
    }

    private String handleDeleteUserById(Request request, Response response) {
        checkForVolunteerRole(request);

        JsonObject responseBody = new JsonObject();
        try {
            String requestedId = request.params(PARAM_ID);
            UserId userId = new UserId(requestedId);

            User deletedUser = userManager.deleteById(userId);
            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(deletedUser));
            response.status(HttpStatus.OK_200);
        } catch (ValidationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        }

        return responseBody.toString();
    }

    private String handleDeleteActiveUser(Request request, Response response){
        JsonObject responseBody = new JsonObject();
        try {
            User userToDelete = extractUserFromRequest(request);
            userManager.delete(userToDelete);
            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(userToDelete));
            response.status(HttpStatus.OK_200);
        } catch (ValidationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        }

        return responseBody.toString();
    }

    private String handleGetAllTeamsByUserId(Request request, Response response) {
        checkForVolunteerRole(request);

        JsonObject responseBody = new JsonObject();
        String userIdParam = request.params(PARAM_ID);
        User user = userManager.getById(new UserId(userIdParam));

        List<Team> teamsForUser = teamManager.getTeamsForUser(user);
        responseBody.add(JSON_PROPERTY_TEAMS, getGson().toJsonTree(teamsForUser));
        return responseBody.toString();
    }

    private String handleGetAllActiveUserTeams(Request request, Response response){
        JsonObject responseBody = new JsonObject();
        User user = extractUserFromRequest(request);;

        List<Team> teamsForUser = teamManager.getTeamsForUser(user);
        responseBody.add(JSON_PROPERTY_TEAMS, getGson().toJsonTree(teamsForUser));
        return responseBody.toString();
    }

    private User updateUserWithPayload(Request request, UserId userId) {
        UpdateUserPayload updateUserPayload = getGson().fromJson(request.body(), UpdateUserPayload.class);
        Password plainTextPassWord = updateUserPayload.getPassword();
        String passwordHash = plainTextPassWord.getHash();
        User updatedUser = userManager.update(
            userId,
            updateUserPayload.getFirstName(),
            updateUserPayload.getMiddleName(),
            updateUserPayload.getLastName(),
            updateUserPayload.getEmailAddress(),
            updateUserPayload.getPhoneNumber(),
            securityManager.hashPassword(passwordHash)

        );

        return updatedUser;
    }

}
