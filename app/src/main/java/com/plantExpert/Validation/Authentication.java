package com.plantExpert.Validation;

import static com.plantExpert.R.drawable.solution_button_pending;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.plantExpert.MainActivity;
import com.plantExpert.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Authentication extends AppCompatActivity {
    View authView ;
    LinearLayout OtpView ;
    TextInputEditText mobileNumber ,OTP;
    Button getOTP , Submit ;
    TextView resend ;
    String PhoneNumber ,SelectedRole ,verificationID ;
    final String regexStr = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[6789]\\d{9}$";
    int REQUEST_PERMISSION_CODE =100;
    RadioGroup radioGroup;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        mobileNumber = findViewById(R.id.mobileNumber);
        getOTP = findViewById(R.id.getOTP);
        authView = findViewById(R.id.AuthView);
        OtpView = findViewById(R.id.OTPView);
        OTP = findViewById(R.id.setOTP);
        Submit = findViewById(R.id.submit);
        resend = findViewById(R.id.ResendOTP);
        radioGroup = findViewById(R.id.radio);

        mAuth = FirebaseAuth.getInstance();
        if( ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS}, REQUEST_PERMISSION_CODE);
        }

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
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            checkConditionsAndEnableButton();
        });
        OTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean Flag = !TextUtils.isEmpty(OTP.getText().toString());
                if(Flag){
                    Submit.setTextColor(getResources().getColor(R.color.white));
                    Submit.setBackgroundColor(getResources().getColor(R.color.blue));
                }else {
                    Submit.setTextColor(getResources().getColor(R.color.white));
                    Submit.setBackgroundColor(getResources().getColor(R.color.grey));
                    Submit.setEnabled(false);
                }
                Submit.setEnabled(Flag);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                boolean Flag = !TextUtils.isEmpty(OTP.getText().toString());
                if(Flag){
                    Submit.setTextColor(getResources().getColor(R.color.white));
                    Submit.setBackgroundColor(getResources().getColor(R.color.blue));
                }else {
                    Submit.setTextColor(getResources().getColor(R.color.white));
                    Submit.setBackgroundColor(getResources().getColor(R.color.grey));
                    Submit.setEnabled(false);
                }
                Submit.setEnabled(Flag);

            }
        });


        getOTP.setOnClickListener(view -> {
            PhoneNumber = Objects.requireNonNull(mobileNumber.getText()).toString();
            if(!PhoneNumber.matches(regexStr)){
                mobileNumber.setError("Enter Valid Number");
            }else {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                SelectedRole = selectedRadioButton.getText().toString();
                authView.setVisibility(View.VISIBLE);
                OtpView.setVisibility(View.VISIBLE);
                sendVerificationCode(PhoneNumber);
                Toast.makeText(Authentication.this,PhoneNumber +SelectedRole,Toast.LENGTH_LONG).show();
            }

        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SelectedRole.equals("Farmer")){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else {
//                    Add the Plant Expert Class Activity
//                    Intent intent = new Intent(getApplicationContext(), PlantExpert.class);
//                    startActivity(intent);
                }
                String otp = OTP.getText().toString();
//                verifyPhone(otp);
            }
        });


    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber("+91"+phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                verifyPhone(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Authentication.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s ,@NonNull PhoneAuthProvider.ForceResendingToken token){
            super.onCodeSent(s,token);
            verificationID = s;
        }
    };

    private void verifyPhone(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID ,code);
        signInByCredential(credential);
    }

    private void signInByCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Authentication.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Authentication.this,MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkConditionsAndEnableButton() {
        TextInputEditText mobileNumberEditText = findViewById(R.id.mobileNumber);
        RadioGroup roleRadioGroup = findViewById(R.id.radio);
        Button submitButton = findViewById(R.id.getOTP);
        boolean isMobileNumberEntered = !TextUtils.isEmpty(mobileNumberEditText.getText());
        boolean isRoleSelected = roleRadioGroup.getCheckedRadioButtonId() != -1;
        boolean isLength =false ;
        if(mobileNumberEditText.getText().length() == 10){
            isLength=true;
        };
        if(isMobileNumberEntered && isRoleSelected && isLength){
            submitButton.setTextColor(getResources().getColor(R.color.white));
            submitButton.setBackgroundColor(getResources().getColor(R.color.blue));
        }else {
            submitButton.setTextColor(getResources().getColor(R.color.white));
            submitButton.setBackgroundColor(getResources().getColor(R.color.grey));
            submitButton.setEnabled(false);
        }
        submitButton.setEnabled(isMobileNumberEntered && isRoleSelected);
    }
    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(Authentication.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){

            }else {
                Toast.makeText(getApplicationContext(),"Permissions Denied , Allow permissions to continue",Toast.LENGTH_LONG).show();
            }
        }
    }
}