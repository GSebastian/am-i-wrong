package studio.roboto.hack24.firebase;

import com.google.firebase.database.DatabaseReference;

import studio.roboto.hack24.Utils;
import studio.roboto.hack24.firebase.models.Comment;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.localstorage.SharedPrefsManager;

public class FirebaseConnector {

    public static void addComment(String questionId, String commentText, boolean saidYes) {
        Comment comment = new Comment(
                commentText,
                Utils.getTimestamp(),
                SharedPrefsManager.sharedInstance.getCurrentName(),
                saidYes);

        DatabaseReference commentsForQuestionNode =
                FirebaseManager
                        .sharedInstance
                        .databaseRef
                        .child("Comments")
                        .child(questionId);

        String newCommentNodeKey = commentsForQuestionNode.push().getKey();

        commentsForQuestionNode.child(newCommentNodeKey).setValue(comment.toMap());
    }

    public static DatabaseReference getComments(String questionId) {
        return FirebaseManager
                .sharedInstance
                .databaseRef
                .child("Comments")
                .child(questionId);
    }

    public static void addQuestion(String questionText) {
        Question newQuestion = new Question(
                questionText,
                SharedPrefsManager.sharedInstance.getCurrentName(),
                Utils.getTimestamp());

        DatabaseReference questionsNode =
                FirebaseManager
                        .sharedInstance
                        .databaseRef
                        .child("Questions");

        String newQuestionKey = questionsNode.push().getKey();

        questionsNode.child(newQuestionKey).setValue(newQuestion.toMap());
    }

    public static DatabaseReference getQuestions() {
        return FirebaseManager
                .sharedInstance
                .databaseRef
                .child("Questions");
    }

    public static void voteYes(String questionId) {
        // Create a "YES" task
    }

    public static void voteNo(String questionId) {
        // Create a "NO" task
    }

    private static DatabaseReference getRemoteNames() {
        return FirebaseManager.sharedInstance.databaseRef.child("Names");
    }
}
