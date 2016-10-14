package com.ufal.ic.fh.nossabc.ConsultaLivros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.Livro.MetaLivro;
import com.ufal.ic.fh.nossabc.R;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe ConsultaActivity:
 * Objetivo da classe popular os campos do activity_consulta, temos a funcionalidade de pesquisa.
 * O usuário entra com uma String, e então tal valor é passado ao servidor, há ainda um tratamento
 * para o caso de não haver resultados. Caso o resultado não seja nulo é criada uma lista com meta
 * livros que é passada a activity seguinte através do Serializable
 */
public class ConsultaActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnBuscar, btnVoltarConsulta;
    private EditText tituloIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        tituloIn = (EditText)findViewById(R.id.tituloIn);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);
        btnVoltarConsulta = (Button)findViewById(R.id.btnVoltarConsulta);

        btnBuscar.setOnClickListener(this);
        btnVoltarConsulta.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnBuscar:
                final Intent intentResultados = new Intent(this, ResultadosConsultaActivity.class);
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            String[] valor = {tituloIn.getText().toString()};
                            String funcaoDoServidor;
                            //comunicacao com o servidor:
                            BufferedReader respostaServidor = Conexao.conectaServidor(valor[0],
                                    "100");
                            funcaoDoServidor = respostaServidor.readLine();
                            if(funcaoDoServidor.equals("-100")){
                                //nao obteve resultados, mostrar erro
                                intentResultados.putExtra("ERRO", "Não Encontrado.");
                                startActivity(intentResultados);
                            }else if(getFuncaoServidor(funcaoDoServidor)){
                                //aqui populo o array de metaLivros:
                                ArrayList<MetaLivro> metaLivros = new ArrayList<>();
                                ConsultaActivity.constroiListaMetaLivro(respostaServidor,
                                        metaLivros);
                                intentResultados.putExtra("LIVROS", metaLivros);
                                startActivity(intentResultados);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                }).start();
                break;
            case R.id.btnVoltarConsulta:
                finish();
                break;
        }
    }

    private boolean getFuncaoServidor (String entrada){
        //checa se a entrada do servidor é um conjunto de livros (100)
        if (entrada.equals("100")){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Utilizando o metodo getMetaLivro da classe MetaLivro aqui a entrada do servidor é analizada
     * e cada linha é passada  ao metodo a fim de criar um meta livro correspondente
     * Note que usamos o valor de CHAMADA, uma vez que cada livro tem seu ID único, livros com mesmo
     * título e edição diferente são diferenciados pelo valor da CHAMADA, desta forma iremos
     * apresentar um item para cada edição daquele título.
     * @param entradaServidor: buffer contendo dados correspondentes a um livro
     * @param metaLivros: lista de meta livros, será usada para criar o RecyclerView
     */
    private static void constroiListaMetaLivro(BufferedReader entradaServidor, ArrayList<MetaLivro>
            metaLivros) throws JSONException {
        int i;
        try {
            int qtdLivros = Integer.parseInt(entradaServidor.readLine());
            for(i=0;i<qtdLivros;i++){
                //le um valor de CHAMADA para criar o novo Metalivro:
                MetaLivro novoMetaLivro = MetaLivro.getMetaLivro(entradaServidor.readLine());
                metaLivros.add(novoMetaLivro);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
