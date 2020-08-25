package com.mustafaelhadad.testprojectqr.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mustafaelhadad.testprojectqr.Model.VoteModel;
import com.mustafaelhadad.testprojectqr.R;
import com.mustafaelhadad.testprojectqr.databinding.FragmentAddVoteragmentBinding;

import java.util.ArrayList;
import java.util.List;


public class AddVoteragment extends Fragment {

    List<String> optionList = new ArrayList<>();
    private static final String TAG = "Add Voting Fragment";
    FragmentAddVoteragmentBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public AddVoteragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentAddVoteragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnAddVote.setOnClickListener(view12 -> {
            String valVote = binding.editTextAddVote.getText().toString();
            String valOptions = binding.textAddOption.getText().toString();
            if (valVote.isEmpty() || valOptions.isEmpty()){
                Toast.makeText(requireContext(), "Please Fill The Vote", Toast.LENGTH_SHORT).show();
            } else {
                sendVoteToFirebase();

            }
        });

        binding.buttonComplateOption.setOnClickListener(view1 -> {
            String optionText = binding.textAddOption.getText().toString();
            optionList.add(optionText);
            binding.textAddOption.setText("");
            binding.textAddOption.setHint("اضافه اختيار");
        });

    }

    private void sendVoteToFirebase(){

        NavController controller = Navigation.findNavController(requireActivity(), R.id.fragment);

        String addVoteEditText = binding.editTextAddVote.getText().toString();
        CollectionReference voteReference = FirebaseFirestore.getInstance()
                .collection("Voting");

        voteReference.add(new VoteModel(addVoteEditText,optionList,"")).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess: " + documentReference.getId());
                voteReference.document(""+documentReference.getId())
                        .set(new VoteModel(addVoteEditText,optionList,documentReference.getId()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
        Toast.makeText(getContext(), "Vote added", Toast.LENGTH_SHORT).show();
        controller.navigate(R.id.action_global_homeFragment);

    }


}