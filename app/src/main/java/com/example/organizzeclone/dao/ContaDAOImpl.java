package com.example.organizzeclone.dao;

import androidx.annotation.NonNull;

import com.example.organizzeclone.database.FirebaseManager;
import com.example.organizzeclone.model.Conta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ContaDAOImpl implements ContaDAO {

    @Override
    public String insert(Conta obj) {
        String id = FirebaseManager.getDatabaseReference().child("contas").push().getKey();
        FirebaseManager.getDatabaseReference().child("contas").child(id).setValue(obj)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
        ;
        return id;
    }

    @Override
    public String update(Conta obj) {
        FirebaseManager.getDatabaseReference().child("contas").child(obj.getId()).setValue(obj)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
        ;
        return obj.getId();
    }
}
