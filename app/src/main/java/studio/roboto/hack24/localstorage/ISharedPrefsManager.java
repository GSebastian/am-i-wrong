package studio.roboto.hack24.localstorage;

import java.util.Set;

public interface ISharedPrefsManager {

    String INTENT_QUESTION_HIDDEN = "INTENT_QUESTION_HIDDEN";

    void setCurrentName(String newName);

    String getCurrentName();

    void addMyQuestionId(String questionId);

    Set<String> getMyQuestionIds();

    void addAnsweredQuestionId(String questionId, boolean wasYes);

    Set<String> getAnsweredQuestionsIds();

    boolean haveIAnsweredQuestion(String question);

    SharedPrefsManager.VOTED whatDidIAnswer(String question);

    void markQuestionAsRemoved(String questionId);

    boolean isQuestionRemoved(String questionId);

    boolean isThisMyQuestion(String questionId);

    long getLastQuestionTime();
}
