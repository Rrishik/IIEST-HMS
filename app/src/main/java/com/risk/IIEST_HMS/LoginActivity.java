package com.risk.IIEST_HMS;

import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.risk.IIEST_HMS.Helper.AsyncTasks;
import com.risk.IIEST_HMS.Helper.Constants;
import com.risk.IIEST_HMS.Helper.PageParser;
import com.risk.IIEST_HMS.Job.DueCheckJob;

public class LoginActivity extends AppCompatActivity {

    private AsyncTasks mAuthTask = null;
    private int JOB_ID = 1;

    private String loginUrl = "http://iiesthostel.iiests.ac.in/students/login_students";

    // UI references.
    private LinearLayout mLoginLayout;
    private TextView mSIDView;
    private TextView mPasswordView;
    private ImageButton mShowPassword;
    private ProgressDialog mProgressView;
    private CheckBox mCheckBox;
    private Switch mSwitch;
    private Button mSignInButton;
    private boolean show_passwd = false;
    private boolean saved_remember_me;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginLayout = (LinearLayout) findViewById(R.id.login_layout);
        // Set up the login form.
        mSIDView = (TextView) findViewById(R.id.sid);

        mPasswordView = (TextView) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mShowPassword = (ImageButton) findViewById(R.id.show_passwd);
        mShowPassword.getBackground().setAlpha(60);
        mShowPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!show_passwd) {
                    mShowPassword.getBackground().setAlpha(140);
                    mPasswordView.setTransformationMethod(null);
                    show_passwd = true;
                } else {
                    mShowPassword.getBackground().setAlpha(60);
                    mPasswordView.setTransformationMethod(new PasswordTransformationMethod());
                    show_passwd = false;
                }
            }
        });

        mProgressView = new ProgressDialog(LoginActivity.this);
        mProgressView.setMessage("Authenticating...");
        mProgressView.setCancelable(true);
        mProgressView.setCanceledOnTouchOutside(false);

        mCheckBox = (CheckBox) findViewById(R.id.checkBox);

        mSwitch = (Switch) findViewById(R.id.switch1);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        saved_remember_me = sharedPreferences.getBoolean(getString(R.string.saved_remember_me), false);

        if (saved_remember_me) {
            mSIDView.setText(sharedPreferences.getString(getString(R.string.saved_sid), null));
            mPasswordView.setText(sharedPreferences.getString(getString(R.string.saved_password), null));
        }

        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.ic_notif);
        if (saved_remember_me) {
            mLoginLayout.setVisibility(View.GONE);
            showProgress(true);
            login(sharedPreferences.getString(getString(R.string.saved_sid), null), sharedPreferences.getString(getString(R.string.saved_password), null));
        }
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
        View focusView = mSignInButton;

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
            // perform the user login attempt and save the credentials

            sharedPreferences.edit().putString(getString(R.string.saved_sid), sid)
                    .putString(getString(R.string.saved_password), password)
                    .apply();


            if (mCheckBox.isChecked()) {
                sharedPreferences.edit()
                        .putBoolean(getString(R.string.saved_remember_me), true)
                        .apply();
            } else {
                sharedPreferences.edit()
                        .putBoolean(getString(R.string.saved_remember_me), false)
                        .apply();
                cancelJobs();
            }


            if (mSwitch.isChecked())
                schedule();

            if (focusView != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
            showProgress(true);
            login(sid, password);
        }
    }

    private void schedule() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(Constants.Job.SID, mSIDView.getText().toString());
        bundle.putString(Constants.Job.PASSWORD, mPasswordView.getText().toString());

        ComponentName serviceName = new ComponentName(getApplicationContext(), DueCheckJob.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(86400000)
                .setExtras(bundle)
                .build();
        cancelJobs();
        int result = jobScheduler.schedule(jobInfo);
        if (result == JobScheduler.RESULT_SUCCESS)
            Toast.makeText(getApplicationContext(), "You'll be notified about your dues :)", Toast.LENGTH_SHORT).show();
    }

    private void cancelJobs() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
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

    private void login(String sid, String password) {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("sid", sid)
                .appendQueryParameter("password", password)
                .appendQueryParameter("type", "2")
                .appendQueryParameter("login", "");
        String query = builder.build().getEncodedQuery();
        String type = "application/x-www-form-urlencoded";
        mAuthTask = new AsyncTasks(query, type, new AsyncTasks.AsyncTasksListener() {
            @Override
            public void onPostExecute(String response) {
                mAuthTask = null;
                showProgress(false);

                if (response != null) {
                    PageParser p = new PageParser(response);
                    if (p.checkLogin()) {
                        Toast.makeText(LoginActivity.this, "Login Success!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        LoginActivity.this.finish();
                        intent.putExtra(Constants.Intent.LOGIN_PAGE_SOURCE, response);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failure!!", Toast.LENGTH_SHORT).show();
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("There seems to be a problem with your internet connection.\nPlease try again later!")
                            .setTitle("OOPS!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    builder.show();
                }
            }

            @Override
            public void onCancelled() {
                mAuthTask = null;
                showProgress(false);
            }
        });
        mAuthTask.execute(loginUrl);
    }
}