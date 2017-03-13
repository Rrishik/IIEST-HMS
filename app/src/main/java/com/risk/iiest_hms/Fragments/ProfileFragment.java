package com.risk.iiest_hms.Fragments;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.risk.iiest_hms.Helper.AsyncTasks;
import com.risk.iiest_hms.Helper.Constants;
import com.risk.iiest_hms.Helper.PageParser;
import com.risk.iiest_hms.LoginActivity;
import com.risk.iiest_hms.R;

public class ProfileFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private AsyncTasks mSubmitTask = null;
    private SharedPreferences sharedPreferences;

    private String login_page_source;

    private View view;
    private EditText enter_new_pass;
    private EditText re_enter_new_pass;

    private CheckedTextView sid;
    private CheckedTextView name;
    private Button chng_passwd;
    private Button logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");

        name = (CheckedTextView) getView().findViewById(R.id.tv_name);
        sid = (CheckedTextView) getView().findViewById(R.id.tv_id);
        chng_passwd = (Button) getView().findViewById(R.id.btn_open_change_pass_dialog);
        logout = (Button) getView().findViewById(R.id.btn_logout);

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        view = layoutInflater.inflate(R.layout.change_passwd_dialog, null);
        enter_new_pass = (EditText) view.findViewById(R.id.et_enter_new_passwd);
        re_enter_new_pass = (EditText) view.findViewById(R.id.et_reenter_new_passwd);

        final Bundle extras = this.getArguments();

        if (extras != null) {
            login_page_source = extras.getString(Constants.Intent.LOGIN_PAGE_SOURCE);
        }

        PageParser p = new PageParser(login_page_source);
        String stu = p.getUserName();
        name.setText(stu.substring(0, stu.indexOf("(")));
        sid.setText(stu.substring(stu.indexOf("(") + 1, stu.indexOf(")")));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit()
                        .putBoolean(getString(R.string.saved_remember_me), false)
                        .apply();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(intent);

            }
        });
        chng_passwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePasswdDialog();
            }
        });
    }

    private void showChangePasswdDialog() {

        if (view.getParent() != null)
            ((ViewGroup) view.getParent()).removeView(view);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Change Password")
                .setPositiveButton("Change", null)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button positive = (dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = (dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean flag = submit();
                        if (flag)
                            dialogInterface.dismiss();
                    }
                });
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        dialog.show();

    }

    private boolean submit() {

        String pass = enter_new_pass.getText().toString();
        String repass = re_enter_new_pass.getText().toString();

        if (!pass.equals(repass)) {
            re_enter_new_pass.requestFocus();
            int ecolor = R.color.white;
            String estring = "The Passwords do not match!";
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(getResources().getColor(ecolor));
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
            ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
            re_enter_new_pass.setError(ssbuilder);
            return false;
        } else {
            final String url = "http://iiesthostel.iiests.ac.in/event/student_password_change";
            String params = "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"id\"\r\n\r\n1124\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"passw\"\r\n\r\n" + pass + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"re_passw\"\r\n\r\n" + pass + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"submit\"\r\n\r\nSubmit\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--";
            final String mediaType = "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW";
            mSubmitTask = new AsyncTasks(params, mediaType, new AsyncTasks.AsyncTasksListener() {
                @Override
                public void onPostExecute(String response) {
                    if (response != null) {
                        PageParser p = new PageParser(response);
                        if (p.checkLogin()) {
                            Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Connection timed Out!")
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
                    mSubmitTask = null;
                }
            });
            mSubmitTask.execute(url);
            return true;
        }
    }

}


