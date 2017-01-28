package com.example.ramen.iiest_hms;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramen.iiest_hms.helper.NetworkUtils;
import com.example.ramen.iiest_hms.helper.PageParser;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;

    private String url = "http://iiesthostel.iiests.ac.in/students/login_students";

    // UI references.
    private TextView mSIDView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private ProgressDialog mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mSIDView = (TextView) findViewById(R.id.sid);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mProgressView = new ProgressDialog(LoginActivity.this);
        mProgressView.setMessage("Authenticating...");
        mProgressView.setCancelable(true);
        mProgressView.setCanceledOnTouchOutside(false);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mSIDView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String sid = mSIDView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a empty Student ID.
        if (TextUtils.isEmpty(sid)) {
            mSIDView.setError(getString(R.string.error_field_required));
            focusView = mSIDView;
            cancel = true;
        }

        // Check for an invalid Student ID.
        if (TextUtils.getTrimmedLength(sid) != 9) {
            mSIDView.setError(getString(R.string.error_invalid_sid));
            focusView = mSIDView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(sid, password);
            mAuthTask.execute(url);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {

        if (show)
            mProgressView.show();
        else
            mProgressView.dismiss();

    }


    public class UserLoginTask extends AsyncTask<String, String, String> {

        private final String mSid;
        private final String mPassword;
        private String response;

        UserLoginTask(String sid, String password) {
            mSid = sid;
            mPassword = password;
        }

        @Override
        protected String doInBackground(String... urls) {

            String urlString = urls[0];
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("sid", mSid)
                    .appendQueryParameter("password", mPassword)
                    .appendQueryParameter("type", "2")
                    .appendQueryParameter("login", "");
            String paramsQuery = builder.build().getEncodedQuery();
            Log.d("params", paramsQuery);

            try {
                response = NetworkUtils.okHttpPostRequest(urlString, paramsQuery);
                return response;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(final String success) {
            mAuthTask = null;
            showProgress(false);

            if (success != null) {
                PageParser p = new PageParser(LoginActivity.this, success);
                if (p.checkLogin()) {
                    Toast.makeText(LoginActivity.this, "Login Success!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, students_home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failure!!", Toast.LENGTH_SHORT).show();
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }


    }
}