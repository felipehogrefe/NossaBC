package com.ufal.ic.fh.nossabc.Painel;

import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.R;
import com.ufal.ic.fh.nossabc.Usuario.MetaUsuario;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 *Classe LoginActivity:
 * Classe que implementa a funcionalidade de Login, o qual é feito juntamento com o servidor, o que
 * faz necessário o uso de threads, uma vez feito o login a variável usuarioArmazLocal é alterada
 * para armazenar os dados do usuário logado
 */
public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{
    private EditText edtSenha, edtUsuario;
    private Button btnLogin, btnVoltaLogin;
    UsuarioArmazLocal usuarioArmazLocal;
    final CountDownLatch latch = new CountDownLatch(1);

    /**
     * Metodo onde as variáveis são fixadas juntos aos elementos do layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioArmazLocal = new UsuarioArmazLocal(this);

        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnVoltaLogin = (Button) findViewById(R.id.btnVoltarLogin);
        btnLogin.setOnClickListener(this);
        btnVoltaLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case(R.id.btnLogin):
                Thread loginThread = new HandlerThread("Handler"){
                    //pega o valor alterado dentro da thread
                    @Override
                    public void run() {
                        try {
                            logaUsuario();
                            latch.countDown();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                loginThread.start();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //seta o usuario como logado
                usuarioArmazLocal.setUsuarioLogado(true);

                finish();
                break;
            case(R.id.btnVoltarLogin):
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * Metodo de contato com o servidor, retorna dados sobre o usuario caso senha e usuario estejam
     * corretos, os quais são armazenados em usuarioArmazLocal.
     * @throws IOException
     */
    private void logaUsuario() throws IOException {
        String usuario = edtUsuario.getText().toString();
        String senha = edtSenha.getText().toString();
        String saida = usuario+"\n"+senha;
        BufferedReader entrada = Conexao.conectaServidor(saida, "201");
        //criar conexao aqui que retorna o ID do usuario caso exista, ou retorna -1 caso login de errado
        if(entrada.readLine().equals("-1")){
            //mostra toast dizenho q senha ou usuario esta errado
            usuarioArmazLocal.setUsuarioLogado(false);
        }else{
            String usuario_ID = entrada.readLine();
            MetaUsuario esseMetaUsuario = new MetaUsuario(usuario, senha, usuario_ID);
            usuarioArmazLocal.armazenaDadosUsuario(esseMetaUsuario);
            usuarioArmazLocal.setUsuarioLogado(true);
        }
    }
}
