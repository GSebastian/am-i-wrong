package studio.roboto.hack24.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import studio.roboto.hack24.R;

public class ReportFeedbackDialog extends DialogFragment implements View.OnClickListener {

    private View mView;
    private Button mBtnDismiss;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_report_feedback, null);

        findViews(mView);
        initViews();

        dialog.setContentView(mView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    private void findViews(View v) {
        mBtnDismiss = (Button) v.findViewById(R.id.btnDismiss);
    }

    private void initViews() {
        mBtnDismiss.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public static ReportFeedbackDialog getInstance() {
        return new ReportFeedbackDialog();
    }
}
