package com.example.organizzeclone.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.organizzeclone.adapters.MovimentacaoAdapter;
import com.example.organizzeclone.dao.DAOFactory;
import com.example.organizzeclone.database.FirebaseManager;
import com.example.organizzeclone.model.Conta;
import com.example.organizzeclone.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organizzeclone.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView txtNome;
    private TextView txtSaldo;
    private RecyclerView recyclerView;
    private MovimentacaoAdapter adapter;
    private Usuario usuario;
    private ValueEventListener valueEventListener;
    private DatabaseReference contasReferencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initControllers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initListeners();
        Log.i("Listeners", "Listener da referência para contas foi adicionado");

        Calendar currentDate = calendarView.getCurrentDate().getCalendar();
        Calendar c = (Calendar) currentDate.clone();
        c.add(Calendar.MONTH, 1);
        calendarView.setCurrentDate(c);
        calendarView.setCurrentDate(currentDate);
    }

    @Override
    protected void onStop() {
        super.onStop();
        contasReferencia.removeEventListener(valueEventListener);
        Log.i("Listeners", "Listener da referência para contas foi removido");
    }

    private void initListeners() {
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                final CalendarDay data = date;
                FirebaseManager.getDatabaseReference()
                        .child("usuarios")
                        .child(FirebaseManager.getFirebaseAuth().getCurrentUser().getUid()
                        ).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usuario = dataSnapshot.getValue(Usuario.class);
                        usuario.setId(dataSnapshot.getKey());
                        txtNome.setText(usuario.getNome());

                        contasReferencia = FirebaseManager.getDatabaseReference()
                                .child("contas")
                                .child(usuario.getContas().get(0)
                                );

                        if (valueEventListener != null) {
                            contasReferencia.removeEventListener(valueEventListener);
                        }

                        valueEventListener = contasReferencia.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Conta conta = dataSnapshot.getValue(Conta.class);
                                writeSaldo(conta.getSaldo());
                                updateAdapter(conta, data.getMonth());
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
            }
        });
    }

    private void updateAdapter(Conta conta, int month) {
        adapter.getMovimentacoes().clear();
        adapter.getMovimentacoes().addAll(conta.getMovimentacoes(month).values());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void writeSaldo(Double saldo) {
        DecimalFormat df = new DecimalFormat("R$ 0.00");
        txtSaldo.setText(df.format(saldo));
    }

    private void initControllers() {
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        calendarView = findViewById(R.id.calendarView);
        String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Junho", "Julho", "Ago", "Set", "Out", "Nov", "Dez"};
        calendarView.setTitleMonths(meses);

        txtNome = findViewById(R.id.txtNome);
        txtSaldo = findViewById(R.id.txtSaldo);

        adapter = new MovimentacaoAdapter(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        ItemTouchHelper.Callback itemTouchHelper = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                deleteMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
    }

    private void deleteMovimentacao(RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        final String idMovimentacao = adapter.getMovimentacoes().get(position).getId();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Deseja realmente efetuar a exclusão desta movimentação?");
        dialog.setTitle("Exclusão");
        dialog.setCancelable(false);
        dialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                contasReferencia
                        .child("movimentacoes")
                        .child(idMovimentacao)
                        .removeValue();
                updateConta();
                adapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Movimentação excluída com sucesso", Toast.LENGTH_LONG).show();
            }
        });
        dialog.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.notifyDataSetChanged();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void updateConta() {
        contasReferencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Conta conta = dataSnapshot.getValue(Conta.class);
                conta.setId(dataSnapshot.getKey());
                DAOFactory.getContaDAO().update(conta);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnSair:
                FirebaseManager.getFirebaseAuth().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startReceitaActivity(View view) {
        startActivity(new Intent(getApplicationContext(), ReceitaActivity.class));
    }

    public void startDespesaActivity(View view) {
        startActivity(new Intent(getApplicationContext(), DespesaActivity.class));
    }

}
