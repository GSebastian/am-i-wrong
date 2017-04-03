package studio.roboto.hack24.localstorage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPrefsManager implements ISharedPrefsManager {

    public static SharedPrefsManager sharedInstance = null;

    private static final String KEY_NAME = "name";
    private static final String KEY_MY_QUESTION_IDS = "my-question-ids";
    private static final String KEY_ANSWERED_QUESTION_IDS = "answered-question-ids";
    private static final String KEY_HIDE_QUESTIONID = "hide-questions";
    private static final String KEY_ANSWERED_QUESTION_ID_YESNO = "answered-question-id-yesno-";

    public enum VOTED {
        UNANSWERED,
        YES,
        NO
    }

    private Context mContext;
    private SharedPreferences mPrefs;

    public static void init(Context context) {
        if (SharedPrefsManager.sharedInstance == null) {
            SharedPrefsManager.sharedInstance = new SharedPrefsManager(context);
        }
    }

    public SharedPrefsManager(Context context) {
        this.mContext = context;
        this.mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public void setCurrentName(String newName) {
        mPrefs
                .edit()
                .putString(KEY_NAME, newName)
                .apply();
    }

    @Override
    public String getCurrentName() {
        return mPrefs.getString(KEY_NAME, "Anonymous");
    }

    @Override
    public void addMyQuestionId(String questionId) {
        Set<String> myQuestionIds = mPrefs.getStringSet(KEY_MY_QUESTION_IDS, new HashSet<String>());
        myQuestionIds.add(questionId);

        mPrefs
                .edit()
                .putStringSet(KEY_MY_QUESTION_IDS, myQuestionIds)
                .apply();
    }

    @Override
    public Set<String> getMyQuestionIds() {
        return mPrefs.getStringSet(KEY_MY_QUESTION_IDS, new HashSet<String>());
    }

    @Override
    public void addAnsweredQuestionId(String questionId, boolean wasYes) {
        Set<String> answeredQuestionsIds = mPrefs.getStringSet(KEY_ANSWERED_QUESTION_IDS, new HashSet<String>());
        answeredQuestionsIds.add(questionId);

        mPrefs
                .edit()
                .putStringSet(KEY_ANSWERED_QUESTION_IDS, answeredQuestionsIds)
                .apply();

        mPrefs
                .edit()
                .putString(KEY_ANSWERED_QUESTION_ID_YESNO + questionId, (wasYes ? "YES" : "NO"))
                .apply();
    }

    @Override
    public Set<String> getAnsweredQuestionsIds() {
        return mPrefs.getStringSet(KEY_ANSWERED_QUESTION_IDS, new HashSet<String>());

    }

    @Override
    public boolean haveIAnsweredQuestion(String questionId) {
        return getAnsweredQuestionsIds().contains(questionId);
    }

    @Override
    public VOTED whatDidIAnswer(String question) {
        if (haveIAnsweredQuestion(question)) {
            String value = mPrefs.getString(KEY_ANSWERED_QUESTION_ID_YESNO + question, "");
            if (value.equals("YES"))
                return VOTED.YES;
            if (value.equals("NO"))
                return VOTED.NO;
            return VOTED.UNANSWERED;
        } else {
            return VOTED.UNANSWERED;
        }
    }

    @Override
    public void markQuestionAsRemoved(String questionId) {
        Set<String> value = mPrefs.getStringSet(KEY_HIDE_QUESTIONID, new HashSet<String>());
        if (value != null) {
            value.add(questionId);
            mPrefs.edit().putStringSet(KEY_HIDE_QUESTIONID, value).apply();

            sendQuestionHiddenBroadcast();
        }
    }

    private void sendQuestionHiddenBroadcast() {
        Intent intent = new Intent(SharedPrefsManager.INTENT_QUESTION_HIDDEN);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    public boolean isQuestionRemoved(String questionId) {
        Set<String> value = mPrefs.getStringSet(KEY_HIDE_QUESTIONID, null);
        if (value != null) {
            return value.contains(questionId);
        }
        return false;
    }

    @Override
    public boolean isThisMyQuestion(String questionId) {
        Set<String> myQuestionIds = getMyQuestionIds();
        return myQuestionIds.contains(questionId);
    }

    private static final String APP_FIRST_OPEN = "app-first-open";

    public boolean isFirstOpen() {
        return mPrefs.getBoolean(APP_FIRST_OPEN, true);
    }

    public void firstOpen() {
        mPrefs
                .edit()
                .putBoolean(APP_FIRST_OPEN, false)
                .apply();
    }
}
