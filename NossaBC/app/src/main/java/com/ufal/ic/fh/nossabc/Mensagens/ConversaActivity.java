package com.ufal.ic.fh.nossabc.Mensagens;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.HandlerThread;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.R;
import com.ufal.ic.fh.nossabc.Painel.PerfilActivity;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Classe referente a tela de conversa entre usuário, aqui uma serie de funcionalidades são defini-
 * das, como, por exemplo, uma conexao persistente, onde o servidor pode enviar novas mensagens que
 * foram adicionadas aquela conversa.
 * Created by Felipe on 14/04/2016.
 */
public class ConversaActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<MetaMensagem> mensagens;
    UsuarioArmazLocal usuarioArmazLocal;
    static CountDownLatch latch;
    EditText etEntrada;
    TextView tvNomeUsuarioConv;
    ImageButton btnEnviar, btnConvMain, btnConvMsg, btnConvSol, btnConvPain;
    static String respostaEnvio;
    static String nomeAlvo;
    static boolean ativo;
    static boolean novaMsg;
    ConversaAdapter adapter;
    RecyclerView recyclerConversa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * ao inicilicazar a activity devemos setar os booleanos referentes a conversa estar aberta
         * (ativo) e referente a novas mensagens (novaMsg) que setamos como false, uma vez que ao
         * abrir a activity cada nova mensagens já é carregada
         */

        ativo=true;
        novaMsg=false;
        setContentView(R.layout.activity_conversa);
        usuarioArmazLocal = new UsuarioArmazLocal(this);

        setCampos();

        preparaConversa();
        if (getIntent().getExtras().containsKey("LOGADO_ID")) {
            controiRecycler();
        }
        tvNomeUsuarioConv.setText(nomeAlvo);
        tvNomeUsuarioConv.setOnClickListener(this);

        new NotConversa().execute("");
    }

    private void setCampos() {
        etEntrada = (EditText) findViewById(R.id.etEntrada);
        tvNomeUsuarioConv = (TextView) findViewById(R.id.tvNomeUsuarioConv);
        btnEnviar = (ImageButton) findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(this);
        btnConvMain=(ImageButton)findViewById(R.id.btnConvMain);
        btnConvMain.setOnClickListener(this);
        btnConvMsg=(ImageButton)findViewById(R.id.btnConvMsg);
        btnConvMsg.setOnClickListener(this);
        btnConvSol=(ImageButton)findViewById(R.id.btnConvSol);
        btnConvSol.setOnClickListener(this);
        btnConvPain=(ImageButton)findViewById(R.id.btnConvPain);
        btnConvPain.setOnClickListener(this);
    }

    /**
     * como temos variaveis referentes a atividade demos seta-las novamente quando o usuario volta
     * para activity, desta forma sobrescrevemos o método onRestart
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        ativo=true;
        novaMsg=false;
        setContentView(R.layout.activity_conversa);
        usuarioArmazLocal = new UsuarioArmazLocal(this);

        setCampos();

        preparaConversa();
        if (getIntent().getExtras().containsKey("LOGADO_ID")) {
            controiRecycler();
        }
        tvNomeUsuarioConv.setText(nomeAlvo);
        tvNomeUsuarioConv.setOnClickListener(this);

        new NotConversa().execute("");
    }

    /**
     * Ao sair da activity podemos finalizar a conexao, setamos ativo como false
     */
    @Override
    public void onStop() {
        super.onStop();
        ativo=false;
    }

    private void controiRecycler() {
        recyclerConversa = (RecyclerView) findViewById(R.id.recyclerConversa);
        //com a lista de comentario preenchida criamos o adapter
        adapter = new ConversaAdapter(mensagens);
        recyclerConversa.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerConversa.setLayoutManager(layoutManager);

        //iniciamos o método que "escuta" por novas mensagens
        atualizadorConversa();
    }

    private void atualizadorConversa() {
        Thread jaLiThread = new HandlerThread("conexaoHandler") {
            @Override
            public void run() {
                try {
                    //pegamos uma instacia de socket:
                    Socket socketMsg = Conexao.conectaPersistenteServidor();
                    DataOutputStream outToServer = new DataOutputStream(socketMsg.getOutputStream());
                    BufferedReader respostaServidor = new BufferedReader(new
                            InputStreamReader(socketMsg.getInputStream()));
                    outToServer.writeBytes("406" + '\n' + getIntent().getStringExtra("LOGADO_ID")+
                            "\n"+getIntent().getStringExtra("ALVO_ID") + '\n');

                    //enquanto a activity estiver ativa mantemos o while, setamos um sleep de 150ms
                    //para nao sobrecarregar a cpu, alem disso tal valor é condizente com a latencia
                    while(ativo){
                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //se o servidor enviar alguma mensagem entramos no seletor:
                        if(respostaServidor.ready()){
                            if(respostaServidor.readLine().equals("406")){
                                //lemos a mensagem e adicionamos ao array de mensagens
                                MetaMensagem essaMetaMensagem = new MetaMensagem(respostaServidor.
                                        readLine());
                                mensagens.add(essaMetaMensagem);
                                //avisar que nova msg foi add
                                novaMsg=true;
                            }
                        }
                    }
                    outToServer.writeBytes("close\n");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        jaLiThread.start();
    }

    private void preparaConversa() {
        latch = new CountDownLatch(1);
        Thread mensagensThread = new HandlerThread("MensagensHandler"){
            @Override
            public void run() {
                try {
                    mensagens = MetaMensagem.constroiConversa(getIntent().
                            getStringExtra("LOGADO_ID"),getIntent().getStringExtra("ALVO_ID"));
                    BufferedReader nome = Conexao.conectaServidor(getIntent().
                            getStringExtra("ALVO_ID"),"104");
                    if(nome.readLine().equals("104")){
                        nomeAlvo = nome.readLine();
                    }else{
                        nomeAlvo = "???";
                    }
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
    }

    /**
     * Para manter o Recycler "escutando" fazemos um metodo rodar numa thread por tras da main, man-
     * temos um while e ao setar que temos nova mensagem saimos dele e chamamos o onPostExecute que
     * irá rodar na thread principal e avisar ao adapter quanto a adicição do novo elemento, e alem
     * disso erá chamar novamente o loop no background para escutar por novas mensagens
     */
    public class NotConversa extends AsyncTask <String,Void,MetaMensagem>{

        @Override
        protected MetaMensagem doInBackground(String... params) {
            while(!novaMsg){
            }
            return null;
        }

        protected void onPostExecute(MetaMensagem essaMetaMensagem) {
            if(ativo){
                adapter.notifyDataSetChanged();
                recyclerConversa.scrollToPosition(mensagens.size()-1);
            }
            novaMsg=false;
            new NotConversa().execute("");
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnEnviar:
                latch = new CountDownLatch(1);
                Thread mensagensThread = new HandlerThread("MensagensHandler"){
                    @Override
                    public void run() {
                        try {
                            JSONObject mensagem = new JSONObject();
                            mensagem.put("mensagem",etEntrada.getText().toString());
                            mensagem.put("sender",usuarioArmazLocal.getIdUsuario());
                            mensagem.put("alvo_id",getIntent().getStringExtra("ALVO_ID"));
                            BufferedReader resposta = Conexao.conectaServidor(mensagem.toString(),
                                    "405");
                            respostaEnvio=resposta.readLine();

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
                if(respostaEnvio.equals("-405")){
                    //faz toast de erro
                }
                etEntrada.setText("");
                break;
            case R.id.tvNomeUsuarioConv:
                Intent intentPerfil = new Intent(this, PerfilActivity.class);
                intentPerfil.putExtra("LOGADO_ID",getIntent().getStringExtra("ALVO_ID"));
                this.startActivity(intentPerfil);
                break;
            case R.id.btnConvMain:
                break;
            case R.id.btnConvMsg:
                break;
            case R.id.btnConvSol:
                break;
            case R.id.btnConvPain:
                break;
            default:
                break;
        }
    }
}
