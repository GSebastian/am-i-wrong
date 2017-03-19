package studio.roboto.hack24.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import studio.roboto.hack24.Utils;
import studio.roboto.hack24.firebase.models.Comment;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.firebase.models.VoteTask;
import studio.roboto.hack24.localstorage.SharedPrefsManager;
import studio.roboto.hack24.questions.OnQuestionAddedListener;

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

    public static void addQuestion(String questionText, @Nullable final OnQuestionAddedListener listener) {
        Question newQuestion = new Question(
                questionText,
                Utils.getTimestamp());

        DatabaseReference questionsNode =
                FirebaseManager
                        .sharedInstance
                        .databaseRef
                        .child("Questions");

        final String newQuestionKey = questionsNode.push().getKey();

        questionsNode
                .child(newQuestionKey)
                .setValue(newQuestion.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (listener != null) {
                            listener.questionAdded(newQuestionKey);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (listener != null) {
                            listener.questionAddFailed();
                        }
                    }
                });
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

    public static DatabaseReference getRemoteNames() {
        return FirebaseManager.sharedInstance.databaseRef.child("Names");
    }
}
