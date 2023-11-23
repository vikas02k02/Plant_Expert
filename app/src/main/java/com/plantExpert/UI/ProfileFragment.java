package com.plantExpert.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.plantExpert.R;
import com.plantExpert.data.Registration.UsersDetails;

import java.util.Objects;


public class ProfileFragment extends Fragment {
    private TextInputEditText Name ,PhoneNumber ,City ,NewPassword ,ConfirmPassword,CurrentPassword;
    FirebaseAuth mAuth ;
    DatabaseReference reference;
    FirebaseDatabase database;
    View BackgroundView ,LoadingView;
    Button submitPassword;
    LinearLayout PasswordView ,ProgressBarView ;
    ConstraintLayout LanguageChangeView;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.fragment_profile, container, false);
        Name = root.findViewById(R.id.FarmerName);
        PhoneNumber=root.findViewById(R.id.FarmerPhoneNumber);
        City =root.findViewById(R.id.FarmerCity);
        Button saveButton = root.findViewById(R.id.Save);
        BackgroundView = root.findViewById(R.id.BackgroundView);
        PasswordView =root.findViewById(R.id.PasswordView);
        LanguageChangeView = root.findViewById(R.id.SelectLanguageView);
        Button hindiApp = root.findViewById(R.id.HindiApp);
        Button englishApp = root.findViewById(R.id.EnglishApp);
        Button punjabiApp = root.findViewById(R.id.PunjabiApp);
        Button tamilApp = root.findViewById(R.id.TamilApp);
        Button changePassword = root.findViewById(R.id.changePassword);
        Button changeLanguage = root.findViewById(R.id.language);
        submitPassword = root.findViewById(R.id.SubmitPassword);
        LoadingView=root.findViewById(R.id.LoadingChangeView);
        ProgressBarView=root.findViewById(R.id.progressView);
        NewPassword=root.findViewById(R.id.NewPassword);
        ConfirmPassword=root.findViewById(R.id.ConfirmPassword);
        CurrentPassword=root.findViewById(R.id.CurrentPassword);

        mAuth= FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Farmer").child(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()).substring(0,10)).child("Farmer Details");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UsersDetails usersDetails = snapshot.getValue(UsersDetails.class);

                    if (usersDetails != null) {
                        Name.setText(usersDetails.getFullName());
                        PhoneNumber.setText(usersDetails.getMobileNumber());
                        City.setText(usersDetails.getCityName());
                    } else {
                        Toast.makeText(getActivity(),"Data is Available",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(),"Error 404: Details Not Found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Server Error: Please Try Again Later", Toast.LENGTH_LONG).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsersDetails usersDetails = new UsersDetails(Objects.requireNonNull(PhoneNumber.getText()).toString(), Objects.requireNonNull(City.getText()).toString(), Objects.requireNonNull(Name.getText()).toString());
                reference.setValue(usersDetails).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(), "Data Saved", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundView.setVisibility(View.VISIBLE);
                LanguageChangeView.setVisibility(View.VISIBLE);
            }
        });
        hindiApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundView.setVisibility(View.GONE);
                LanguageChangeView.setVisibility(View.GONE);
            }
        });
        englishApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundView.setVisibility(View.GONE);
                LanguageChangeView.setVisibility(View.GONE);
            }
        });
        punjabiApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundView.setVisibility(View.GONE);
                LanguageChangeView.setVisibility(View.GONE);
            }
        });
        tamilApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundView.setVisibility(View.GONE);
                LanguageChangeView.setVisibility(View.GONE);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundView.setVisibility(View.VISIBLE);
                PasswordView.setVisibility(View.VISIBLE);
            }
        });
        CurrentPassword.addTextChangedListener(new TextWatcher() {
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
        NewPassword.addTextChangedListener(new TextWatcher() {
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
        ConfirmPassword.addTextChangedListener(new TextWatcher() {
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
        submitPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingView.setVisibility(View.VISIBLE);
                ProgressBarView.setVisibility(View.VISIBLE);
               FirebaseUser user= mAuth.getCurrentUser();
               if (user!=null){
                   String newPassword = Objects.requireNonNull(NewPassword.getText()).toString();
                   String currentPassword = Objects.requireNonNull(CurrentPassword.getText()).toString();
                   AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
                   user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()){
                                           Toast.makeText(getActivity(),"Password Changed Successfully",Toast.LENGTH_LONG).show();
                                       }else {
                                           Toast.makeText(getActivity(),"Password Change Error:"+ Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                                       }
                                       LoadingView.setVisibility(View.GONE);
                                       ProgressBarView.setVisibility(View.GONE);
                                       BackgroundView.setVisibility(View.GONE);
                                       PasswordView.setVisibility(View.GONE);
                                   }
                               });
                           }else {
                               LoadingView.setVisibility(View.GONE);
                               ProgressBarView.setVisibility(View.GONE);
                               BackgroundView.setVisibility(View.GONE);
                               PasswordView.setVisibility(View.GONE);
                               Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                           }
                           NewPassword.setText("");
                           ConfirmPassword.setText("");
                           CurrentPassword.setText("");

                       }
                   });


               }else {
                   Toast.makeText(getActivity(),"Error 404:User Not Found",Toast.LENGTH_LONG).show();
               }
            }
        });

        return root;
    }
    private void checkConditionsAndEnableButton() {
        String newPassword = Objects.requireNonNull(NewPassword.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(ConfirmPassword.getText()).toString().trim();
        String currentPassword = Objects.requireNonNull(CurrentPassword.getText()).toString().trim();

        boolean isNewPasswordEntered = !TextUtils.isEmpty(newPassword);
        boolean isConfirmPasswordEntered = !TextUtils.isEmpty(confirmPassword);
        boolean isCurrentPasswordEntered = !TextUtils.isEmpty(currentPassword);
        boolean isSAME = newPassword.equals(confirmPassword);
        boolean isLength = newPassword.length() > 5;

        Button submitButton = requireActivity().findViewById(R.id.SubmitPassword);
        submitButton.setEnabled(isNewPasswordEntered && isCurrentPasswordEntered && isConfirmPasswordEntered && isSAME && isLength);

        if (isNewPasswordEntered &&isCurrentPasswordEntered && isConfirmPasswordEntered && isSAME && isLength) {
            submitButton.setTextColor(getResources().getColor(R.color.white));
            submitButton.setBackgroundColor(getResources().getColor(R.color.blue));
        } else {
            submitButton.setTextColor(getResources().getColor(R.color.white));
            submitButton.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    }

}