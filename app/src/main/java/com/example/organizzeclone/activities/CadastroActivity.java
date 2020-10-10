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
import com.example.organizzeclone.dao.DAOFactory;
import com.example.organizzeclone.database.FirebaseManager;
import com.example.organizzeclone.model.Conta;
import com.example.organizzeclone.model.Usuario;
import com.example.organizzeclone.activities.exceptions.ValidationException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText txtNome;
    private EditText txtEmail;
    private EditText txtSenha;
    private Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        getSupportActionBar().setTitle("Cadastro");

        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btnCadastrar = findViewById(R.id.btnEntrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateCadastro();
                    createUsuario();
                } catch (ValidationException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validateCadastro() {
        String err;
        String tag = "Error";

        if (txtNome.getText().toString().trim().isEmpty()) {
            err = "Nome de usuário vazio";
            Log.i(tag, err);
            throw new ValidationException(err);
        }

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

    private void createUsuario() {
        FirebaseManager.getFirebaseAuth().createUserWithEmailAndPassword(
                txtEmail.getText().toString().trim(),
                txtSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Conta conta = new Conta(null, "Conta Principal", task.getResult().getUser().getUid());
                            String id = DAOFactory.getContaDAO().insert(conta);

                            Usuario usuario = new Usuario(task.getResult().getUser().getUid(), txtNome.getText().toString(), txtEmail.getText().toString(), txtSenha.getText().toString());
                            usuario.getContas().add(id);
                            DAOFactory.getUsuarioDAO().insert(usuario);

                            Toast.makeText(getApplicationContext(), "Usuário criado com sucesso!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(getApplicationContext(), "Senha com menos de 6 caracteres", Toast.LENGTH_LONG).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(), "Um ou mais parâmetros de autenticação estão inválidos", Toast.LENGTH_LONG).show();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(getApplicationContext(), "Este usuário já existe", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Erro ao tentar criar usuário", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
