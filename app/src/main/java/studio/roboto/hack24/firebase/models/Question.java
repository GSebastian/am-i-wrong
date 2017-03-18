package studio.roboto.hack24.firebase.models;

import java.util.HashMap;
import java.util.Map;

public class Question {

    public String id;
    public String text;
    public String name;
    public long timestamp;

    public Question(String text, String name, long timestamp) {
        this.text = text;
        this.name = name;
        this.timestamp = timestamp;
    }

    public Question() {
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("text", text);
        result.put("name", name);
        result.put("time", timestamp);

        return result;
    }
}
