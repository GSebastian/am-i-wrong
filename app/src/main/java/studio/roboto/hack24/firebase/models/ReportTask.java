package studio.roboto.hack24.firebase.models;

import java.util.Map;

/**
 * Created by sebastian on 27/03/2017.
 */

public class ReportTask extends Task {

    public String questionId;

    public ReportTask(String questionId) {
        this.questionId = questionId;
    }

    public ReportTask() {
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> result = super.toMap();

        result.put("questionId", questionId);

        return result;
    }

    @Override
    String getTaskType() {
        return "report";
    }
}
