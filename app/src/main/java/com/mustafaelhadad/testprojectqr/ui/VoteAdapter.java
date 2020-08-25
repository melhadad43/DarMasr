package com.mustafaelhadad.testprojectqr.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.mustafaelhadad.testprojectqr.Model.VoteModel;
import com.mustafaelhadad.testprojectqr.databinding.VoteItemBinding;

public class VoteAdapter extends FirestoreRecyclerAdapter<VoteModel, VoteAdapter.VoteHolder> {

    private VoteItemBinding binding;

    public VoteAdapter(@NonNull FirestoreRecyclerOptions<VoteModel> options) {
        super(options);
    }


    public static class VoteHolder extends RecyclerView.ViewHolder {

        public VoteHolder(@NonNull final VoteItemBinding binding) {
            super(binding.getRoot());

        }
    }

    @NonNull
    @Override
    public VoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = VoteItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VoteHolder(binding);
    }

    @Override
    protected void onBindViewHolder(@NonNull VoteHolder holder, int position, @NonNull VoteModel model) {
        binding.textVote.setText(model.getVote());
        Log.e("TAG", "onBindViewHolder: vote" + model.getVote());
        Log.e("TAG", "onBindViewHolder: vote" + model.getId());
        Log.e("TAG", "onBindViewHolder: Options " + model.getOptionList().size());

        for (int i = 0; i < model.getOptionList().size(); i++) {
             Log.e("TAG", "for: test"+model.getOptionList().get(i) );

            RadioButton optionRadioButton = new RadioButton(holder.itemView.getContext());
            optionRadioButton.setId(View.generateViewId());
            optionRadioButton.setText(model.getOptionList().get(i));
            binding.optionsRadioGroup.addView(optionRadioButton);
        }

    }


}
