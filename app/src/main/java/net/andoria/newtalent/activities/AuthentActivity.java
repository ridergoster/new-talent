package net.andoria.newtalent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.andoria.newtalent.R;
import net.andoria.newtalent.models.SessionData;
import net.andoria.newtalent.models.User;
import net.andoria.newtalent.network.APIService;
import net.andoria.newtalent.utils.PreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by maxime on 22/07/16.
 */
public class AuthentActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_subscribe)
    Button btnSubscribe;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.pb_authent)
    RelativeLayout pbAuthent;
    @BindView(R.id.btn_exit)
    Button btnExit;

    private boolean isViewLogin = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authent);
        ButterKnife.bind(this);
        PreferenceHelper.getInstance().initPref(this, null);
    }

    @OnClick({R.id.btn_login, R.id.btn_subscribe, R.id.btn_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (verifFields()) {
                    pbAuthent.setVisibility(View.VISIBLE);
                    APIService.getInstance(getBaseContext()).authentication(buildUser(), new APIService.APIResult<User>() {
                        @Override
                        public void success(User res) {
                            PreferenceHelper.getInstance().initPref(getBaseContext(), res.getId());
                            SessionData.getInstance().setCurrentUser(res);
                            pushNextActivity();
                        }

                        @Override
                        public void error(int code, String message) {
                            Toast.makeText(getBaseContext(), "FAILED : " + message, Toast.LENGTH_LONG).show();
                            pbAuthent.setVisibility(View.GONE);
                        }
                    });
                }
                break;
            case R.id.btn_subscribe:
                if (isViewLogin) {
                    animBtnTranslate(true);
                } else {
                    if (verifFields()) {
                        pbAuthent.setVisibility(View.VISIBLE);
                        APIService.getInstance(getBaseContext()).subscribe(buildUser(), new APIService.APIResult<User>() {
                            @Override
                            public void success(User res) {
                                animBtnTranslate(false);
                                pbAuthent.setVisibility(View.GONE);
                            }

                            @Override
                            public void error(int code, String message) {
                                Toast.makeText(getBaseContext(), "FAILED : " + message, Toast.LENGTH_LONG).show();
                                pbAuthent.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                }
                break;
            case R.id.btn_exit:
                animBtnTranslate(false);
                break;
        }
    }

    private void animBtnTranslate(boolean hideLogin) {
        if (hideLogin) {
            isViewLogin = false;
            btnExit.setTranslationX(-750);
            btnExit.setVisibility(View.VISIBLE);
            btnLogin.animate()
                    .translationXBy(750)
                    .alpha(0f)
                    .setDuration(500)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            btnLogin.setEnabled(false);
                            etEmail.setText("");
                            etPassword.setText("");
                        }
                    });

            btnExit.animate()
                    .translationXBy(750)
                    .alpha(1f)
                    .setDuration(500).
                    withEndAction(new Runnable() {
                        @Override
                        public void run() {
//                            btnExit.setVisibility(View.VISIBLE);
                        }
                    });
            ;
        } else {
            isViewLogin = true;
            btnLogin.animate()
                    .alpha(1f)
                    .translationXBy(-750)
                    .setDuration(500)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            btnLogin.setEnabled(true);
                            etEmail.setText("");
                            etPassword.setText("");
                        }
                    });
            btnExit.animate()
                    .alpha(0f)
                    .translationXBy(-750)
                    .setDuration(500)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            btnExit.setVisibility(View.GONE);
                        }
                    });
        }
    }


    private void pushNextActivity() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        pbAuthent.setVisibility(View.GONE);
        finish();
    }

    private User buildUser() {
        User user = new User();
        user.setEmail(etEmail.getText().toString());
        user.setPassword(etPassword.getText().toString());
        return user;
    }

    private boolean verifFields() {
        boolean isOk = true;
        if (etEmail.getText().toString().isEmpty()) {
            tilEmail.setError(getResources().getString(R.string.auth_til_error_mandatory_field));
            isOk = false;
        }
        if (etPassword.getText().toString().isEmpty()) {
            tilPassword.setError(getResources().getString(R.string.auth_til_error_mandatory_field));
            isOk = false;
        }

        return isOk;
    }
}
