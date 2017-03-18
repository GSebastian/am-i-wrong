package studio.roboto.hack24.firebase.models;

import java.util.HashMap;
import java.util.Map;

public class Comment {

    public String id;
    public String text;
    public String name;
    public boolean yes;
    public long timestamp;

    public Comment(String text, long timestamp, String name, boolean yes) {
        this.text = text;
        this.timestamp = timestamp;
        this.name = name;
        this.yes = yes;
    }

    public Comment() {
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("text", text);
        result.put("time", timestamp);
        result.put("name", name);
        result.put("yes", yes);

        return result;
    }
}
