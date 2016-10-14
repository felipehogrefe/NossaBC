package com.ufal.ic.fh.nossabc.Solicitacao;

import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.R;
import com.ufal.ic.fh.nossabc.Painel.PerfilActivity;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Felipe on 13/04/2016.
 */
public class SolicitacaoAdapter extends RecyclerView.Adapter<SolicitacaoAdapter.ViewHolder>{
    private ArrayList<MetaSolicitacao> items;
    String solicitante_ID;
    private static Context context;
    static CountDownLatch latch;
    static UsuarioArmazLocal usuarioArmazLocal;
    static String aceitar;

    public SolicitacaoAdapter(ArrayList<MetaSolicitacao> items){ this.items = items; }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nomeSolicitante, cursoSolicitante, aceitar, recusar;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            usuarioArmazLocal = new UsuarioArmazLocal(context);
            itemView.setOnClickListener(this);

            nomeSolicitante = (TextView) itemView.findViewById(R.id.nomeSolicitante);
            cursoSolicitante = (TextView) itemView.findViewById(R.id.cursoSolicitante);
            aceitar = (TextView) itemView.findViewById(R.id.btnAceitar);
            recusar = (TextView) itemView.findViewById(R.id.btnRecusar);
            nomeSolicitante.setOnClickListener(this);
            aceitar.setOnClickListener(this);
            recusar.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            MetaSolicitacao solicitacao = items.get(this.getAdapterPosition());
            switch(v.getId()){
                case R.id.nomeSolicitante:
                    Intent intentPerfil = new Intent(context, PerfilActivity.class);
                    intentPerfil.putExtra("USUARIO_ID",solicitacao.getSolicitanteId());
                    context.startActivity(intentPerfil);
                    break;
                case R.id.btnAceitar:
                    //conecta com o servidor para avisar que aceitou
                    if(acaoSolicitacao("a\n")){
                        alteraBtns("Amigo Aceito.");
                    }
                    break;
                case R.id.btnRecusar:
                    //conecta com o servidor para avisar que recusou
                    if(acaoSolicitacao("r\n")) {
                        alteraBtns("Amigo Rescusado.");
                    }
                    break;
                default:
                    break;
            }
        }

        private void alteraBtns(String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            aceitar.setText("");
            aceitar.setClickable(false);
            recusar.setText("");
            recusar.setClickable(false);
        }
    }



    private boolean acaoSolicitacao(final String funcao) {
        latch = new CountDownLatch(1);
        Thread jaLiThread = new HandlerThread("conexaoHandler") {
            @Override
            public void run() {
                try {
                    aceitar = Conexao.conectaServidor(funcao+usuarioArmazLocal.getIdUsuario()+"\n"+solicitante_ID,"305").readLine();
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
        if(aceitar.equals("305")||aceitar.equals("-305")){
            return true;
        }
        return false;
    }

    @Override
    public SolicitacaoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context esseContext = parent.getContext();
        //infla o layour personalizado
        View solicitacaoView = LayoutInflater.from(esseContext).inflate(R.layout.item_solicitacao, parent, false);
        //retorna a nova instancia
        ViewHolder viewHolder = new ViewHolder(solicitacaoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SolicitacaoAdapter.ViewHolder viewHolder, int position) {
        MetaSolicitacao metaSolicitacao = items.get(position);
        solicitante_ID = metaSolicitacao.getSolicitanteId();
        JSONObject dados = dadosSolicitante(solicitante_ID);

        TextView nomeSolicitanteHolder = viewHolder.nomeSolicitante;
        TextView cursoSolicitanteHolder = viewHolder.cursoSolicitante;

        try{
            cursoSolicitanteHolder.setText(dados.getString("curso"));
            nomeSolicitanteHolder.setText(dados.getString("nome"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject dadosSolicitante(String solicitante_ID){
        JSONObject dados;
        try{
            dados = Conexao.jConectaServidor("205",solicitante_ID);
            return dados;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}


