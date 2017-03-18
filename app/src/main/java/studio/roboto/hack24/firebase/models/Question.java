package studio.roboto.hack24.firebase.models;

import java.util.HashMap;
import java.util.Map;

public class Question {

    public String id;
    public String text;
    public long timestamp;
    public long yes;
    public long no;

    public Question(String text, long timestamp) {
        this(text, timestamp, 0, 0);
    }

    public Question(String text, long timestamp, long yes, long no) {
        this.text = text;
        this.timestamp = timestamp;
        this.yes = yes;
        this.no = no;
    }

    public Question() {

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("text", text);
        result.put("time", timestamp);
        result.put("yes", yes);
        result.put("no", no);

        return result;
    }
}
