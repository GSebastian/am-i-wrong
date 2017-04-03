package studio.roboto.hack24.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import studio.roboto.hack24.HomeActivity;
import studio.roboto.hack24.R;
import studio.roboto.hack24.firebase.FirebaseConnector;
import studio.roboto.hack24.localstorage.SharedPrefsManager;

/**
 * Created by jordan on 03/04/17.
 */

public class RandomNameDialog extends DialogFragment implements ValueEventListener, View.OnClickListener {

    private String[] mNames;

    private View mView;
    private EditText mEtName;
    private ProgressBar mPbLoading;

    private Button mBtnRandomise;
    private Button mBtnAnonymous;
    private Button mBtnOk;

    private DatabaseReference mDbRef;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_random_name, null);

        findViews(mView);
        initViews();

        dialog.setContentView(mView);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    private void findViews(View v) {
        mEtName = (EditText) v.findViewById(R.id.etName);
        mPbLoading = (ProgressBar) v.findViewById(R.id.pbLoading);
        mBtnAnonymous = (Button) v.findViewById(R.id.btnAnonymous);
        mBtnRandomise = (Button) v.findViewById(R.id.btnRandomise);
        mBtnOk = (Button) v.findViewById(R.id.btnOk);
    }

    private void initViews() {
        enableRandomise(false);

        mDbRef = FirebaseConnector.getRemoteNames();
        mDbRef.addValueEventListener(this);

        mBtnRandomise.setOnClickListener(this);
        mBtnAnonymous.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);

        mEtName.setText(SharedPrefsManager.sharedInstance.getCurrentName());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mEtName.setText(savedInstanceState.getString("INPUT"));
        } else {
            mEtName.setText(SharedPrefsManager.sharedInstance.getCurrentName());
        }
        super.onViewStateRestored(savedInstanceState);
    }

    private void enableRandomise(boolean shouldEnable) {
        mBtnRandomise.setAlpha(shouldEnable ? 1.0f : 0.5f);
        mBtnRandomise.setEnabled(shouldEnable);
        mPbLoading.setVisibility(!shouldEnable ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDbRef.removeEventListener(this);
    }

    //region Callbacks for Firebase ValueEventListener

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            mNames = new String[(int)dataSnapshot.getChildrenCount()];
            int i = 0;
            for (DataSnapshot s : dataSnapshot.getChildren()) {
                mNames[i] = s.getKey();
                i++;
            }
            enableRandomise(true);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    //endregion


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("INPUT", mEtName.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnRandomise) {
            if (mNames != null) {
                Random r = new Random();
                int randomIndex = r.nextInt(mNames.length);
                mEtName.setText(mNames[randomIndex]);

            }
        }
        if (v == mBtnAnonymous) {
            mEtName.setText("Anonymous");
        }
        if (v == mBtnOk) {
            if (!mEtName.getText().toString().trim().equals("")) {
                SharedPrefsManager.sharedInstance.setCurrentName(mEtName.getText().toString().trim());
                ((HomeActivity)getActivity()).nameConfirmed(mEtName.getText().toString().trim());
                dismiss();
            }
        }
    }
}
