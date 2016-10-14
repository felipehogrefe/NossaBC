package com.ufal.ic.fh.nossabc.Usuario;

import android.content.Intent;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ufal.ic.fh.nossabc.Comentario.Comentario;
import com.ufal.ic.fh.nossabc.Comentario.ComentarioAdapter;
import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.Livro.LivroActivity;
import com.ufal.ic.fh.nossabc.Livro.LivroAdapter;
import com.ufal.ic.fh.nossabc.Painel.PerfilActivity;
import com.ufal.ic.fh.nossabc.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ResultadosUsuarios extends AppCompatActivity implements View.OnClickListener{
    private TextView txtResultadosConsultaUsuarios;
    private Button btnVoltarResultUsuarios;
    private ArrayList<MetaUsuario> metaUsuarios;
    private CountDownLatch latch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_usuarios);

        btnVoltarResultUsuarios = (Button) findViewById(R.id.btnVoltarResultUsuarios);
        btnVoltarResultUsuarios.setOnClickListener(this);

        metaUsuarios = criaItensResultado(getIntent().getStringExtra("NOME"));

        for(int i=0;i<metaUsuarios.size();i++){
            System.out.println("nome: "+metaUsuarios.get(i).getNome()+"\n");
        }

        RecyclerView recyclerUsuarios = (RecyclerView) findViewById(R.id.recyclerResultadosUsuarios);
        UsuarioAdapter adapter = new UsuarioAdapter(metaUsuarios);
        recyclerUsuarios.setAdapter(adapter);
        recyclerUsuarios.setLayoutManager(new LinearLayoutManager(this));
        final Intent intentResultados = new Intent(this, PerfilActivity.class);
        adapter.setOnItemClickListener(new UsuarioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String usuario_ID = metaUsuarios.get(position).getUsuarioId();
                intentResultados.putExtra("USUARIO_ID", usuario_ID);
                startActivity(intentResultados);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnVoltarResultUsuarios:
                finish();
                break;
            default:
                break;
        }

    }

    public ArrayList<MetaUsuario> criaItensResultado(final String entrada){
        final ArrayList<MetaUsuario> aux = new ArrayList<>();
        latch = new CountDownLatch(1);
        Thread jaLiThread = new HandlerThread("usuariosHandler") {
            @Override
            public void run() {
                try {
                    BufferedReader retornoUsuario = Conexao.conectaServidor(entrada + "\n", "500");
                    if (retornoUsuario.readLine().equals("500")) {
                        int qtdResultados = Integer.parseInt(retornoUsuario.readLine());
                        for(int i=0;i<qtdResultados;i++){
                            String usuario_ID = retornoUsuario.readLine();
                            MetaUsuario esseUsuario = new MetaUsuario(usuario_ID);
                            JSONObject usuario = Conexao.jConectaServidor("501", usuario_ID);
                            esseUsuario.setCurso(usuario.getString("curso"));
                            esseUsuario.setNome(usuario.getString("nome"));
                            aux.add(esseUsuario);
                        }
                        //
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

        return aux;
    }
}
