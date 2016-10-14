package com.ufal.ic.fh.nossabc.Solicitacao;

import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.ufal.ic.fh.nossabc.Comentario.Comentario;
import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.Decoration.DividerItemDecoration;
import com.ufal.ic.fh.nossabc.R;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Para finalizar o processor de adição de novos amigos o usuário deve ser capaz de
 * responder a uma solicitação de pedido de amizade, para isso temos um layout no qual
 * iremos exibir as solicitações pendentes. Trata-se de um RecyclerView que construimos
 * a partir de ID do usuário logado e dos IDs dos usuários solicitantes.
 */
public class SolicitacaoActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<MetaSolicitacao> solicitacoes;
    static CountDownLatch latch;
    UsuarioArmazLocal usuarioArmazLocal;
    private ImageButton btnMain,btnMsg,btnSol,btnPain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuarioArmazLocal = new UsuarioArmazLocal(this);
        setContentView(R.layout.activity_solicitacao);
        setaCampos();
        constroiRecycler();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        usuarioArmazLocal = new UsuarioArmazLocal(this);
        setContentView(R.layout.activity_solicitacao);
        setaCampos();
        constroiRecycler();
    }

    private void setaCampos(){
        btnMain=(ImageButton)findViewById(R.id.btnSolMain);
        btnMain.setOnClickListener(this);
        btnMsg=(ImageButton)findViewById(R.id.btnSolMsg);
        btnMsg.setOnClickListener(this);
        btnSol=(ImageButton)findViewById(R.id.btnSolSol);
        btnSol.setOnClickListener(this);
        btnPain=(ImageButton)findViewById(R.id.btnSolPain);
        btnPain.setOnClickListener(this);
    }

    private void constroiRecycler() {
        //criamos um array com os IDs dos solicitantes
        latch = new CountDownLatch(1);
        Thread solicitacoesThread = new HandlerThread("SolicitacoesHandler"){
            @Override
            public void run() {
                try {
                    solicitacoes = MetaSolicitacao.constroiListaSolicitacoes(usuarioArmazLocal.getIdUsuario());
                    latch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        solicitacoesThread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerSolicitacoes = (RecyclerView) findViewById(R.id.recyclerSolicitacoes);
        //com a lista de comentario preenchida criamos o adapter
        SolicitacaoAdapter adapter = new SolicitacaoAdapter(solicitacoes);
        recyclerSolicitacoes.setAdapter(adapter);
        recyclerSolicitacoes.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSolMain:
                break;
            case R.id.btnSolMsg:
                break;
            case R.id.btnSolSol:
                break;
            case R.id.btnSolPain:
                break;
            default:
                break;
        }
    }
}
