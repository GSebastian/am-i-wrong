package studio.roboto.hack24.questions;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionFragmentPagerAdapter extends FragmentStatePagerAdapter{

    private Context mContext;
    private List<View> views = new ArrayList<>();

    public QuestionFragmentPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        QuestionElementFragment frag = new QuestionElementFragment();
        Bundle b = new Bundle();
        b.putInt("KEY", position);
        frag.setArguments(b);
        return frag;
    }
}
