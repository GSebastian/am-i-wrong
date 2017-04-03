package studio.roboto.hack24.localstorage;

import java.util.List;
import java.util.Set;

public interface ISharedPrefsManager {

    public static final String INTENT_QUESTION_HIDDEN = "INTENT_QUESTION_HIDDEN";

    public void setCurrentName(String newName);

    public String getCurrentName();

    public void addMyQuestionId(String questionId);

    public Set<String> getMyQuestionIds();

    public void addAnsweredQuestionId(String questionId, boolean wasYes);

    public Set<String> getAnsweredQuestionsIds();

    public boolean haveIAnsweredQuestion(String question);

    public SharedPrefsManager.VOTED whatDidIAnswer(String question);

    public void markQuestionAsRemoved(String questionId);

    public boolean isQuestionRemoved(String questionId);

    public boolean isThisMyQuestion(String questionId);
}
