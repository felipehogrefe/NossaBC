package com.ufal.ic.fh.nossabc.Painel;

import android.content.Intent;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.Mensagens.ConversaActivity;
import com.ufal.ic.fh.nossabc.R;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView ivPerfilUsuario;
    private UsuarioArmazLocal usuarioArmazLocal;
    private TextView txtNomeUsuarioPerfil, txtBioUsuario, txtIdadeUsuario, txtCursoUsuario,
            txtQtdJaLiUsuario, txtUltimoLidoUsuario, txtUltimoComentario, txtUltimoLivroComent,
            lblLivroComentado;
    private Button btnEnviarMsg, btnAddAmigo;
    private ImageButton btnMain,btnMsg,btnSol,btnPain;
    private String usuario_ID;
    static private String resposta="";

    static CountDownLatch latch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        usuarioArmazLocal = new UsuarioArmazLocal(this);


        if(getIntent().getExtras().containsKey("USUARIO_ID")){
            usuario_ID=getIntent().getStringExtra("USUARIO_ID");

            fixaCamposPerfil();
            try{
                JSONObject estePerfil = Conexao.jConectaServidor("301",usuario_ID);
                txtNomeUsuarioPerfil.setText(estePerfil.getString("nome"));
                txtBioUsuario.setText(estePerfil.getString("bio"));
                txtIdadeUsuario.setText(estePerfil.getString("idade"));
                txtCursoUsuario.setText(estePerfil.getString("curso"));
                txtQtdJaLiUsuario.setText(estePerfil.getString("jali"));
                //Pegar o nome do livro aqui:
                txtUltimoLidoUsuario.setText(estePerfil.getString("ultimoLido"));
                if(estePerfil.getString("ultimoComentario").equals("-1")){

                }else{
                    txtUltimoComentario.setText(estePerfil.getString("ultimoComentario"));
                    lblLivroComentado.setText("Em:");
                    //Pegar o nome do livro aqui:
                    txtUltimoLivroComent.setText(estePerfil.getString("ultimoLivroComent"));
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void fixaCamposPerfil(){
        txtNomeUsuarioPerfil = (TextView) findViewById(R.id.txtNomeUsuarioPerfil);
        txtBioUsuario = (TextView) findViewById(R.id.txtBioUsuario);
        txtIdadeUsuario = (TextView) findViewById(R.id.txtIdadeUsuario);
        txtCursoUsuario = (TextView) findViewById(R.id.txtCursoUsuario);
        txtQtdJaLiUsuario = (TextView) findViewById(R.id.txtQtdJaLiUsuario);
        txtUltimoLidoUsuario = (TextView) findViewById(R.id.txtUltimoLidoUsuario);
        txtUltimoComentario = (TextView) findViewById(R.id.txtUltimoComentario);
        lblLivroComentado = (TextView) findViewById(R.id.lblLivroComentado);
        txtUltimoLivroComent = (TextView) findViewById(R.id.txtUltimoLivroComent);

        btnAddAmigo=(Button) findViewById(R.id.btnAddAmigo);
        btnAddAmigo.setOnClickListener(this);
        checaSolicitacao();
        btnEnviarMsg=(Button) findViewById(R.id.btnEnviarMsg);
        btnEnviarMsg.setOnClickListener(this);

        btnMain=(ImageButton)findViewById(R.id.btnPerMain);
        btnMain.setOnClickListener(this);
        btnMsg=(ImageButton)findViewById(R.id.btnPerMsg);
        btnMsg.setOnClickListener(this);
        btnSol=(ImageButton)findViewById(R.id.btnPerSol);
        btnSol.setOnClickListener(this);
        btnPain=(ImageButton)findViewById(R.id.btnPerPain);
        btnPain.setOnClickListener(this);
    }

    private void checaSolicitacao(){
        latch = new CountDownLatch(1);
        Thread solicitaAmizadeThread = new HandlerThread("ChecaSolicitacaoHandler"){
            @Override
            public void run() {
                try {
                    BufferedReader entrada = Conexao.conectaServidor(usuarioArmazLocal.getIdUsuario()+"\n"+usuario_ID+"\n","303");
                    resposta = entrada.readLine();
                    latch.countDown();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        solicitaAmizadeThread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(resposta.equals("303a")){
            btnAddAmigo.setText("AMIGOS");
        }else if(resposta.equals("303b")){
            btnAddAmigo.setText("SOLICITADO");
        }else if(resposta.equals("303c")){
            btnAddAmigo.setText("ACEITAR");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddAmigo:
                Boolean logado = UsuarioArmazLocal.autenticar(usuarioArmazLocal);
                if(logado){
                    try{
                        clicaAmizade();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Intent intentLogin = new Intent(this, LoginActivity.class);
                    startActivity(intentLogin);
                }
                break;
            case R.id.btnEnviarMsg:
                Intent intentMensagens = new Intent(this, ConversaActivity.class);
                intentMensagens.putExtra("LOGADO_ID",usuarioArmazLocal.getIdUsuario());
                intentMensagens.putExtra("ALVO_ID",usuario_ID);
                startActivity(intentMensagens);
                break;
            case R.id.btnPerMain:
                break;
            case R.id.btnPerMsg:
                break;
            case R.id.btnPerSol:
                break;
            case R.id.btnPerPain:
                break;
            default:
                break;
        }
    }

    private void clicaAmizade() throws IOException{
        latch = new CountDownLatch(1);
        Thread solicitaAmizadeThread = new HandlerThread("SolicitaAmizadeHandler"){
            @Override
            public void run() {
                try {
                    BufferedReader entrada = Conexao.conectaServidor(usuarioArmazLocal.getIdUsuario()+"\n"+usuario_ID,"302");
                    resposta = entrada.readLine();
                    latch.countDown();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        solicitaAmizadeThread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        switch(resposta){
            case "302":
                Toast.makeText(PerfilActivity.this, "Solicitado.", Toast.LENGTH_SHORT).show();
                clicaAmizade();
                //tratar a solicitacao
                btnAddAmigo.setText("SOLICITADO");
                break;
            case "302a":
                //já são amigos, btn está exibindo AMIGOS
                break;
            case "302b":
                //nada a fazer
                btnAddAmigo.setText("SOLICITADO");
                break;
            case "302c":
                btnAddAmigo.setText("AMIGOS");
                clicaAmizade();
                //tratar a aceitacao
                Toast.makeText(PerfilActivity.this, "Amigo aceito.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
