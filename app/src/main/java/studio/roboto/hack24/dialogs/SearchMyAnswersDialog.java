package studio.roboto.hack24.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import studio.roboto.hack24.R;
import studio.roboto.hack24.search.SearchMyQuestionActivity;

/**
 * Created by jordan on 03/04/17.
 */

public class SearchMyAnswersDialog extends DialogFragment implements View.OnClickListener {

    private View mView;

    private EditText mEtQuestion;
    private Button mBtnCancel;
    private Button mBtnSearch;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_search_answers, null);

        findViews(mView);
        initViews();

        dialog.setContentView(mView);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    private void findViews(View v) {
        mEtQuestion = (EditText) v.findViewById(R.id.etQuestionInput);
        mBtnCancel = (Button) v.findViewById(R.id.btnCancel);
        mBtnSearch = (Button) v.findViewById(R.id.btnSearch);
    }

    private void initViews() {
        mBtnCancel.setOnClickListener(this);
        mBtnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mBtnSearch == v) {
            if (!mEtQuestion.getText().toString().trim().equals("")) {
                // Load up the question
                Intent i = new Intent(getContext(), SearchMyQuestionActivity.class);
                i.putExtra(SearchMyQuestionActivity.SEARCH_TERM, mEtQuestion.getText().toString().trim());
                startActivity(i);
            }
        }
        if (mBtnCancel == v) {
            dismiss();
        }
    }
}
