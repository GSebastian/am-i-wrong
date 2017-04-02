package studio.roboto.hack24.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import studio.roboto.hack24.R;

public class ReportFeedbackDialog {

    private Context mContext;

    public static ReportFeedbackDialog create(Context context) {
        ReportFeedbackDialog dialog = new ReportFeedbackDialog();
        dialog.mContext = context;
        return dialog;
    }

    public AlertDialog getDialog() {
        return new AlertDialog.Builder(mContext)
                .setTitle(R.string.report_feedback)
                .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();
    }
}
