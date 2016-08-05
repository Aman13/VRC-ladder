package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.CourtManager;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.TeamNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.MatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.MatchScheduler;
import ca.sfu.cmpt373.alpha.vrcladder.scores.ScoreCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.NewTeamIdListPayload;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.NewTradingMatchGroupsPayload;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.ScoreCardPayload;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.CourtSerializer;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.MatchGroupSerializer;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.ScoreCardSerializer;
import ca.sfu.cmpt373.alpha.vrcrest.security.RouteSignature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.eclipse.jetty.http.HttpStatus;
import org.hibernate.exception.ConstraintViolationException;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ca.sfu.cmpt373.alpha.vrcrest.datatransfer.JsonProperties.JSON_PROPERTY_SCORES;

public class MatchGroupRouter extends RestRouter {

    private MatchGroupManager matchGroupManager;
    private TeamManager teamManager;
    private CourtManager courtManager;

    public MatchGroupRouter(ApplicationManager applicationManager) {
        super(applicationManager);
        matchGroupManager = applicationManager.getMatchGroupManager();
        teamManager = applicationManager.getTeamManager();
        courtManager = applicationManager.getCourtManager();
    }

    private static final String PARAM_MATCHGROUP_ID = PARAM_ID + "matchGroupId";

    private static final String ROUTE_MATCHGROUP = "/matchgroup";

    public static final String ROUTE_MATCHGROUPS = "/matchgroups";
    public static final String ROUTE_MATCHGROUP_GENERATION = ROUTE_MATCHGROUPS + "/generate";
    public static final String ROUTE_MATCHGROUP_ID = ROUTE_MATCHGROUP + "/" + PARAM_ID;
    public static final String ROUTE_MATCHGROUP_SCORES = ROUTE_MATCHGROUP + "/" + PARAM_ID + "/scores";
    public static final String ROUTE_MATCH_SCHEDULE = ROUTE_MATCHGROUPS + "/schedule";
    public static final String ROUTE_MATCHGROUP_UPDATE_MATCHGROUP = ROUTE_MATCHGROUP + "/update/" + PARAM_MATCHGROUP_ID;
    public static final String ROUTE_MATCHGROUPS_TRADE_TEAMS = ROUTE_MATCHGROUPS + "/trade";

    private static final String JSON_PROPERTY_MATCHGROUPS = "matchGroups";
    private static final String JSON_PROPERTY_MATCHGROUP = "matchGroup";
    private static final String JSON_PROPERTY_COURTS = "courts";

    private static final String ERROR_NO_MATCHGROUP_FOUND = "There was no MatchGroup found for the given Id";
    private static final String ERROR_NO_TEAM_MATCHGROUP_FOUND = "The Team or the MatchGroup couldn't be found";

    @Override
    public void attachRoutes() {
        Spark.get(ROUTE_MATCHGROUPS, this::handleGetAllMatchGroups);
        Spark.get(ROUTE_MATCHGROUP_ID, this::handleGetMatchGroupById);
        Spark.get(ROUTE_MATCH_SCHEDULE, this::handleGetMatchSchedule);
        Spark.put(ROUTE_MATCHGROUP_SCORES, this::handleUpdateMatchGroupScores);
        Spark.delete(ROUTE_MATCHGROUP_ID, this::handleDeleteMatchGroup);
        Spark.post(ROUTE_MATCHGROUP_GENERATION, this::handleGenerateMatchGroups);
        Spark.put(ROUTE_MATCHGROUP_UPDATE_MATCHGROUP, this::handleUpdateMatchGroupTeams);
        Spark.get(ROUTE_MATCHGROUP_SCORES, this::handleGetMatchGroupScores);
        Spark.post(ROUTE_MATCHGROUPS_TRADE_TEAMS, this::handleSwapMatchGroupTeams);
    }

    @Override
    public List<RouteSignature> getPublicRouteSignatures() {
        return Collections.emptyList();
    }

