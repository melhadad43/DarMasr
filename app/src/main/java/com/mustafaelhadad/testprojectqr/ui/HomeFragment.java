package com.mustafaelhadad.testprojectqr.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mustafaelhadad.testprojectqr.Model.Results;
import com.mustafaelhadad.testprojectqr.Model.VoteModel;
import com.mustafaelhadad.testprojectqr.R;
import com.mustafaelhadad.testprojectqr.databinding.FragmentHomeBinding;
import com.mustafaelhadad.testprojectqr.databinding.VoteItemBinding;
import com.mustafaelhadad.testprojectqr.ui.VoteAdapter.VoteHolder;

import java.util.Objects;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    NavController controller;
    FirestoreRecyclerAdapter<VoteModel, VoteAdapter.VoteHolder> adapter;
    private VoteItemBinding binding2;

    String name ;
    String adminMail ;
    String id ;
    Results results ;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        controller = Navigation.findNavController(requireActivity(), R.id.fragment);
        SharedPreferences sharedpreferences = requireActivity().getSharedPreferences("MyPREFERENCES", requireContext().MODE_PRIVATE);

         adminMail = sharedpreferences.getString("admin",null);
         id = sharedpreferences.getString("id",null);
         name = sharedpreferences.getString("userName",null);

        if (adminMail != null && adminMail.equals("true")) {
            binding.floatingActionButtonCreateVote.setVisibility(View.VISIBLE);
        } else {
            binding.floatingActionButtonCreateVote.setVisibility(View.GONE);
        }

        binding.floatingActionButtonCreateVote.setOnClickListener(view -> addVoteToHome());
        setUpRecyclerView();
        return binding.getRoot();

    }

    private void setUpRecyclerView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference voteREF = db.collection("Voting");
        Query query = voteREF.orderBy("vote", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<VoteModel> options = new FirestoreRecyclerOptions.Builder<VoteModel>()
                .setQuery(query, VoteModel.class)
                .build();

       adapter = new FirestoreRecyclerAdapter<VoteModel, VoteAdapter.VoteHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VoteAdapter.VoteHolder holder, int position, @NonNull VoteModel model) {
                results = new Results();

                binding2.textVote.setText(model.getVote());
                if (adminMail !=null && adminMail.matches("true")) {
                    binding2.btnSendVote.setEnabled(false);
                    binding2.optionsRadioGroup.setVisibility(View.GONE);
                }else {

                }

                binding2.btnSendVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Create Result
                        DocumentReference docRef = db.collection("Results").document(adapter.getItem(position).getId());

                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        if (view.equals(binding2.body)){

                                            
                                        }
                                        Toast.makeText(requireContext(), "is exists", Toast.LENGTH_SHORT).show();
                                    }else {
                                        docRef.set(results).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(requireContext(), "OK", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                                });
                    }
                });
                for (int i = 0; i < model.getOptionList().size(); i++) {
                    Log.e("TAG", "for: test"+model.getOptionList().get(i) );

                    RadioButton optionRadioButton = new RadioButton(holder.itemView.getContext());
                    optionRadioButton.setId(View.generateViewId());
                    optionRadioButton.setText(model.getOptionList().get(i));
                    optionRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b){
                                results = new Results();
                                results.setVoteOp(""+compoundButton.getText());
                                results.setIdVote(adapter.getItem(position).getId());
                                results.setIdUser(Integer.parseInt(id));
                            }
                        }
                    });
                    binding2.optionsRadioGroup.addView(optionRadioButton);

                }
            }

           @NonNull
           @Override
           public VoteModel getItem(int position) {
               return super.getItem(position);
           }

           @NonNull
            @Override
            public VoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                binding2 = VoteItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new VoteHolder(binding2);
            }
        };

        adapter.startListening();
        binding.recycler.setAdapter(adapter);

    }

    private  void  add(int i,VoteModel model){
        results.setIdUser(Integer.parseInt(id));
        results.setIdVote(model.getId());
            results.setVoteOp(model.getOptionList().get(i));

    }
    private void addVoteToHome() {
        controller.navigate(R.id.action_homeFragment_to_addVoteragment);
    }

}