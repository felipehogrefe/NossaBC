package com.ufal.ic.fh.nossabc.Comentario;

import com.ufal.ic.fh.nossabc.Conexao.Conexao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe Comentario:
 * Classe dedicada ao objeto individual comentário, o qual compõe o recyclerView encontrado no layout
 * activity_livro. Temos as propriedades do objeto a serem fixadas em objetos TextView, e outras como
 * comentario_ID, autor_ID que são usadas para identificar o objeto comentario e o autor, alem de
 * serialVersionUID, que possibilita o empacotamento do objeto comentario
 */
public class Comentario{
    private int comentario_ID;
    private String comentario;
    private String jaLeu;
    private String autorComent;
    private String gosteiTotal;
    private String notaUsuario;
    private String autor_ID;

    public Comentario(int comentario_ID, String autorComent, String jaLeu, String notaUsuario,
                      String gosteiTotal, String comentario, String autor_ID){
        this.comentario_ID=comentario_ID;
        this.comentario=comentario;
        this.jaLeu=jaLeu;
        this.autorComent=autorComent;
        this.gosteiTotal=gosteiTotal;
        this.notaUsuario=notaUsuario;
        this.autor_ID=autor_ID;
    }

    /**
     * O metodo constroiListaComentario adiciona objetos comentario a uma lista. Para construir tal
     * lista é enviado ao servidor o ID do livro, o qual entao responde com uma linha "110" caso a
     * requisição tenha gerado resultados, entao temos uma linha que consiste na quantidade de
     * comentarios feitos naqueles livros, e entao uma linha com o ID de cada comentario. Dados tais
     * IDs o metodo se conecta ao servidor e um buffer com os dados de tal comentario é retornado
     * é, entao, construido o comentario usando o metodo retornaComentario.
     */
    public static void constroiListaComentario(String livroID, ArrayList<Comentario> comentarios){
        try {
            int qtdComentarios, i;
            BufferedReader respostaServidor = Conexao.conectaServidor(livroID, "110");

            switch(respostaServidor.readLine()){
                case "110":
                    //leitura da quantidade de comentarios
                    qtdComentarios=Integer.parseInt(respostaServidor.readLine());
                    int listaIDs[] = new int[qtdComentarios];

                    for(i=0;i<qtdComentarios;i++){
                        //leitura do ID do comentario
                        listaIDs[i]=Integer.parseInt(respostaServidor.readLine());
                        //dado o ID temos a conexao com o servidor, que responderá com a funcao "111" caso positivo
                        JSONObject respostaComentarioServidor = Conexao.jConectaServidor("111",Integer.toString(listaIDs[i]));
                        //se o servidor responder ao valor de enviado, adiciona o comentario recebido
                        Comentario comentarioAux = retornaComentario(respostaComentarioServidor,listaIDs[i]);
                        comentarios.add(comentarioAux);
                    }
                    break;
                default:
                    //aqui falhou, não há comentarios
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *Dado um buffer o metodo o analiza a fim de construir o objeto comentario lendo as linhas do
     * buffer, cada linha corresponde a uma propriedade
     */
    private static Comentario retornaComentario(JSONObject comentario, int id) throws IOException, JSONException {
        String jali;
        if(comentario.getString("jali").equals("1")){
            jali = "Já Li";
        }else{
            jali = "Já Li";
        }
        Comentario metaComentario = new Comentario(id,comentario.getString("nome"),
                jali,comentario.getString("nota"),comentario.getString("totalGostei"),
                comentario.getString("comentario"),comentario.getString("usuario_id"));

        return metaComentario;
    }

    public String getNotaUsuario() { return notaUsuario; }
    public String getGosteiTotal() { return gosteiTotal; }
    public String getAutorComent() { return autorComent; }
    public String getComentario() { return comentario; }
    public String getJaLeu() { return jaLeu; }
    public int getComentario_ID() { return comentario_ID; }
    public String getAutorId() { return autor_ID; }
}