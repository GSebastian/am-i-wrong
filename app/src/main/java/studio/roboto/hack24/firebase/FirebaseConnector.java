package studio.roboto.hack24.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import studio.roboto.hack24.Utils;
import studio.roboto.hack24.firebase.models.Comment;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.firebase.models.VoteTask;
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

    public static Task<Void> addQuestion(String questionText) {
        Question newQuestion = new Question(
                questionText,
                Utils.getTimestamp());

        DatabaseReference questionsNode =
                FirebaseManager
                        .sharedInstance
                        .databaseRef
                        .child("Questions");

        String newQuestionKey = questionsNode.push().getKey();

        return questionsNode.child(newQuestionKey).setValue(newQuestion.toMap());
    }

    public static DatabaseReference getQuestions() {
        return FirebaseManager
                .sharedInstance
                .databaseRef
                .child("Questions");
    }

    public static DatabaseReference getQuestion(String questionId) {
        return FirebaseManager
                .sharedInstance
                .databaseRef
                .child("Questions")
                .child(questionId);
    }

    public static void vote(String questionId, boolean yes) {
        VoteTask voteTask = new VoteTask(
                yes,
                questionId);

        DatabaseReference tasksNode =
                FirebaseManager
                        .sharedInstance
                        .databaseRef
                        .child("Queue")
                        .child("tasks");

        String newTaskKey = tasksNode.push().getKey();

        tasksNode.child(newTaskKey).setValue(voteTask.toMap());
    }

    private static DatabaseReference getRemoteNames() {
        return FirebaseManager.sharedInstance.databaseRef.child("Names");
    }
}
