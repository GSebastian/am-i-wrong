package studio.roboto.hack24.questions;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.google.firebase.database.DatabaseReference;

import studio.roboto.hack24.firebase.FirebaseConnector;
import studio.roboto.hack24.firebase.FirebaseManager;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.localstorage.SharedPrefsManager;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionMainPA extends QuestionFragmentPagerAdapter {
    public QuestionMainPA(FragmentManager manager, Context context) {
        super(manager, context);
    }

    @Override
    public boolean shouldAdd(Question question) {
        return !SharedPrefsManager.sharedInstance.haveIAnsweredQuestion(question.id);
    }

    @Override
    public boolean showNew() {
        return true;
    }

    @Override
    public DatabaseReference getRef() {
        return FirebaseConnector.getQuestions();
    }
}
