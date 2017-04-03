package studio.roboto.hack24.questions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.util.Random;

import studio.roboto.hack24.R;
import studio.roboto.hack24.Utils;
import studio.roboto.hack24.dialogs.PostingTooSoonDialog;
import studio.roboto.hack24.firebase.FirebaseConnector;
import studio.roboto.hack24.localstorage.SharedPrefsManager;

public class NewQuestionFragment extends Fragment implements View.OnClickListener {

    private static final int MAX_CHARACTERS = 180;

    private Button mBtnAsk;
    private EditText mEtQuestionInput;
    private TextView mTvCurrentLength;

    private OnQuestionAddedListener mOnQuestionAddedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_question, null);

        getArgs(rootView);

        findViews(rootView);
        initViews();

        return rootView;
    }

    private void findViews(View rootView) {
        mBtnAsk = (Button) rootView.findViewById(R.id.btnAsk);
        mEtQuestionInput = (EditText) rootView.findViewById(R.id.etQuestionInput);
        mTvCurrentLength = (TextView) rootView.findViewById(R.id.tvCurrentLength);
    }

    private void updateCharacterCounter(int currentCharNumber) {
        String charsRemainingUnformatted = getString(R.string.chars_remaining);
        mTvCurrentLength.setText(String.format(charsRemainingUnformatted, currentCharNumber, MAX_CHARACTERS));
    }

    private void initViews() {
        mBtnAsk.setOnClickListener(this);

        mEtQuestionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int currentLength = charSequence.length();
                updateCharacterCounter(currentLength);

                mBtnAsk.setEnabled(charSequence.toString().trim().length() > 0);
            }
        });

        mEtQuestionInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_CHARACTERS)});
        updateCharacterCounter(0);
        mBtnAsk.setEnabled(false);

        String[] hints = getResources().getStringArray(R.array.example_question);

        Random random = new Random();
        int index = random.nextInt(hints.length);
        mEtQuestionInput.setHint(hints[index]);
    }

    private boolean getArgs(View v) {
        if (getArguments() != null) {
            int position = getArguments().getInt("KEY", -1);
            if (position != -1) {
                v.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
            }
        }
        return false;
    }

    public void setOnQuestionAddedListener(OnQuestionAddedListener listener) {
        mOnQuestionAddedListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnAsk) {

            if (Utils.canIPost()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setView(R.layout.dialog_adding_message);

                final AlertDialog progressDialog = builder.create();
                progressDialog.show();

                FirebaseConnector.addQuestion(
                        mEtQuestionInput.getText().toString().trim(),
                        new OnQuestionAddedListener() {
                            @Override
                            public void questionAdded(String questionId) {
                                progressDialog.hide();

                                SharedPrefsManager.sharedInstance.addMyQuestionId(questionId);

                                mEtQuestionInput.setText(null);

                                if (mOnQuestionAddedListener != null) {
                                    mOnQuestionAddedListener.questionAdded(questionId);
                                }
                            }

                            @Override
                            public void questionAddFailed() {
                                progressDialog.hide();

                                if (mOnQuestionAddedListener != null) {
                                    mOnQuestionAddedListener.questionAddFailed();
                                }
                            }
                        });
            } else {
                new PostingTooSoonDialog().show(getFragmentManager(), "PostingTooSoonDialog");
            }
        }
    }
}
