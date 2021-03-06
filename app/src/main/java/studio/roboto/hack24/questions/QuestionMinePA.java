package studio.roboto.hack24.questions;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.google.firebase.database.DatabaseReference;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import studio.roboto.hack24.firebase.FirebaseConnector;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.localstorage.SharedPrefsManager;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionMinePA extends QuestionFragmentPagerAdapter {
    public QuestionMinePA(FragmentManager manager, Context context, EnchantedViewPager enchantedViewPager) {
        super(manager, context, enchantedViewPager);
    }

    @Override
    public boolean shouldAdd(Question question) {
        return !SharedPrefsManager.sharedInstance.isQuestionRemoved(question.id) &&
                SharedPrefsManager.sharedInstance.getMyQuestionIds().contains(question.id) ;
    }

    @Override
    public void added(Question question) {

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
