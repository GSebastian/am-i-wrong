package studio.roboto.hack24.firebase;

import studio.roboto.hack24.firebase.models.Question;

public class FirebaseConnector {

    public static void addComment(String comment) {
        FirebaseManager.sharedInstance.databaseRef.child("Comments").push().setValue("ABCD");
    }

    public static void voteYes(Question question) {

    }

    public static void voteNo(Question question) {

    }
}
