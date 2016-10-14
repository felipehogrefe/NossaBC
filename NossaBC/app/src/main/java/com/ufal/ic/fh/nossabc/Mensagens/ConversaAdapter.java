package com.ufal.ic.fh.nossabc.Mensagens;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
public class ConversaAdapter extends RecyclerView.Adapter<ConversaAdapter.ViewHolder>{
    private ArrayList<MetaMensagem> items;
    private static Context context;
    static UsuarioArmazLocal usuarioArmazLocal;
    static ViewHolder viewHolder;

    public ConversaAdapter(ArrayList<MetaMensagem> items){ this.items = items; }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mensagem;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            usuarioArmazLocal = new UsuarioArmazLocal(context);

            mensagem = (TextView) itemView.findViewById(R.id.txtMensagem);
        }

    }

    @Override
    public ConversaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context esseContext = parent.getContext();
        //infla o layour personalizado
        View mensagemView = LayoutInflater.from(esseContext).inflate(R.layout.item_mensagem, parent, false);
        //retorna a nova instancia
        ViewHolder viewHolder = new ViewHolder(mensagemView);
        this.viewHolder=viewHolder;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ConversaAdapter.ViewHolder viewHolder, int position) {
        MetaMensagem metaMensagem = items.get(position);

        if(metaMensagem.dados==null){
            dadosSolicitante(metaMensagem);
        }

        TextView mensagemHolder = viewHolder.mensagem;
        try{
            //setamos o lado em que a mensagem irá ser exibida para dar a ideia de emissor e receptor
            if(usuarioArmazLocal.getIdUsuario().equals(metaMensagem.dados.getString("sender"))){
                System.out.println("right: "+metaMensagem.dados.getString("mensagem"));
                mensagemHolder.setGravity(Gravity.RIGHT);
            }else{

                System.out.println("left: "+metaMensagem.dados.getString("mensagem"));
                mensagemHolder.setGravity(Gravity.LEFT);
                //colocar imagem do alvo
            }
            mensagemHolder.setText( metaMensagem.dados.getString("mensagem"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dadosSolicitante(MetaMensagem metaMensagem){
        try{
            metaMensagem.dados = Conexao.jConectaServidor("404",metaMensagem.getMensagemId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}