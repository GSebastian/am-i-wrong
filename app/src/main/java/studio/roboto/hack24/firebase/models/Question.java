package studio.roboto.hack24.firebase.models;

public class Question {

    public String id;
    public String text;
    public String name;
    public long timestamp;

    public Question(String id, String text, String name, long timestamp) {
        this.id = id;
        this.text = text;
        this.name = name;
        this.timestamp = timestamp;
    }

    public Question() {
    }
}
