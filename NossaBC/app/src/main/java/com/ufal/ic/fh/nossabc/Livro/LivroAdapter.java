package com.ufal.ic.fh.nossabc.Livro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ufal.ic.fh.nossabc.R;

import java.util.ArrayList;

/**
 * Adpater referente a construção dos itens a serem apresentados no Recycler de resultados de uma
 * consulta
 */
public class LivroAdapter extends RecyclerView.Adapter<LivroAdapter.ViewHolder> {
    private static OnItemClickListener listener;
    private ArrayList<MetaLivro> items;
    public LivroAdapter(ArrayList<MetaLivro> items){
        this.items = items;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView titulo;
        public TextView edicao;
        public TextView status;

        //é necessário setar o item como clicável:
        public ViewHolder(final View itemView) {
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.txt_titulo_livro);
            edicao = (TextView) itemView.findViewById(R.id.txt_edicao_livro);
            status = (TextView) itemView.findViewById(R.id.txt_status_livro);
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
    public LivroAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        //infla o layour personalizado
        View livroView = LayoutInflater.from(context).inflate(R.layout.item_meta_livro, parent, false);
        //retorna a nova instancia
        ViewHolder viewHolder = new ViewHolder(livroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        MetaLivro metaLivro = items.get(position);
        TextView tituloHolder = viewHolder.titulo;
        tituloHolder.setText(metaLivro.getTitulo());
        TextView edicaoHolder = viewHolder.edicao;
        edicaoHolder.setText(metaLivro.getEdicao());
        TextView statusHolder = viewHolder.status;
        statusHolder.setText(metaLivro.getStringStatus());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
