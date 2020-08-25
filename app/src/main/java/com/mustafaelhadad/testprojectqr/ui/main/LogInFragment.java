package com.mustafaelhadad.testprojectqr.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mustafaelhadad.testprojectqr.Model.UserModel;
import com.mustafaelhadad.testprojectqr.R;
import com.mustafaelhadad.testprojectqr.databinding.FragmentLogInBinding;

import java.util.ArrayList;

public class LogInFragment extends Fragment {


    private static final String TAG = "Login Fragment";
    NavController controller;
    FragmentLogInBinding binding;
    FirebaseFirestore db;
    Bundle bundle;
    UserModel userModel ;
    QueryDocumentSnapshot document1 ;
    ArrayList<String> list ;
    public LogInFragment() {
    }
    SharedPreferences sharedpreferences ;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        controller = Navigation.findNavController(requireActivity(), R.id.fragment);
        db = FirebaseFirestore.getInstance();


        binding.btnLogin.setOnClickListener(view -> {
            String valNumber = binding.emailLogin.getText().toString();
            if (valNumber.isEmpty()) {
                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(valNumber);
            }
        });
        return binding.getRoot();

    }

    private void valditeEmail() {

        String valEmail = binding.emailLogin.getText().toString();
        if (valEmail.isEmpty()) {
            binding.emailLogin.setError("Please fill the form");
        } else {

            // Navigate from LogIn to Hom Fragment with adminMail argument (String)




        }
    }



    private void    loginUser(String id){

        DocumentReference docRef = db.collection("Users").document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getId());
                        userModel = new UserModel() ;
                        userModel.setId(id);
                        userModel.setAdmin(""+document.get("admin"));
                        userModel.setUserName(""+document.get("userName"));

                        sharedpreferences = requireActivity().getSharedPreferences("MyPREFERENCES", requireContext().MODE_PRIVATE);

                        editor = sharedpreferences.edit();

                        editor.putString("id", id);
                        editor.putString("userName",userModel.getUserName() );
                        editor.putString("admin",userModel.isAdmin() );
                        editor.commit();
                        controller.navigate(R.id.action_logInFragment_to_homeFragment);


                    } else {
                        AlertDialog.Builder aler = new AlertDialog.Builder(requireContext());
                        aler.setTitle("انت لست مسجل من فضلك اضغط علي تسجيل");
                        aler.setMessage("SingIn ?");
                        aler.setPositiveButton("تسجيل", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SingInNewUser(id);

                            }
                        });
                        aler.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        aler.show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
    public  synchronized String getUserId ( QueryDocumentSnapshot id){
        return   id.getId();
    }
    public  void  SingInNewUser(String id ){
        UserModel userModel = new UserModel();
        userModel.setUserName(userModel.getUserName());
        userModel.setAdmin("false");
        userModel.setId(""+id);


        db.collection("Users").document(id)
                .set(userModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(getContext(), task.getResult().toString(), Toast.LENGTH_SHORT).show();

                        if (task.isSuccessful()) {
                            sharedpreferences = requireActivity().getSharedPreferences("MyPREFERENCES", requireContext().MODE_PRIVATE);

                            editor = sharedpreferences.edit();
                            editor.putString("id", id);
                            editor.putString("userName", userModel.getUserName());
                            editor.putString("admin", userModel.isAdmin());
                            editor.commit();
                            controller.navigate(R.id.action_logInFragment_to_homeFragment);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

}
