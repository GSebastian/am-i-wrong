package studio.roboto.hack24.firebase.models;

import java.util.HashMap;
import java.util.Map;

public class VoteTask {

    public boolean yes;
    public String questionId;

    public VoteTask(boolean yes, String questionId) {
        this.yes = yes;
        this.questionId = questionId;
    }

    public VoteTask() {
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("yes", yes);
        result.put("questionId", questionId);

        return result;
    }
}
