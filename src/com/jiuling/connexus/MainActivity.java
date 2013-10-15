package com.jiuling.connexus;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener,
        PlusClient.ConnectionCallbacks, PlusClient.OnConnectionFailedListener{

    private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;

    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;

    private TextView mSignInStatus;
    private PlusClient mPlusClient;
    private SignInButton mSignInButton;
    private View mSignOutButton;
    private ConnectionResult mConnectionResult;
    private View mViewStreamsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlusClient = new PlusClient.Builder(this, this, this)
        .setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
        .build();

        mSignInStatus = (TextView) findViewById(R.id.sign_in_status);
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);
        mSignOutButton = findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(this);
        mViewStreamsButton = findViewById(R.id.all_stream_button);
        mViewStreamsButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPlusClient.connect();
    }

    @Override
    public void onStop() {
        mPlusClient.disconnect();
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sign_in_button:
                
                try {
                    mSignInStatus.setText("signing in");
                    mConnectionResult.startResolutionForResult(this, REQUEST_CODE_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    // Fetch a new result to start.
                    mPlusClient.connect();
                }
                break;
            case R.id.sign_out_button:
                if (mPlusClient.isConnected()) {
                	WebUtility.userAvailable = false;
                    WebUtility.userName = "";
                    mPlusClient.clearDefaultAccount();
                    mPlusClient.disconnect();
                    mPlusClient.connect();
                }
                break;
            case R.id.all_stream_button:
            	Intent intent = new Intent(this,ViewAllStreamsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id != DIALOG_GET_GOOGLE_PLAY_SERVICES) {
            return super.onCreateDialog(id);
        }

        int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            return null;
        }
        if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
            return GooglePlayServicesUtil.getErrorDialog(
                    available, this, REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES);
        }
        return new AlertDialog.Builder(this)
                .setMessage("error")
                .setCancelable(true)
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SIGN_IN
                || requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
            if (resultCode == RESULT_OK && !mPlusClient.isConnected()
                    && !mPlusClient.isConnecting()) {
                // This time, connect should succeed.
                mPlusClient.connect();
            }
        }
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        String currentPersonName = mPlusClient.getAccountName() != null
                ? mPlusClient.getAccountName()
                : "Unknown";
        mSignInStatus.setText("Signed in as " + currentPersonName);
        updateButtons(true /* isSignedIn */);
        WebUtility.userAvailable = true;
        WebUtility.userName = currentPersonName;
    }

    @Override
    public void onDisconnected() {
        mSignInStatus.setText("loading");
        mPlusClient.connect();
        updateButtons(false /* isSignedIn */);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        mConnectionResult = result;
        updateButtons(false /* isSignedIn */);
    }

    private void updateButtons(boolean isSignedIn) {
        if (isSignedIn) {
            mSignInButton.setEnabled(false);
            mSignOutButton.setEnabled(true);
        } else {
            if (mConnectionResult == null) {
                // Disable the sign-in button until onConnectionFailed is called with result.
                mSignInStatus.setText("loading");
            } else {
                // Enable the sign-in button since a connection result is available.
                mSignInStatus.setText("signed_out");
            }
            mSignInButton.setEnabled(true);
            mSignOutButton.setEnabled(false);
        }
    }

	
}
