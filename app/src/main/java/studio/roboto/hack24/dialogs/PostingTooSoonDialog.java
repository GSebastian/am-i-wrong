package studio.roboto.hack24.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import studio.roboto.hack24.R;
import studio.roboto.hack24.Utils;

public class PostingTooSoonDialog extends DialogFragment implements View.OnClickListener {

    private String[] mNames;

    private View mView;
    private TextView mTvExplanation;
    private Button mBtnDismiss;

    private DatabaseReference mDbRef;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_posting_too_soon, null);

        findViews(mView);
        initViews();

        dialog.setContentView(mView);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    private void findViews(View v) {
        mTvExplanation = (TextView) v.findViewById(R.id.tvExplanation);
        mBtnDismiss = (Button) v.findViewById(R.id.btnDismiss);
    }

    private void initViews() {
        String unformatted = getString(R.string.post_too_soon);
        mTvExplanation.setText(String.format(unformatted, Utils.POST_LIMIT / 60));

        mBtnDismiss.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnDismiss) {
            dismiss();
        }
    }
}
