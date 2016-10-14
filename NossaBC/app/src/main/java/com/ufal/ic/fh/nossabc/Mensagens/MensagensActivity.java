package com.ufal.ic.fh.nossabc.Mensagens;

import android.content.Intent;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.ufal.ic.fh.nossabc.Decoration.DividerItemDecoration;
import com.ufal.ic.fh.nossabc.Livro.LivroActivity;
import com.ufal.ic.fh.nossabc.Livro.LivroAdapter;
import com.ufal.ic.fh.nossabc.R;
import com.ufal.ic.fh.nossabc.Solicitacao.MetaSolicitacao;
import com.ufal.ic.fh.nossabc.Solicitacao.SolicitacaoAdapter;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Activity para exibir as conversas abertas pelo usuario
 */
public class MensagensActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<MetaMensagem> mensagens;
    static CountDownLatch latch;
    UsuarioArmazLocal usuarioArmazLocal;
    private ImageButton btnMain,btnMsg,btnSol,btnPain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagens);
        usuarioArmazLocal = new UsuarioArmazLocal(this);

        btnMain=(ImageButton)findViewById(R.id.btnMsgMain);
        btnMain.setOnClickListener(this);
        btnMsg=(ImageButton)findViewById(R.id.btnMsgMsg);
        btnMsg.setOnClickListener(this);
        btnSol=(ImageButton)findViewById(R.id.btnMsgSol);
        btnSol.setOnClickListener(this);
        btnPain=(ImageButton)findViewById(R.id.btnMsgPain);
        btnPain.setOnClickListener(this);

        constroiRecycler();
        //
    }

    private void constroiRecycler() {
        //criamos um array com os IDs das Mensagens
        latch = new CountDownLatch(1);
        Thread mensagensThread = new HandlerThread("MensagensHandler"){
            @Override
            public void run() {
                try {
                    mensagens = MetaMensagem.constroiListaMensagens(usuarioArmazLocal.getIdUsuario());

                    latch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mensagensThread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RecyclerView recyclerMensagens = (RecyclerView) findViewById(R.id.recyclerMensagens);
        //com a lista de comentario preenchida criamos o adapter
        MensagensAdapter adapter = new MensagensAdapter(mensagens);
        final Intent intentMensagens = new Intent(this, ConversaActivity.class);
        preencheRecycler(recyclerMensagens, adapter, intentMensagens);
    }

    private void preencheRecycler(RecyclerView recyclerMensagens, MensagensAdapter adapter, final Intent intentMensagens) {
        recyclerMensagens.setAdapter(adapter);
        recyclerMensagens.setLayoutManager(new LinearLayoutManager(this));
        //trata o clique do usuário aqui:
        adapter.setOnItemClickListener(new MensagensAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intentMensagens.putExtra("LOGADO_ID", usuarioArmazLocal.getIdUsuario());
                intentMensagens.putExtra("ALVO_ID", mensagens.get(position).getAlvo());
                startActivity(intentMensagens);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnMsgMain:
                break;
            case R.id.btnMsgMsg:
                break;
            case R.id.btnMsgSol:
                break;
            case R.id.btnMsgPain:
                break;
            default:
                break;
        }

    }
}


