package studio.roboto.hack24.search;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.google.firebase.database.DatabaseReference;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.util.List;

import studio.roboto.hack24.firebase.FirebaseConnector;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.localstorage.SharedPrefsManager;
import studio.roboto.hack24.questions.QuestionFragmentPagerAdapter;

/**
 * Created by jordan on 03/04/17.
 */

public class QuestionSearchPA extends QuestionFragmentPagerAdapter {

    private List<String> mWordsToCheck;

    public QuestionSearchPA(FragmentManager manager, Context context, EnchantedViewPager enchantedViewPager, List<String> wordsToCheck) {
        super(manager, context, enchantedViewPager);
        this.mWordsToCheck = wordsToCheck;
    }

    @Override
    public boolean shouldAdd(Question question) {
        System.out.println("Processing (" + question.id + ") -- " + question.text);
        int count = 0;
        for (int i = 0; i < mWordsToCheck.size(); i++) {
            if (question.text.toLowerCase().contains(mWordsToCheck.get(i).toLowerCase())) {
                count++;
            }
        }
        if (count >= 1) {
            System.out.println(" --> COUNT IS " + count + " :: " + mWordsToCheck.size());
            System.out.println("SharedPrefsManager.sharedInstance.haveIAnsweredQuestion(question.id) = " + SharedPrefsManager.sharedInstance.haveIAnsweredQuestion(question.id));
            System.out.println("SharedPrefsManager.sharedInstance.isQuestionRemoved(question.id) = " + SharedPrefsManager.sharedInstance.isQuestionRemoved(question.id));
        }
        return count == mWordsToCheck.size() && SharedPrefsManager.sharedInstance.haveIAnsweredQuestion(question.id) && !SharedPrefsManager.sharedInstance.isQuestionRemoved(question.id);
    }

    @Override
    public void added(Question question) {

    }

    @Override
    public boolean showNew() {
        return false;
    }

    @Override
    public DatabaseReference getRef() {
        return FirebaseConnector.getQuestions();
    }
}
