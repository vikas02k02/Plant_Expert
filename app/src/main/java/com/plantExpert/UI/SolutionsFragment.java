package com.plantExpert.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.plantExpert.R;

import java.util.ArrayList;
import java.util.Objects;


public class SolutionsFragment extends Fragment {
    ListView solutionView;


    public SolutionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_solutions, container, false);
        solutionView = root.findViewById(R.id.recyclerView);
        ArrayList<String> plantArray = new ArrayList<>();
        plantArray.add("Cotton");
        plantArray.add("Sunflower");
        plantArray.add("Cucumber");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireActivity(), R.layout.solution_layout,R.id.plantNameDB ,plantArray);
        solutionView.setAdapter(arrayAdapter);
        return root ;
    }
}