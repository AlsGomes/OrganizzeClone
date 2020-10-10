package com.example.organizzeclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizzeclone.R;
import com.example.organizzeclone.model.Movimentacao;
import com.example.organizzeclone.model.enums.Tipo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoAdapter extends RecyclerView.Adapter<MovimentacaoAdapter.MovimentacaoViewHolder> {

    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private Context context;

    public MovimentacaoAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MovimentacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movimentacoes, parent, false);
        return new MovimentacaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovimentacaoViewHolder holder, int position) {
        holder.txtDescricao.setText(movimentacoes.get(position).getDescricao());

        holder.txtValor.setText(String.format("%.2f", movimentacoes.get(position).getValor()));
        holder.txtValor.setTextColor(
                movimentacoes.get(position).getTipo().equals(Tipo.DESPESA) ?
                        context.getResources().getColor(R.color.colorPrimaryDespesa)
                        : context.getResources().getColor(R.color.colorPrimaryReceita)
        );

        holder.txtCategoria.setText(movimentacoes.get(position).getCategoria().getNome());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        holder.txtData.setText(sdf.format(movimentacoes.get(position).getData()));
    }

    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    public class MovimentacaoViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDescricao;
        private TextView txtValor;
        private TextView txtCategoria;
        private TextView txtData;

        public MovimentacaoViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtValor = itemView.findViewById(R.id.txtValor);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            txtData = itemView.findViewById(R.id.txtData);
        }
    }

    public List<Movimentacao> getMovimentacoes() {
        return movimentacoes;
    }
}
