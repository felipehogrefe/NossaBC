package com.ufal.ic.fh.nossabc.Mensagens;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.R;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Felipe on 13/04/2016.
 */
public class MensagensAdapter extends RecyclerView.Adapter<MensagensAdapter.ViewHolder>{
    private ArrayList<MetaMensagem> items;
    private static Context context;
    String mensagem_ID;
    static UsuarioArmazLocal usuarioArmazLocal;
    private static OnItemClickListener listener;
    CountDownLatch latch;

    public MensagensAdapter(ArrayList<MetaMensagem> items){ this.items = items; }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nomeSender, snapMensagem, nomeAlvo;

        public ViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            usuarioArmazLocal = new UsuarioArmazLocal(context);

            nomeSender = (TextView) itemView.findViewById(R.id.nomeSender);
            snapMensagem = (TextView) itemView.findViewById(R.id.snapMensagem);
            nomeAlvo = (TextView) itemView.findViewById(R.id.nomeAlvo);

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
    public MensagensAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context esseContext = parent.getContext();
        //infla o layour personalizado
        View mensagemView = LayoutInflater.from(esseContext).inflate(R.layout.item_snap_mensagem, parent, false);
        //retorna a nova instancia
        ViewHolder viewHolder = new ViewHolder(mensagemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MensagensAdapter.ViewHolder viewHolder, int position) {
        MetaMensagem metaMensagem = items.get(position);
        mensagem_ID = metaMensagem.getMensagemId();
        JSONObject dados = dadosMensagem(mensagem_ID);

        TextView nomeSenderHolder = viewHolder.nomeSender;
        TextView snapMensagemHolder = viewHolder.snapMensagem;
        TextView nomeAlvoHolder = viewHolder.nomeAlvo;

        try{
            metaMensagem.setAlvo(dados.getString("alvoId"));
            nomeSenderHolder.setText(dados.getString("sender")+":");
            snapMensagemHolder.setText(dados.getString("snap"));
            nomeAlvoHolder.setText(dados.getString("alvo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject dadosMensagem(String mensagem_ID){
        JSONObject dados;

        try{
            dados = Conexao.jConectaServidor("402",mensagem_ID+"\n"+usuarioArmazLocal.getIdUsuario());
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


