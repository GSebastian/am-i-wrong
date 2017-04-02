package studio.roboto.hack24.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import studio.roboto.hack24.R;
import studio.roboto.hack24.localstorage.SharedPrefsManager;

public class HideQuestionDialog {

    private Context mContext;
    private String mQuestionId;
    private QuestionHiddenListener mListener;

    private AlertDialog mDialog;

    public interface QuestionHiddenListener {
        void questionHidden(String questionId);
    }

    public static HideQuestionDialog create(Context context,
                                            String questionId,
                                            @Nullable QuestionHiddenListener listener) {

        HideQuestionDialog dialog = new HideQuestionDialog();
        dialog.mContext = context;
        dialog.mQuestionId = questionId;
        dialog.mListener = listener;
        return dialog;
    }

    public AlertDialog getDialog() {
        mDialog = new AlertDialog.Builder(mContext)
                .setNegativeButton(R.string.cancel, null)
                .setTitle(R.string.hide_question)
                .setMessage(mContext.getString(R.string.hide_question_confirmation))
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPrefsManager.sharedInstance.markQuestionAsRemoved(mQuestionId);
                        if (mListener != null) {
                            mListener.questionHidden(mQuestionId);
                        }

                        mDialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();

        return mDialog;
    }
}