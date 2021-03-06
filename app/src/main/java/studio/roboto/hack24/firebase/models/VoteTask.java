package studio.roboto.hack24.firebase.models;

import java.util.Map;

public class VoteTask extends Task {

    public boolean yes;
    public String questionId;

    public VoteTask(boolean yes, String questionId) {
        this.yes = yes;
        this.questionId = questionId;
    }

    public VoteTask() {
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> result = super.toMap();

        result.put("yes", yes);
        result.put("questionId", questionId);

        return result;
    }

    @Override
    String getTaskType() {
        return "vote";
    }
}
