package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses;

import ca.sfu.cmpt373.alpha.vrcladder.scores.ScoreCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.JsonProperties;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

public class ScoreCardSerializer implements JsonSerializer<ScoreCard> {


    @Override
    public JsonElement serialize(ScoreCard scoreCard, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonScoreCard = new JsonObject();
        if (!scoreCard.isFilledOut()) {
            return jsonScoreCard;
        }
        List<Team> rankedTeams = scoreCard.getRankedTeams();
        for (int i = 0; i < rankedTeams.size(); i++) {
            int teamNumber = i + 1;
            Team currTeam = rankedTeams.get(i);
            jsonScoreCard.addProperty(
                    JsonProperties.JSON_PROPERTY_TEAM_ID + teamNumber,
                    currTeam.getId().toString());
        }
        return jsonScoreCard;
    }
}
