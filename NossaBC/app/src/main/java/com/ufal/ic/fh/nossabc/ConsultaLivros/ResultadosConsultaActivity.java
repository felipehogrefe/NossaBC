package com.ufal.ic.fh.nossabc.ConsultaLivros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.ufal.ic.fh.nossabc.Livro.LivroActivity;
import com.ufal.ic.fh.nossabc.Livro.LivroAdapter;
import com.ufal.ic.fh.nossabc.Livro.MetaLivro;
import com.ufal.ic.fh.nossabc.R;

import java.util.ArrayList;

/**
 * Classe ResultadosConsultaActivity:
 * Responsável por apresentar os resultados de uma busca por livros, temos métodos para construir o
 * RecyclerView contendo os livros que constam na lista metaLivros que é contruida em
 * ConsultaActivity e passada via intent. Irá exibir um layout de erro caso não existam resultados.
 */
public class ResultadosConsultaActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnVoltar;
    private ArrayList<MetaLivro> metaLivros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras().containsKey("ERRO")){
            //exibe layout de erro
            setContentView(R.layout.activity_consulta_sem_resultados);
        }else if(getIntent().getExtras().containsKey("LIVROS")){
            //exibe layout com recyclerview contendo os resultados:
            //recebe o pacote com metaLivros
            metaLivros = (ArrayList<MetaLivro>) getIntent().getSerializableExtra("LIVROS");
            setContentView(R.layout.activity_resultados_livros);
            //cria o recyclerview a partir dos metaLivros recebidos, trata itemDecoration,
            // layoutManager etc:
            constroiRecycler();

        }
        btnVoltar = (Button)findViewById(R.id.btn_voltar_resultados);
        btnVoltar.setOnClickListener(this);
    }

    /**
     * Dada a lista metaLivros recuperada em onCreate construimos o RecyclerView contendo os
     * resultados
     */
    private void constroiRecycler() {
        final Intent intentResultados = new Intent(this, LivroActivity.class);
        RecyclerView recyclerResultados = (RecyclerView) findViewById(R.id.recycler_resultados);
        LivroAdapter adapter = new LivroAdapter(metaLivros);
        preencheRecycler(recyclerResultados, adapter, intentResultados);

    }

    /**
     * Prenchemos o RecyclerView com os resultados, ainda é posibilitada a ação de clique, com a
     * finalidade de redirecionar para o livro clicado
     * @param recycler_resultados: RecyclerView referente ao layout recycler_resultados
     * @param adapter: adapter da lista de metaLivros
     * @param intentResultados: intent para LivroActivity, classe alvo caso haja clique em um
     *                        elemento
     */
    private void preencheRecycler(RecyclerView recycler_resultados, LivroAdapter adapter, final
    Intent intentResultados) {
        recycler_resultados.setAdapter(adapter);
        recycler_resultados.setLayoutManager(new LinearLayoutManager(this));
        //trata o clique do usuário aqui:
        adapter.setOnItemClickListener(new LivroAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String livroID = metaLivros.get(position).getLivroID();
                intentResultados.putExtra("LIVRO", livroID);
                startActivity(intentResultados);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_voltar_resultados:
                finish();
                break;
        }

    }
}