    @Override
    protected Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(MatchGroup.class, new MatchGroupSerializer())
                .registerTypeAdapter(Court.class, new CourtSerializer())
                .registerTypeAdapter(ScoreCard.class, new ScoreCardSerializer())
                .registerTypeAdapter(ScoreCardPayload.class, new ScoreCardPayload.GsonDeserializer())
                .registerTypeAdapter(NewTeamIdListPayload.class, new NewTeamIdListPayload.GsonDeserializer())
                .registerTypeAdapter(NewTradingMatchGroupsPayload.class, new NewTradingMatchGroupsPayload.GsonDeserializer())
                .setPrettyPrinting()
                .create();
    }

    private String handleGetAllMatchGroups(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        List<MatchGroup> matchGroups = matchGroupManager.getAll();
        responseBody.add(JSON_PROPERTY_MATCHGROUPS, getGson().toJsonTree(matchGroups));
        return responseBody.toString();
    }

    private String handleGetMatchSchedule(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        List<Court> courts = courtManager.getAll();
        responseBody.add(JSON_PROPERTY_COURTS, getGson().toJsonTree(courts));
        return responseBody.toString();
    }

    private String handleGetMatchGroupById(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        try {
            String matchGroupIdParam = request.params(PARAM_ID);

            MatchGroup matchGroup = matchGroupManager.getById(new GeneratedId(matchGroupIdParam));
            responseBody.add(JSON_PROPERTY_MATCHGROUP, getGson().toJsonTree(matchGroup));
        } catch (EntityNotFoundException e) {
            response.status(HttpStatus.NOT_FOUND_404);
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NO_MATCHGROUP_FOUND);
        }
        return responseBody.toString();
    }

    private String handleGenerateMatchGroups(Request request, Response response) {
        checkForVolunteerRole(request);

        JsonObject responseBody = new JsonObject();
        try {
            courtManager.deleteAll();
            matchGroupManager.deleteAll();
            List<Team> teams = teamManager.getAll();
            List<Team> waitList = new ArrayList<>();
            List<Court> courts = MatchScheduler.scheduleMatches(
                    MatchScheduler.DEFAULT_NUM_COURTS,
                    MatchGroupGenerator.generateMatchGroupings(teams, waitList,  (MatchScheduler.DEFAULT_NUM_COURTS * MatchScheduler.DEFAULT_NUM_PLAYTIME)));
            for (Court court : courts) {
                for (MatchGroup matchGroup : court.getScheduledMatches().values()) {
                    matchGroupManager.create(matchGroup);
                }
                courtManager.create(court);
            }
        } catch (MatchMakingException e) {
            response.status(HttpStatus.BAD_REQUEST_400);
            responseBody.addProperty(JSON_PROPERTY_ERROR, e.getMessage());
        } catch (ConstraintViolationException e) {
            response.status(HttpStatus.CONFLICT_409);
            responseBody.addProperty(JSON_PROPERTY_ERROR, e.getMessage());
        }
        return responseBody.toString();
    }

    private String handleUpdateMatchGroupScores(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        try {
            String matchGroupIdParam = request.params(PARAM_ID);
            MatchGroup matchGroup = matchGroupManager.getById(new GeneratedId(matchGroupIdParam));

            ScoreCardPayload scoreCardPayload = getGson().fromJson(request.body(), ScoreCardPayload.class);

            //get ranked teams without querying the database again
            List<Team> matchGroupTeams = matchGroup.getTeams();
            List<Team> rankedTeams = new ArrayList<>();
            for (IdType teamId : scoreCardPayload.getTeamIds()) {
                for (Team team : matchGroupTeams) {
                    if (team.getId().equals(teamId)) {
                        rankedTeams.add(team);
                        break;
                    }
                }
            }

            //the ScoreCard will check if the teams are in the MatchGroup,
            //if all MatchGroup teams are in the ScoreCard,
            //and make sure there are no duplicate teams
            matchGroup.getScoreCard().setRankedTeams(rankedTeams);
            matchGroupManager.updateScoreCard(matchGroup);
        } catch (JsonSyntaxException e) {
            response.status(HttpStatus.BAD_REQUEST_400);
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
        } catch (JsonParseException | IllegalStateException e) {
            response.status(HttpStatus.BAD_REQUEST_400);
            responseBody.addProperty(JSON_PROPERTY_ERROR, e.getMessage());
        } catch (EntityNotFoundException e) {
            response.status(HttpStatus.NOT_FOUND_404);
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NO_MATCHGROUP_FOUND);
        } catch (ConstraintViolationException e) {
            response.status(HttpStatus.CONFLICT_409);
            responseBody.addProperty(JSON_PROPERTY_ERROR, e.getMessage());
        }
        return responseBody.toString();
    }

    private String handleGetMatchGroupScores(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        try {
            String matchGroupIdParam = request.params(PARAM_ID);
            MatchGroup matchGroup = matchGroupManager.getById(new GeneratedId(matchGroupIdParam));
            responseBody.add(JSON_PROPERTY_SCORES, getGson().toJsonTree(matchGroup.getScoreCard()));
        } catch (EntityNotFoundException e) {
            response.status(HttpStatus.NOT_FOUND_404);
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NO_MATCHGROUP_FOUND);
        } catch (IllegalStateException e) {
            //this occurs if there's no recorded scores in the ScoreCard
            //It's triggered by the ScoreCardSerializer when calling scoreCard.getRankedTeams()
            response.status(HttpStatus.BAD_REQUEST_400);
            responseBody.addProperty(JSON_PROPERTY_ERROR, e.getMessage());
        }
        return responseBody.toString();
    }

    private String handleDeleteMatchGroup(Request request, Response response) {
        checkForVolunteerRole(request);

        JsonObject responseBody = new JsonObject();
        try {
            String matchGroupIdParam = request.params(PARAM_ID);
            matchGroupManager.deleteById(new GeneratedId(matchGroupIdParam));
        } catch (EntityNotFoundException e) {
            response.status(HttpStatus.NOT_FOUND_404);
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NO_MATCHGROUP_FOUND);
            return responseBody.toString();
        }
        return responseBody.toString();
    }

    private String handleUpdateMatchGroupTeams(Request request, Response response) {
        checkForVolunteerRole(request);

        JsonObject responseBody = new JsonObject();
        try {
            String paramMatchGroupId1 = request.params(PARAM_MATCHGROUP_ID);

            NewTeamIdListPayload newTeamPayload = getGson().fromJson(request.body(), NewTeamIdListPayload.class);

            List<Team> newTeams = new ArrayList<>();
            for(GeneratedId teamId : newTeamPayload.getTeamIds()) {
                newTeams.add(teamManager.getById(teamId));
            }

            matchGroupManager.setTeamsInMatchGroup(new GeneratedId(paramMatchGroupId1), newTeams);
        } catch (EntityNotFoundException e) {
            response.status(HttpStatus.NOT_FOUND_404);
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NO_TEAM_MATCHGROUP_FOUND);
        } catch (IllegalStateException e) {
            //This occurs if the list of teams passed in is less than 3 or more than 4 in size
            response.status(HttpStatus.BAD_REQUEST_400);
            responseBody.addProperty(JSON_PROPERTY_ERROR, e.getMessage());
        }
        return responseBody.toString();
    }

    private String handleSwapMatchGroupTeams(Request request, Response response) {
        checkForVolunteerRole(request);

        JsonObject responseBody = new JsonObject();
        try {
            NewTradingMatchGroupsPayload newSwapPayload = getGson().fromJson(request.body(), NewTradingMatchGroupsPayload.class);

            GeneratedId firstMatchGroupId = newSwapPayload.getFirstMatchGroupId();
            GeneratedId secondMatchGroupId = newSwapPayload.getSecondMatchGroupId();
            GeneratedId firstTeamId = newSwapPayload.getFirstTeamId();
            GeneratedId secondTeamId = newSwapPayload.getSecondTeamId();

            Team teamToTrade1 = teamManager.getById(firstTeamId);
            Team teamToTrade2 = teamManager.getById(secondTeamId);
            matchGroupManager.tradeTeamsInMatchGroups(firstMatchGroupId, teamToTrade1, secondMatchGroupId, teamToTrade2);
        } catch (EntityNotFoundException e) {
            response.status(HttpStatus.NOT_FOUND_404);
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NO_TEAM_MATCHGROUP_FOUND);
        } catch (TeamNotFoundException e) {
            response.status(HttpStatus.BAD_REQUEST_400);
            responseBody.addProperty(JSON_PROPERTY_ERROR, e.getMessage());
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        }
        return responseBody.toString();
    }
}
