package com.example.organizzeclone.dao;

import androidx.annotation.NonNull;

import com.example.organizzeclone.database.FirebaseManager;
import com.example.organizzeclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public boolean insert(Usuario obj) {
        FirebaseManager.getDatabaseReference().child("usuarios").child(obj.getId()).setValue(obj)
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

        return true;
    }
}
