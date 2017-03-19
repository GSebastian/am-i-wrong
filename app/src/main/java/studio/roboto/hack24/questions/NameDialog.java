package studio.roboto.hack24.questions;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

import studio.roboto.hack24.R;
import studio.roboto.hack24.localstorage.SharedPrefsManager;

/**
 * Created by jordan on 18/03/17.
 */

public class NameDialog implements DialogInterface.OnShowListener {

    private Context mContext;

    private EditText mEditText;
    private AlertDialog mDialog;
    private NameConfirmedCallback mCallback;

    public interface NameConfirmedCallback {
        void nameConfirmed(String name);
    }

    public static NameDialog create(Context context) {
        NameDialog dialog = new NameDialog();
        dialog.mContext = context;
        return dialog;
    }

    public AlertDialog getDialog(NameConfirmedCallback callback) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_enter_name, null);
        mEditText = (EditText) view.findViewById(R.id.etName);
        mDialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .setTitle(R.string.enter_name)
                .setPositiveButton(R.string.go, null)
                .setNeutralButton(R.string.random, null)
                .setNegativeButton(R.string.anonymous, null)
                .setCancelable(false)
                .create();
        mDialog.setOnShowListener(this);
        mDialog.setCanceledOnTouchOutside(false);
        mCallback = callback;
        return mDialog;
    }

    @Override
    public void onShow(DialogInterface dialog) {
        Button ok = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mEditText.getText().toString().trim().equals("")) {
                    SharedPrefsManager.sharedInstance.setCurrentName(mEditText.getText().toString().trim());
                    mCallback.nameConfirmed(mEditText.getText().toString().trim());
                    mDialog.hide();
                }
            }
        });
        Button random = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] string = mContext.getResources().getStringArray(R.array.names);
                Random r = new Random();
                int randomIndex = r.nextInt(string.length);
                mEditText.setText(string[randomIndex]);
            }
        });
        Button anonymous = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
        anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText("Anonymous");
            }
        });
    }
}
