package com.plantExpert.UI;

import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.plantExpert.R;
import com.plantExpert.data.AskExpert.ProblemsDetails;

import java.io.IOException;
import java.util.Objects;


public class AskExpertFragment extends Fragment {
    private TextInputEditText cityName , plantName;
    private Button sendData    ;
    private AppCompatButton record ,CloseRecording,RecordFinish;
    private FloatingActionButton floatingActionButton;
    private LinearLayout ProgressView , ResponseView ,recordingView;
    private View ProcessView ;
    private ImageView diseaseImage;
    private TextView ResponseMessage ,OkButton;
    private Uri uploadedImage;
    boolean isRecording= false;
    String AudioFilePath ;
    MediaRecorder mediaRecorder;
    MediaRecorder recorder;
    private String Plant_Name ,City_Name;
    FirebaseAuth mAuth;


    public AskExpertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ask_expert, container, false);
        plantName = root.findViewById(R.id.PlantName);
        cityName = root.findViewById(R.id.cityName);
        sendData = root.findViewById(R.id.sendData);
        record = root.findViewById(R.id.RecordVoice);
        ProgressView = root.findViewById(R.id.ProgressView);
        ResponseView = root.findViewById(R.id.responseView);
        ProcessView = root.findViewById(R.id.ProcessView);
        diseaseImage= root.findViewById(R.id.diseaseImage);
        floatingActionButton= root.findViewById(R.id.captureImage);
        ResponseMessage = root.findViewById(R.id.responseMessage);
        OkButton = root.findViewById(R.id.OK);
        CloseRecording = root.findViewById(R.id.recordExit);
        RecordFinish = root.findViewById(R.id.recordStop);
        recordingView=root.findViewById(R.id.RecordingView);
        mAuth = FirebaseAuth.getInstance();
        cityName.addTextChangedListener(new TextWatcher() {
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

        plantName.addTextChangedListener(new TextWatcher() {
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

        sendData.setOnClickListener(view -> {
            if (uploadedImage != null) {
                diseaseImage.setBackgroundColor(getResources().getColor(R.color.white));
                ProcessView.setVisibility(View.VISIBLE);
                ProgressView.setVisibility(View.VISIBLE);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference problemReference = database.getReference().child("Problems");
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    String user = Objects.requireNonNull(currentUser.getEmail()).substring(0, 10);
                    DatabaseReference userReference = database.getReference().child("Farmer").child(user);
                    String pId = problemReference.push().getKey();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference imageReference = storage.getReference().child("PlantImage/" + pId + ".jpg");
                    UploadTask uploadTask = imageReference.putFile(uploadedImage);

                    uploadTask.addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                if (uri != null) {
                                    Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_LONG).show();
                                    ProblemsDetails problemsDetails = new ProblemsDetails(Plant_Name, City_Name, uri.toString());

                                    assert pId != null;
                                    problemReference.child(pId).setValue(problemsDetails).addOnCompleteListener(innerTask -> {
                                        if (innerTask.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Problem Stored", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    userReference.child("Problem Ids").setValue(pId).addOnCompleteListener(innerTask -> {
                                        if (innerTask.isSuccessful()) {
                                            ProgressView.setVisibility(View.GONE);
                                            ResponseView.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else {
                    // Handle the case where currentUser is null
                }
            } else {
                diseaseImage.setBackgroundColor(getResources().getColor(R.color.red));
            }
        });


        OkButton.setOnClickListener(view -> {
            AskExpertFragment askExpertFragment = new AskExpertFragment();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,askExpertFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AskExpertFragment.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        diseaseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AskExpertFragment.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        AudioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/newAudio.mp3";
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProcessView.setVisibility(View.VISIBLE);
                recordingView.setVisibility(View.VISIBLE);
//                startRecording();

            }
        });
        CloseRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProcessView.setVisibility(View.GONE);
                recordingView.setVisibility(View.GONE);
//                stopRecording();
            }
        });
        RecordFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProcessView.setVisibility(View.GONE);
                recordingView.setVisibility(View.GONE);
                stopRecording();
            }
        });

        return root ;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uploadedImage =data.getData();
        diseaseImage.setImageURI(uploadedImage);

    }
    private void checkConditionsAndEnableButton() {
        City_Name = Objects.requireNonNull(cityName.getText()).toString().trim();
        Plant_Name = Objects.requireNonNull(plantName.getText()).toString().trim();
        boolean isCityNameEntered = !TextUtils.isEmpty(City_Name);
        boolean isPlantNameEntered = !TextUtils.isEmpty(Plant_Name);

        Button submitButton = requireActivity().findViewById(R.id.sendData);
        submitButton.setEnabled(isCityNameEntered && isPlantNameEntered);

        if (isCityNameEntered && isPlantNameEntered) {
            submitButton.setTextColor(getResources().getColor(R.color.white));
            submitButton.setBackgroundColor(getResources().getColor(R.color.blue));
        } else {
            submitButton.setTextColor(getResources().getColor(R.color.white));
            submitButton.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    }
    private void startRecording() {
        try{
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(Environment.getExternalStorageDirectory().getPath()+"/temp.3gp");
            recorder.prepare();
            recorder.start();
        }catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
    }


    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

}
