package com.plantExpert.Validation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.plantExpert.MainActivity;
import com.plantExpert.R;

import java.util.Objects;

public class Authentication extends AppCompatActivity {
    private View LoginView;
    private LinearLayout ProgressBarView;
    private TextInputEditText mobileNumber, LoginPassword;
    private RadioGroup radioGroup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        mobileNumber = findViewById(R.id.mobileNumber);
        LoginPassword=findViewById(R.id.LoginPassword);
        radioGroup = findViewById(R.id.radio);
        Button loginButton = findViewById(R.id.LoginUser);
        TextView newUserRegister = findViewById(R.id.NewUser);
        LoginView=findViewById(R.id.LoginView);
        ProgressBarView = findViewById(R.id.progressBarView);

        mAuth = FirebaseAuth.getInstance();

        mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                checkConditionsAndEnableButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkConditionsAndEnableButton();
            }
        });

        LoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                checkConditionsAndEnableButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkConditionsAndEnableButton();
            }
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> checkConditionsAndEnableButton());


        loginButton.setOnClickListener(view -> {
            String PhoneNumber = Objects.requireNonNull(mobileNumber.getText()).toString();
            String Password = Objects.requireNonNull(LoginPassword.getText()).toString();
            RadioButton selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
            String SelectedRole = selectedRadioButton.getText().toString();
            LoginView.setVisibility(View.VISIBLE);
            ProgressBarView.setVisibility(View.VISIBLE);
            if (SelectedRole.equals("Farmer")){
                LoginFarmer(PhoneNumber+"@farmer.com" , Password);
            } else if (SelectedRole.equals("Plant Expert")) {
                LoginPlantExpert(PhoneNumber+"@expert.com",Password);
            }

        });

        newUserRegister.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Registration.class);
            startActivity(intent);
            finish();
        });

    }

    private void LoginPlantExpert(String s, String password) {
        mAuth.signInWithEmailAndPassword(s,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                LoginView.setVisibility(View.GONE);
                ProgressBarView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Login Successful : Work In Progress ",Toast.LENGTH_LONG).show();
//                    Handler handler= new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    },1500);
            }else {
                LoginView.setVisibility(View.GONE);
                ProgressBarView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void LoginFarmer(String s, String password) {
        mAuth.signInWithEmailAndPassword(s,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                LoginView.setVisibility(View.GONE);
                ProgressBarView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Login Successful : WELCOME BACK ",Toast.LENGTH_LONG).show();
                Handler handler= new Handler();
                handler.postDelayed(() -> {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                },1000);
            }else {
                LoginView.setVisibility(View.GONE);
                ProgressBarView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkConditionsAndEnableButton() {
        boolean isMobileNumberEntered = !TextUtils.isEmpty(mobileNumber.getText());
        boolean isPasswordEntered = !TextUtils.isEmpty(LoginPassword.getText());
        boolean isRoleSelected = radioGroup.getCheckedRadioButtonId() != -1;
        boolean isLength = mobileNumber.getText().length() == 10;

        Button submitButton = findViewById(R.id.LoginUser);
        submitButton.setEnabled(isMobileNumberEntered && isRoleSelected && isLength);

        if (isMobileNumberEntered && isPasswordEntered && isRoleSelected && isLength) {
            submitButton.setTextColor(getResources().getColor(R.color.white));
            submitButton.setBackgroundColor(getResources().getColor(R.color.blue));
        } else {
            submitButton.setTextColor(getResources().getColor(R.color.white));
            submitButton.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    }



}
