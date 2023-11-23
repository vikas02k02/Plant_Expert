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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.plantExpert.MainActivity;
import com.plantExpert.R;
import com.plantExpert.data.Registration.UsersDetails;

import java.util.Objects;

public class Registration extends AppCompatActivity {
    private TextInputEditText mobileNum , city ,Name , password ;
    private RadioGroup radioGroup;
    private FirebaseAuth mAuth ;
    private View registerView;
    private LinearLayout ProgressView;
    final String regexStr = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[6789]\\d{9}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mobileNum = findViewById(R.id.mobileNumber);
        city = findViewById(R.id.cityName);
        Name = findViewById(R.id.Name);
        Button register = findViewById(R.id.LoginUser);
        password = findViewById(R.id.Password);
        radioGroup = findViewById(R.id.radio);
        registerView=findViewById(R.id.registerView);
        ProgressView=findViewById(R.id.progressView);
        TextView alreadyUser = findViewById(R.id.AlreadyUser);
        mAuth = FirebaseAuth.getInstance();

        mobileNum.addTextChangedListener(new TextWatcher() {
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
        city.addTextChangedListener(new TextWatcher() {
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
        Name.addTextChangedListener(new TextWatcher() {
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
        password.addTextChangedListener(new TextWatcher() {
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


        register.setOnClickListener(view -> {
            String Email = Objects.requireNonNull(mobileNum.getText()).toString();
            String pass = Objects.requireNonNull(password.getText()).toString();
            String place = Objects.requireNonNull(city.getText()).toString();
            String name = Objects.requireNonNull(Name.getText()).toString();
            if(Email.substring(0,10).matches(regexStr)){
                registerView.setVisibility(View.VISIBLE);
                ProgressView.setVisibility(View.VISIBLE);
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                String SelectedRole = selectedRadioButton.getText().toString();
                if(SelectedRole.equals("Farmer")){
                    RegisterFarmer(Email+"@farmer.com",pass ,place,name);
                }else if (SelectedRole.equals("Plant Expert")){
                    PlantExpertRegister(Email+"@expert.com",pass,place,name);
                }
            }else {
                mobileNum.setError("Enter Valid Number");
            }


        });

        alreadyUser.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Authentication.class);
            startActivity(intent);
            finish();
        });
    }

    private void PlantExpertRegister(String Email ,String Password ,String City ,String FullName) {
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                String userMail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
                assert userMail != null;
                String MobileNumber = userMail.substring(0,10);
                UsersDetails UD= new UsersDetails(MobileNumber,City,FullName);
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference().child("Plant Expert");
                ref.child(MobileNumber).child("Expert Details").setValue(UD).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                        ProgressView.setVisibility(View.GONE);
                        registerView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Registration Completed : Work In Progress ",Toast.LENGTH_LONG).show();
                    }else {
                        ProgressView.setVisibility(View.GONE);
                        registerView.setVisibility(View.GONE);
                        Toast.makeText(Registration.this, "Registration Failed :"+ Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                ProgressView.setVisibility(View.GONE);
                registerView.setVisibility(View.GONE);
                Toast.makeText(Registration.this, "Registration Failed :"+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void RegisterFarmer(String Email ,String Password ,String City ,String FullName){
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                String userMail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
                assert userMail != null;
                String MobileNumber = userMail.substring(0,10);
                UsersDetails UD= new UsersDetails(MobileNumber,City,FullName);
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference().child("Farmer");
                ref.child(MobileNumber).child("Farmer Details").setValue(UD).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                        ProgressView.setVisibility(View.GONE);
                        registerView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Registration Completed",Toast.LENGTH_SHORT).show();
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        },1500);

                    }else {
                        ProgressView.setVisibility(View.GONE);
                        registerView.setVisibility(View.GONE);
                        Toast.makeText(Registration.this, "Registration Failed :"+ Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                ProgressView.setVisibility(View.GONE);
                registerView.setVisibility(View.GONE);
                Toast.makeText(Registration.this, "Registration Failed :"+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void checkConditionsAndEnableButton() {
        boolean isMobileNumberEntered = !TextUtils.isEmpty(mobileNum.getText());
        boolean isRoleSelected = radioGroup.getCheckedRadioButtonId() != -1;
        boolean isLength = mobileNum.getText().length() == 10 && Objects.requireNonNull(password.getText()).toString().length()>5;
        boolean isCityNameEntered = !TextUtils.isEmpty(city.getText());
        boolean isFullNameEntered = !TextUtils.isEmpty(Name.getText());
        boolean isPasswordEntered = !TextUtils.isEmpty(Name.getText());

        Button Register = findViewById(R.id.LoginUser);
        Register.setEnabled(isMobileNumberEntered && isRoleSelected && isLength && isCityNameEntered && isFullNameEntered && isPasswordEntered);

        if (isMobileNumberEntered && isRoleSelected && isLength && isCityNameEntered && isFullNameEntered && isPasswordEntered) {
            Register.setTextColor(getResources().getColor(R.color.white));
            Register.setBackgroundColor(getResources().getColor(R.color.blue));
        } else {
            Register.setTextColor(getResources().getColor(R.color.white));
            Register.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}