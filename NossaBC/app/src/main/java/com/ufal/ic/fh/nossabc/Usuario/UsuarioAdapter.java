package com.ufal.ic.fh.nossabc.Usuario;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ufal.ic.fh.nossabc.R;
import java.util.ArrayList;

/**
 * Created by Felipe on 25/04/2016.
 */
public class UsuarioAdapter  extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {
    private static OnItemClickListener listener;
    private static ArrayList<MetaUsuario> items;

    public UsuarioAdapter(ArrayList<MetaUsuario> items){
        this.items = items;
    }


    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nome;
        public TextView curso;

        public ViewHolder(final View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.nomeUsuario);
            curso = (TextView) itemView.findViewById(R.id.cursoUsuario);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }

    @Override
    public UsuarioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        //infla o layour personalizado
        View usuarioView = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        //retorna a nova instancia
        ViewHolder viewHolder = new ViewHolder(usuarioView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UsuarioAdapter.ViewHolder holder, int position) {
        MetaUsuario metaUsuario = items.get(position);
        TextView nomeHolder = holder.nome;
        nomeHolder.setText(metaUsuario.getNome());
        TextView cursoHolder = holder.curso;
        cursoHolder.setText(metaUsuario.getCurso());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
