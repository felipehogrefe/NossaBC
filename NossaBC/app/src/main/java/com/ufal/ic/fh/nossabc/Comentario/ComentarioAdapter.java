package com.ufal.ic.fh.nossabc.Comentario;

import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.R;
import com.ufal.ic.fh.nossabc.Painel.LoginActivity;
import com.ufal.ic.fh.nossabc.Painel.PerfilActivity;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Classe ComentarioAdapter:
 * A classe consiste em uma lista de items, os comentários, temos ainda propriedades auxiliares para
 * o tratamento de variáveis dentro de Threads paralelas. Tem como objetivo construir um RecyclerView
 * cujos itens são os comentários da lista.
 */
public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ViewHolder> {

    private ArrayList<Comentario> items;
    private static Context context;
    static UsuarioArmazLocal usuarioArmazLocal;
    CountDownLatch latch;
    int comentario_ID;
    String auxGostei;
    String auxGosteiTotal;

    public ComentarioAdapter(ArrayList<Comentario> items){ this.items = items; }

    /**
     * Cada comentário consiste em um ViewHolder, dessa forma para cada item da lista criamos um
     * objeto ViewHolder distinto.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView comentario, autorComent, gosteiTotal, notaUsuario, jaLeu, gostei;

        public ViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            usuarioArmazLocal = new UsuarioArmazLocal(context);
            //setamos o view como clicável, dessa forma o usuário pode interagir com os elementos
            itemView.setOnClickListener(this);
            gostei = (TextView) itemView.findViewById(R.id.txt_gostei);
            gostei.setOnClickListener(this);
            autorComent = (TextView) itemView.findViewById(R.id.txt_autor_coment);
            autorComent.setOnClickListener(this);
            jaLeu = (TextView) itemView.findViewById(R.id.txt_autor_ja_leu);
            notaUsuario = (TextView) itemView.findViewById(R.id.txt_autor_nota);
            gosteiTotal = (TextView) itemView.findViewById(R.id.txt_gostei_total);
            comentario = (TextView) itemView.findViewById(R.id.txt_comentario);
        }

        @Override
        public void onClick(View v) {
            //check para usuário logado, caso não logado devemos redirecioná-lo para sessão de login
            Boolean logado = UsuarioArmazLocal.autenticar(usuarioArmazLocal);
            Comentario comentario = items.get(this.getAdapterPosition());
            int id=comentario.getComentario_ID();
            switch(v.getId()){
                case R.id.txt_autor_coment:
                    //criamos o intent para iniciar PerfilActivity, para isso levamos o ID do usuário
                    Intent intentPerfil = new Intent(context, PerfilActivity.class);
                    intentPerfil.putExtra("USUARIO_ID",comentario.getAutorId());
                    context.startActivity(intentPerfil);
                    break;
                case R.id.txt_gostei:
                    if(logado){
                        //tratamento da acao referente a usuario clicar em "gostei"
                        usuarioClicouGostei(id);
                        gostei.setText(auxGostei);
                        gosteiTotal.setText(auxGosteiTotal);
                    }else{
                        Intent intentLogin = new Intent(context, LoginActivity.class);
                        context.startActivity(intentLogin);
                    }
                    break;
            }
        }
    }

    /**
     * Metodo que tem como objetivo tratar o clique no elemento Gostei, tal ação é comunicada ao
     * servidor que então irá decrementar ou incrementar o total de gosteis daqueles comentário, além
     * de alterar o elemento exibido de Gostei? para Gostei! ou de Gostei! para Gostei?.
     * @param id: ID do usuário logado
     */
    private void usuarioClicouGostei(final int id){
        latch = new CountDownLatch(1);
        Thread jaLiThread = new HandlerThread("gostouHandler") {
            @Override
            public void run() {
                try {
                    BufferedReader entrada = Conexao.conectaServidor(usuarioArmazLocal.getIdUsuario()+"\n"+id+"\n","116");
                    if(entrada.readLine().equals("-116")){
                        auxGostei="Gostei?";
                        auxGosteiTotal=entrada.readLine();
                    }else{
                        auxGostei="Gostei!";
                        auxGosteiTotal=entrada.readLine();
                    }
                    latch.countDown();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        jaLiThread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Para cada comentário é necessário setar se o usuário gostou ou não daquele comentário em parti
     * cular, dessa forma é aberta uma comunicação com o servidor. Caso tenha gostado será fixado
     * Gostei! ou Gostei? caso contrário
     * @param id: ID do usuário logado
     */
    private void usuarioGostou(final int id){
        latch = new CountDownLatch(1);
        Thread jaLiThread = new HandlerThread("gosteiHandler") {
            @Override
            public void run() {
                try {
                    BufferedReader entrada = Conexao.conectaServidor(usuarioArmazLocal.getIdUsuario()+"\n"+id+"\n","115");
                    if(entrada.readLine().equals("115")){
                        auxGostei="Gostei!";
                    }else{
                        auxGostei="Gostei?";
                    }
                    latch.countDown();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        jaLiThread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * definicao do viewHolder referente a comentarios, usamos o layout imte_comentario
     */
    @Override
    public ComentarioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context esseContext = parent.getContext();
        //infla o layour personalizado
        View comentarioView = LayoutInflater.from(esseContext).inflate(R.layout.item_comentario, parent, false);
        //retorna a nova instancia
        ViewHolder viewHolder = new ViewHolder(comentarioView);
        return viewHolder;
    }

    /**
     * Override para setar os valores dos campos do layout
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Comentario comentario = items.get(position);
        comentario_ID=comentario.getComentario_ID();

        TextView autorComentHolder = viewHolder.autorComent;
        autorComentHolder.setText(comentario.getAutorComent());

        TextView jaLeuHolder = viewHolder.jaLeu;
        jaLeuHolder.setText(comentario.getJaLeu());

        TextView notaUsuarioHolder = viewHolder.notaUsuario;
        notaUsuarioHolder.setText(comentario.getNotaUsuario());

        TextView gosteiTotalHolder = viewHolder.gosteiTotal;
        gosteiTotalHolder.setText(comentario.getGosteiTotal());
        auxGosteiTotal=comentario.getGosteiTotal();

        TextView comentarioHolder = viewHolder.comentario;
        comentarioHolder.setText(comentario.getComentario());

        TextView gosteiHolder = viewHolder.gostei;
        usuarioGostou(comentario_ID);
        gosteiHolder.setText(auxGostei);
    }

    @Override
    public int getItemCount() { return items.size(); }
}
