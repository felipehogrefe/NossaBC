package com.ufal.ic.fh.nossabc.Livro;

import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import com.ufal.ic.fh.nossabc.Comentario.*;
import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.Mapa.MapaActivity;
import com.ufal.ic.fh.nossabc.R;
import com.ufal.ic.fh.nossabc.Painel.LoginActivity;
import com.ufal.ic.fh.nossabc.Usuario.MetaUsuario;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Classe LivroActivity:
 * Classe criada com intuito de construir o layout activity_livro, o qual apresenta as informações
 * sobre um livro. Consiste basicamente em um cabeçalho com título, edição e autor, img Buttons para
 * funções de usuário, além de duas tabs, uma que comporta o conteúdo acadêmico acerca do livro
 * (dados técnicos), e outra que comporta o conteúdo social, notas, total de pessoas que já leram,
 * comentários e outros.
 * Para tal fazemos uso de uma lista de comentários, de um UsuarioArmazLocal, para comportar o usuá-
 * rio logado, elementos do layout como o seekBar barraNota, elementos auxiliáres como
 * CountDownLatch latch, usuado para fins de trabalho com threads paralelas, no caso da tab social
 * ainda construimos
 * o RecyclerView com o metodo constroiRecyclerComentarios.
 */
public class LivroActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Comentario> comentarios;
    private UsuarioArmazLocal usuarioArmazLocal;
    private TextView txtComentar, tituloLivro, livroEdicao,livroAutor,totalLeram,jaLi,notaUsuario,
            notaMedia, tvNotas, tvAssuntos,tvNumNorm,tvDescFis, tvPublicacao, tvTitUniforme,
            tvAutorSec, tvLocalizacao, tvTituloPrincipal, txtVerMapa;
    private String usuario_ID, respJaLi = "Já Leu?", StringTotalLeram,stringNotaUsuario = "",
            auxMediaNota = "";
    private SeekBar barraNota;
    private CountDownLatch latch;
    private int auxTotalLeram;
    private JSONObject respostaLivro;
    private ImageButton btnLivroMsg,btnLivroMain,btnLivroSol,btnLivroPain;

    /**
     * No onCreate devemos inicializar os campos do layout, além disso também nos informamos sobre
     * o usuário logado. Para setar os detalhes do livro usamos o metodo pegaDetalhesLivro, para
     * isso fazemos o uso de threads, uma vez que a conexão com a internet não é permitida na thread
     * principal. Os detalhes do livro são então armazenados na variável esteLivro, e para que a
     * paralelização de atividades não venha a causar danos utilizamos um CountDownLatch.
     * Além disso as tabs são inicializadas e setadas.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livro);

        usuarioArmazLocal = new UsuarioArmazLocal(this);
        MetaUsuario esseMetaUsuario = usuarioArmazLocal.getUsuarioLogado();
        usuario_ID = esseMetaUsuario.getUsuarioId();
        latch = new CountDownLatch(1);

        //checa se o intent foi setado direito
        if (getIntent().getExtras().containsKey("LIVRO")) {
            Thread livroThread = new HandlerThread("LivroHandler") {
                //pega o valor alterado dentro da thread
                @Override
                public void run() {
                    try {
                        pegaDetalhesLivro();
                        latch.countDown();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            livroThread.start();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                fixaCamposLivro();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setaBarraNota();



            TabHost host = (TabHost) findViewById(R.id.tabhost);
            host.setup();
            //Tab academica
            TabHost.TabSpec spec = host.newTabSpec("Acadêmico");
            spec.setContent(R.id.layout_academico);
            spec.setIndicator("Acadêmico");
            host.addTab(spec);
            //constroi o recycler dos comentarios:
            constroiRecyclerComentarios();
            //Tab social
            spec = host.newTabSpec("Social");
            spec.setContent(R.id.layout_social);
            spec.setIndicator("Social");
            host.addTab(spec);
        }
    }

    /**
     * Definido pois durante a atividade de fazer login, por exemplo, o conteúdo do livro
     * pode ser redefinido, outro usuário pode dar uma nota, por exemplo, e ainda ao submeter um
     * comentário ele deverá estar presente quando usuário voltar ao livro.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_livro);

        usuarioArmazLocal = new UsuarioArmazLocal(this);
        MetaUsuario esseMetaUsuario = usuarioArmazLocal.getUsuarioLogado();
        usuario_ID = esseMetaUsuario.getUsuarioId();
        latch = new CountDownLatch(1);

        if (getIntent().getExtras().containsKey("LIVRO")) {
            Thread livroThread = new HandlerThread("LivroHandler") {
                //pega o valor alterado dentro da thread
                @Override
                public void run() {
                    try {
                        pegaDetalhesLivro();
                        latch.countDown();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            livroThread.start();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                fixaCamposLivro();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setaBarraNota();

            TabHost host = (TabHost) findViewById(R.id.tabhost);
            host.setup();
            //Tab academica
            TabHost.TabSpec spec = host.newTabSpec("Academico");
            spec.setContent(R.id.layout_academico);
            spec.setIndicator("Academico");
            host.addTab(spec);
            //Tab social
            //constroi o recycler dos comentarios:
            constroiRecyclerComentarios();
            spec = host.newTabSpec("Social");
            spec.setContent(R.id.layout_social);
            spec.setIndicator("Social");
            host.addTab(spec);
        }
    }


    /**
     * Definição das funcionalidades da seekBar barraNota. Ao mover a barra fazemos um check se o
     * usuário está logado, caso negativo ele irá para seção de login, caso positivo a nova nota de-
     * ve ser informada ao servidor, e os campos referentes a nota devem ser alterados, tanto a nota
     * dada pelo usuário, quanto a média total, retornada pelo servidor
     */
    private void setaBarraNota() {
        final Intent intentBorraNota = new Intent(this, LoginActivity.class);
        barraNota.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progresso = 0;
            //pegar nota do livro!

            @Override
            public void onProgressChanged(SeekBar barraNota, int progresValue, boolean fromUser) {
                progresso = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar barraNota) {
            }

            //captar nova nota
            @Override
            public void onStopTrackingTouch(SeekBar barraNota) {
                final String nota = Integer.toString(progresso + 1) + ",0";
                Boolean logado = UsuarioArmazLocal.autenticar(usuarioArmazLocal);
                if(logado) {
                    notaUsuario.setText(nota);
                    latch = new CountDownLatch(1);
                    Thread jaLiThread = new HandlerThread("darNotaHandler") {
                        @Override
                        public void run() {
                            try {
                                //comunicação para alterar nova média total
                                auxMediaNota = alteraNota(nota);
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
                    notaMedia.setText(auxMediaNota);
                } else {
                    startActivity(intentBorraNota);
                }
            }
        });
    }

    /**
     * Metodo em que dada uma nova nota definida pelo usuário retorna a nova média calculada pelo
     * servidor
     * @param nota: recebe a nova nota setada pelo usuário
     * @return: retorna a nova média total calculada pelo servidor
     * @throws IOException: tratamento de problemas de conexão
     */
    private String alteraNota(String nota) throws IOException, JSONException {
        BufferedReader novaNotaTotal = Conexao.conectaServidor(usuarioArmazLocal.getIdUsuario()
                + "\n" + getIntent().getStringExtra("LIVRO") + "\n" + nota, "114");
        if (novaNotaTotal.readLine().equals("114")) {
            return novaNotaTotal.readLine();
        }
        return respostaLivro.getString("notaMedia");
    }

    /**
     * Fixação dos campos do layout
     */
    private void fixaCamposLivro() throws JSONException {
        tituloLivro = (TextView) findViewById(R.id.txt_livro_titulo);
        livroEdicao = (TextView) findViewById(R.id.txt_livro_edicao);
        livroAutor = (TextView) findViewById(R.id.txt_livro_autor);
        totalLeram = (TextView) findViewById(R.id.txt_total_leram);
        barraNota = (SeekBar) findViewById(R.id.barraNota);
        jaLi = (TextView) findViewById(R.id.txt_ja_li);
        notaUsuario = (TextView) findViewById(R.id.txt_nota_usuario);
        tvNotas = (TextView) findViewById(R.id.tvNotas);
        tvAssuntos = (TextView) findViewById(R.id.tvAssuntos);
        tvNumNorm = (TextView) findViewById(R.id.tvNumNorm);
        tvDescFis = (TextView) findViewById(R.id.tvDescFis);
        tvPublicacao = (TextView) findViewById(R.id.tvPublicacao);
        tvTitUniforme = (TextView) findViewById(R.id.tvTitUniforme);
        tvAutorSec = (TextView) findViewById(R.id.tvAutorSec);
        tvLocalizacao = (TextView) findViewById(R.id.tvLocalizacao);
        tvTituloPrincipal = (TextView) findViewById(R.id.tvTituloPrincipal);
        txtVerMapa = (TextView) findViewById(R.id.txtVerMapa);
        txtVerMapa.setOnClickListener(this);
        setaNotaUsuario();
        notaUsuario.setText(stringNotaUsuario);
        txtComentar=(TextView)findViewById(R.id.txtComentar);
        txtComentar.setOnClickListener(this);
        notaMedia = (TextView) findViewById(R.id.txt_nota_total);

        btnLivroMsg=(ImageButton)findViewById(R.id.btnLivroMsg);
        btnLivroMsg.setOnClickListener(this);
        btnLivroMain=(ImageButton)findViewById(R.id.btnLivroMain);
        btnLivroMain.setOnClickListener(this);
        btnLivroSol=(ImageButton)findViewById(R.id.btnLivroSol);
        btnLivroSol.setOnClickListener(this);
        btnLivroPain=(ImageButton)findViewById(R.id.btnLivroPain);
        btnLivroPain.setOnClickListener(this);

        tituloLivro.setText(respostaLivro.getString("titulo"));
        livroEdicao.setText(respostaLivro.getString("edicao"));
        livroAutor.setText(respostaLivro.getString("autor"));
        totalLeram.setText(respostaLivro.getString("totalJaLi"));
        notaMedia.setText(respostaLivro.getString("notaMedia"));
        usuarioLogadoJaLeu();
        jaLi.setOnClickListener(this);
        tvNotas.setText(respostaLivro.getString("notas"));
        tvAssuntos.setText(respostaLivro.getString("assuntos"));
        tvNumNorm.setText(respostaLivro.getString("numero_normalizado"));
        tvDescFis.setText(respostaLivro.getString("descricao_fisica"));
        tvPublicacao.setText(respostaLivro.getString("publicacao"));
        tvTitUniforme.setText(respostaLivro.getString("titulo_uniforme"));
        tvAutorSec.setText(respostaLivro.getString("autor_secundario"));
        tvLocalizacao.setText(respostaLivro.getString("chamada"));
        tvTituloPrincipal.setText(respostaLivro.getString("titulo_principal"));
    }

    /**
     * Conexão com o servidor, dado o id do livro contido na EXTRA "LIVRO" será retornada os detalhes
     * do livro em questao
     * @throws IOException: tratamento de problemas de conexao
     */
    private void pegaDetalhesLivro() throws IOException, JSONException {
        respostaLivro = Conexao.jConectaServidor("101",getIntent().getStringExtra("LIVRO"));
        auxTotalLeram = Integer.valueOf(respostaLivro.getString("totalJaLi"));



    }

    /**
     * Para recuperar a nota dada ao livro em questão pelo usuário logado devemos pedir a informação
     * ao servidor, haja visto que existe variação do usuário logado isso não é feito em conjunto a
     * obtenção dos detalhes do livro. Há ainda variação da nota, pois o usuário pode redefini-la ao
     * alterar a barraNota
     */
    private void setaNotaUsuario() {
        Boolean logado = UsuarioArmazLocal.autenticar(usuarioArmazLocal);
        //check de logado
        if (logado) {
            latch = new CountDownLatch(1);
            Thread jaLiThread = new HandlerThread("notaHandler") {
                @Override
                public void run() {
                    try {
                        BufferedReader notaUsuario = Conexao.conectaServidor(getIntent().
                                getStringExtra("LIVRO") + "\n" + usuario_ID + "\n", "113");
                        if (notaUsuario.readLine().equals("113")) {
                            String nota = notaUsuario.readLine();
                            //se o usuário não deu nota, setamos vazio
                            if (nota.equals("-1")) {
                                stringNotaUsuario = "";
                            } else {
                            //se deu notas, setamos com o valor correto
                                stringNotaUsuario = nota;
                                barraNota.setProgress(Integer.valueOf(stringNotaUsuario.substring(0,
                                        1)) - 1);
                            }
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
        } else {
            //se o usuário não está logado
            stringNotaUsuario="";
        }
    }

    /**
     * Metodo criado para recuperação do status Já Li, semalhante ao setaNotaUsuario
     */
    private void usuarioLogadoJaLeu() {
        latch = new CountDownLatch(1);
        Thread jaLiThread = new HandlerThread("jaLiHandler") {
            @Override
            public void run() {
                try {
                    BufferedReader jaLeu = Conexao.conectaServidor(getIntent().
                            getStringExtra("LIVRO") + "\n" + usuario_ID + "\n", "102");
                    if (jaLeu.readLine().equals("102")) {
                        respJaLi = "Já Li!";
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
        jaLi.setText(respJaLi);
    }

    /**
     * Metodo que recupera uma lista de comentario, preenchida em constroiListaComentario, thread é
     * usado pois em constroiListaComentario há comunicação com o servidor.
     */
    private void constroiRecyclerComentarios() {
        comentarios = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Comentario.constroiListaComentario(getIntent().getStringExtra("LIVRO"),
                            comentarios);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        RecyclerView recyclerResultados = (RecyclerView) findViewById(R.id.recycler_comentarios);
        //com a lista de comentario preenchida criamos o adapter
        ComentarioAdapter adapter = new ComentarioAdapter(comentarios);
        preencheRecycler(recyclerResultados, adapter);
    }

    /**
     * Por fim para preencher o RecyclerView dos comentarios temos o metodo preencheRecycler
     * @param recycler_resultados: RecyclerView a ser exibido no layout do livro
     * @param adapter: adapter referente aos comentarios
     */
    private void preencheRecycler(RecyclerView recycler_resultados, ComentarioAdapter adapter) {
        recycler_resultados.setAdapter(adapter);
        recycler_resultados.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Metodo para recuperar o item clicado
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Tratamento de cliques no View v:
     * É necessário primeiramente definir o usuário logado, para fins de transfir para login, ou
     * comunicação com o servidor. Note que cliques em elementos dentro do RecyclerView são tratados
     * no recyclerView, não aqui.
     * @param v: view com os elementos que podem ser clicados
     */
    @Override
    public void onClick(View v) {
        Boolean logado = UsuarioArmazLocal.autenticar(usuarioArmazLocal);
        latch = new CountDownLatch(1);
        switch (v.getId()) {
            case R.id.txt_ja_li:
                if (logado) {
                    Thread jaLiThread = new HandlerThread("jaLeuHandler") {
                        @Override
                        public void run() {
                            try {
                                BufferedReader respJaliServidor = Conexao.conectaServidor
                                        (usuarioArmazLocal.getIdUsuario()
                                        + "\n" + getIntent().getStringExtra("LIVRO"), "112");
                                if (respJaliServidor.readLine().equals("112")) {
                                    String jaliOK = respJaliServidor.readLine();
                                    if (jaliOK.equals("112")) {
                                        respJaLi = "Já Li!";
                                        auxTotalLeram++;
                                    } else if (jaliOK.equals("-112")) {
                                        respJaLi = "Já Leu?";
                                        auxTotalLeram--;
                                    }
                                } else {
                                    //tratar erro
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
                    totalLeram.setText(Integer.toString(auxTotalLeram));
                    jaLi.setText(respJaLi);

                } else {
                    Intent intentLogin = new Intent(this, LoginActivity.class);
                    startActivity(intentLogin);
                }
                break;
            case R.id.txtComentar:
                if(logado){
                    Intent intentComentar = new Intent(this, ComentarioActivity.class);
                    intentComentar.putExtra("LIVRO_ID",getIntent().getStringExtra("LIVRO"));
                    startActivity(intentComentar);
                }else{
                    Intent intentLogin = new Intent(this, LoginActivity.class);
                    startActivity(intentLogin);
                }
                break;
            case R.id.txtVerMapa:
                Intent intentVerMapa = new Intent(this, MapaActivity.class);
                intentVerMapa.putExtra("LIVRO_ID",getIntent().getStringExtra("LIVRO"));
                startActivity(intentVerMapa);
                break;
            case R.id.btnLivroMain:
                break;
            case R.id.btnLivroMsg:
                break;
            case R.id.btnLivroSol:
                break;
            case R.id.btnLivroPain:
                break;
            default:
                break;
        }
    }
}