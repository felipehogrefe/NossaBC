package com.ufal.ic.fh.nossabc.Comentario;

import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.R;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Classe ComentarioActivity:
 * Classe utilizada para submissão de novos comentários.
 * Além das propriedades correspondentes aos objetos do layout temos UsuarioArmazLocal, o qual arma-
 * zena o usuário logado atualmente, objeto chave para recuperação de ID do usuário. Temos ainda
 * latch, utilizado para que os valores trabalhados na Thread paralela sejam corretamente computados
 * na Thread principal.
 * Como fazemos a submissão do comentário em uma Activity filha não precisamos tratar a adição do
 * novo comentário na lista de comentários desse livro, isso será feito no metodo onRestart da Activity
 * mãe, que irá requisitar novamente a lista de comentários, e caso a submissão enha sido efetuada
 * com sucesso irá exibir o novo comentário
 */
public class ComentarioActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnVoltar, btnSubmeter;
    private EditText edtComentario;
    private UsuarioArmazLocal usuarioArmazLocal;
    private static String livro_ID;
    final CountDownLatch latch = new CountDownLatch(1);

    /**
     * Aqui temos a recuperação do ID do livro ao estamos tentando submeter novo comentario, para isso
     * recuperamos a variável e armazenamos na variável estática livro_ID.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cometario);

        usuarioArmazLocal = new UsuarioArmazLocal(this);
        livro_ID=this.getIntent().getStringExtra("LIVRO_ID");

        btnVoltar = (Button)findViewById(R.id.btnVoltarComent);
        btnSubmeter=(Button)findViewById(R.id.btnSubmeter);
        edtComentario=(EditText)findViewById(R.id.edtComentario);

        btnSubmeter.setOnClickListener(this);
        btnVoltar.setOnClickListener(this);
    }

    /**
     * Tratamento do clique do usuário, temos dois casos, ao clicar em voltar a Activity é fechada,
     * e ao clicar em submeter chamamos o metodo enviaComentario, o qual deve responder com a função
     * "118" em caso positivo. Aqui fazemos a conexão dentro de uma thread, uma vez que atividades
     * de conexão não são permitidas na thread principal
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnVoltarComent:
                finish();
                break;
            case R.id.btnSubmeter:
                final String comentario = edtComentario.getText().toString();
                final String usuario_ID = usuarioArmazLocal.getIdUsuario();
                Thread submeteComentThread = new HandlerThread("SubmeteComentHandler"){
                    @Override
                    public void run() {
                        try {
                            //envia os dados para o servidor
                            if(enviaComentario(comentario, usuario_ID).equals("118")){
                                //tudo certo
                            }else{
                                //algo deu errado exibe um toast
                            }
                            latch.countDown();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                submeteComentThread.start();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
                break;
            default:
                break;
        }
    }

    /**
     *
     * @param comentario: comentário a ser submetido
     * @param usuario_ID: ID do usuário que pretende submenter o comentário
     * @return: retorno utilizado para confirmar a submissão
     * @throws IOException: podemos ter erros de conexão, servidor offline entre outros
     */
    private static String enviaComentario(String comentario, String usuario_ID) throws IOException{

        System.out.println(livro_ID);
        BufferedReader entrada = Conexao.conectaServidor(usuario_ID+"\n"+livro_ID+"\n"+comentario, "118");
        if(entrada.readLine().equals("118")){
            return "118";
        }else{
            return "-1";
        }
    }
}
