package studio.roboto.hack24.localstorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefsManager implements ISharedPrefsManager {

    public static SharedPrefsManager sharedInstance = null;

    private static final String KEY_NAME = "name";
    private static final String KEY_MY_QUESTION_IDS = "my-question-ids";
    private static final String KEY_ANSWERED_QUESTION_IDS = "answered-question-ids";
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
        return mPrefs.getString(KEY_NAME, null);
    }

    @Override
    public void addMyQuestionId(String questionId) {
        String myQuestionIdsString = mPrefs.getString(KEY_MY_QUESTION_IDS, "");
        List<String> myQuestionIds = getListFromString(myQuestionIdsString);

        myQuestionIds.add(questionId);

        String newString = mapListToString(myQuestionIds);

        mPrefs
                .edit()
                .putString(KEY_MY_QUESTION_IDS, newString)
                .apply();
    }

    @Override
    public List<String> getMyQuestionIds() {
        String storedString = mPrefs.getString(KEY_MY_QUESTION_IDS, "");
        return getListFromString(storedString);
    }

    @Override
    public void addAnsweredQuestionId(String questionId, boolean wasYes) {
        String answeredQuestionsIdsString = mPrefs.getString(KEY_ANSWERED_QUESTION_IDS, "");
        List<String> answeredQuestionsIds = getListFromString(answeredQuestionsIdsString);

        answeredQuestionsIds.add(questionId);

        String newString = mapListToString(answeredQuestionsIds);

        mPrefs
                .edit()
                .putString(KEY_ANSWERED_QUESTION_IDS, newString)
                .apply();

        mPrefs
                .edit()
                .putString(KEY_ANSWERED_QUESTION_ID_YESNO, (wasYes ? "YES" : "NO"))
                .apply();
    }

    @Override
    public List<String> getAnsweredQuestionsIds() {
        String storedString = mPrefs.getString(KEY_ANSWERED_QUESTION_IDS, "");
        return getListFromString(storedString);
    }

    @Override
    public boolean haveIAnsweredQuestion(String questionId) {
        return getMyQuestionIds().contains(questionId);
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


    //region Utility
    private static final String DELIMITER = "_";

    static String mapListToString(List<String> list) {
        String result = "";
        for (Object element : list) {
            result += element;
            result += DELIMITER;
        }

        return result;
    }

    static List<String> getListFromString(String string) {
        String[] array = string.split(DELIMITER);
        List<String> result = new ArrayList<>();

        for (String item : array) {
            result.add(item);
        }

        return result;
    }
    //endregion
}
