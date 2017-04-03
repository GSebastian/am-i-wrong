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
    private NoResultNotifier mNotifier;

    public QuestionSearchPA(FragmentManager manager, Context context, EnchantedViewPager enchantedViewPager,
                            List<String> wordsToCheck, NoResultNotifier notifier) {
        super(manager, context, enchantedViewPager);
        this.mWordsToCheck = wordsToCheck;
        this.mNotifier = notifier;
    }

    @Override
    public boolean shouldAdd(Question question) {
        int count = 0;
        for (int i = 0; i < mWordsToCheck.size(); i++) {
            if (question.text.toLowerCase().contains(mWordsToCheck.get(i).toLowerCase())) {
                count++;
            }
        }
        boolean shouldAdd = count == mWordsToCheck.size()
                && SharedPrefsManager.sharedInstance.haveIAnsweredQuestion(question.id)
                && !SharedPrefsManager.sharedInstance.isQuestionRemoved(question.id);

        if (shouldAdd) {
            mNotifier.resultAdded();
        }

        return shouldAdd;
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

    public interface NoResultNotifier {
        void resultAdded();
    }
}
