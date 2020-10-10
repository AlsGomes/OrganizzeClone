package com.example.organizzeclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizzeclone.R;
import com.example.organizzeclone.activities.exceptions.ValidationException;
import com.example.organizzeclone.database.FirebaseManager;
import com.example.organizzeclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtSenha;
    private Button btnEntrar;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateLogin();
                    loginUsuario();
                } catch (ValidationException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validateLogin() {
        String err;
        String tag = "Error";

        if (txtEmail.getText().toString().trim().isEmpty()) {
            err = "E-mail do usuário vazio";
            Log.i(tag, err);
            throw new ValidationException(err);
        }

        if (txtSenha.getText().toString().trim().isEmpty()) {
            err = "Senha do usuário vazia";
            Log.i(tag, err);
            throw new ValidationException(err);
        }

        return true;
    }

    private void loginUsuario() {
        FirebaseManager.getFirebaseAuth().signInWithEmailAndPassword(
                txtEmail.getText().toString(),
                txtSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Bem-vindo!", Toast.LENGTH_LONG)
                                    .show();
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(), "Um ou mais parâmetros de autenticação estão inválidos", Toast.LENGTH_LONG).show();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(getApplicationContext(), "Este usuário não existe", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Erro ao tentar se autenticar", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
