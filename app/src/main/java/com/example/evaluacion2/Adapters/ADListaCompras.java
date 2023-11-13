package com.example.evaluacion2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluacion2.R;
import com.example.evaluacion2.ViewHolder.viewHolderCompras;
import com.example.evaluacion2.Clases.CompraDetalle;

import java.util.List;

public class ADListaCompras extends RecyclerView.Adapter<viewHolderCompras> {

    private OnItemClickListener onItemClickListener;
    private List<CompraDetalle> datos;

    public ADListaCompras(List<CompraDetalle> datos, OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.datos = datos;
    }

    @NonNull
    @Override
    public viewHolderCompras onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compras_diarias, parent,false);
        return new viewHolderCompras(view);    }
    @Override
    public void onBindViewHolder(@NonNull viewHolderCompras holder, int position) {
        final CompraDetalle detalle = datos.get(position);
        holder.getTvNombre().setText(datos.get(position).getNombre() + "");
        holder.getTvCantidad().setText(datos.get(position).getCantidad() + "");
        holder.getTvPrecio().setText(datos.get(position).getPrecio() + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(detalle);
                }
            }
        });
    }
    @Override
    public int getItemCount() {return datos.size();}
    public interface OnItemClickListener {
        void onItemClick(CompraDetalle detalle);
    }
}
