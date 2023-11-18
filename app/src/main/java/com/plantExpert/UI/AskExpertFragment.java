package com.plantExpert.UI;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.plantExpert.R;


public class AskExpertFragment extends Fragment {
    private TextInputEditText cityName , plantName;
    private Button sendData ;
    private AppCompatButton record;
    private LinearLayout ProgressView , ResponseView;
    private View ProcessView ;
    private TextView ResponseMessage ,OkButton;


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
        ResponseMessage = root.findViewById(R.id.responseMessage);
        OkButton = root.findViewById(R.id.OK);

        sendData.setOnClickListener(view -> {
            ProcessView.setVisibility(View.VISIBLE);
            ProgressView.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                ProgressView.setVisibility(View.GONE);
                ResponseView.setVisibility(View.VISIBLE);
            },2000);

        });

        OkButton.setOnClickListener(view -> {
            AskExpertFragment askExpertFragment = new AskExpertFragment();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,askExpertFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });


        return root ;
    }
}