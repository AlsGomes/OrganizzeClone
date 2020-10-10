package com.example.organizzeclone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizzeclone.R;
import com.example.organizzeclone.activities.exceptions.ValidationException;
import com.example.organizzeclone.dao.DAOFactory;
import com.example.organizzeclone.database.FirebaseManager;
import com.example.organizzeclone.model.Conta;
import com.example.organizzeclone.model.Movimentacao;
import com.example.organizzeclone.model.Usuario;
import com.example.organizzeclone.model.enums.Categoria;
import com.example.organizzeclone.model.enums.Tipo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ReceitaActivity extends AppCompatActivity {

    private EditText txtValor;
    private EditText txtData;
    private EditText txtDescricao;
    private EditText txtCategoria;
    private FloatingActionButton fab;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        txtValor = findViewById(R.id.txtValor);
        txtData = findViewById(R.id.txtData);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtCategoria = findViewById(R.id.txtCategoria);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDespesa();
            }
        });
    }

    private void saveDespesa() {
        try {
            validateMovimentacao();

            final Movimentacao movimentacao = new Movimentacao();
            try {
                movimentacao.setId(UUID.randomUUID().toString());
                movimentacao.setTipo(Tipo.RECEITA);
                movimentacao.setDescricao(txtDescricao.getText().toString().trim());
                movimentacao.setCategoria(Categoria.toEnum(txtCategoria.getText().toString().trim()));
                double valor = Double.parseDouble(txtValor.getText().toString().trim());
                movimentacao.setValor(valor);
                movimentacao.setData(sdf.parse(txtData.getText().toString().trim()));
            } catch (ParseException e) {
                throw new ValidationException("Formato de data inválido");
            }

            FirebaseManager.getDatabaseReference()
                    .child("usuarios")
                    .child(FirebaseManager.getFirebaseAuth().getCurrentUser().getUid()
                    ).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    FirebaseManager.getDatabaseReference()
                            .child("contas")
                            .child(usuario.getContas().get(0)
                            ).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Conta conta = dataSnapshot.getValue(Conta.class);
                            conta.setId(dataSnapshot.getKey());
                            conta.getMovimentacoes().put(movimentacao.getId(), movimentacao);
                            DAOFactory.getContaDAO().update(conta);

                            Toast.makeText(getApplicationContext()
                                    , "Movimentação inserida com sucesso!"
                                    , Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (ValidationException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.i("Error", e.getMessage());
        }
    }

    private boolean validateMovimentacao() {
        if (txtValor.getText().toString().trim().isEmpty()) {
            throw new ValidationException("Valor vazio");
        }

        try {
            Double valor = Double.parseDouble(txtValor.getText().toString().trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Formato de valor inválido");
        }

        if (txtData.getText().toString().trim().isEmpty()) {
            throw new ValidationException("Data vazia");
        }

        try {
            Date data = sdf.parse(txtData.getText().toString().trim());
        } catch (ParseException e) {
            throw new ValidationException("Formato de data inválido");
        }

        if (txtDescricao.getText().toString().trim().isEmpty()) {
            throw new ValidationException("Descrição vazia");
        }

        if (txtCategoria.getText().toString().trim().isEmpty()) {
            throw new ValidationException("Categoria vazia");
        }

        try {
            Categoria categoria = Categoria.toEnum(txtCategoria.getText().toString().trim());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Categoria não encontrada");
        }

        return true;
    }
}
