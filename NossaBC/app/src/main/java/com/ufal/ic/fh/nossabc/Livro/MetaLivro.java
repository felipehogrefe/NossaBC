package com.ufal.ic.fh.nossabc.Livro;

import android.support.annotation.NonNull;

import com.ufal.ic.fh.nossabc.Conexao.Conexao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;

/**
 * Classe MetaLivro:
 * Classe correspondente ao objeto livro, como é um objeto temporário, só existe no cliente, no
 * momento da exibição não é propriamente um elemento livro, por isso meta. A classe é composta pelas
 * propriedades do livro, bem como de um valor long: serialVersionUID, o qual é utilizado para que o
 * objeto seja passado em um intent
 */
public class MetaLivro implements Serializable{
    private String titulo;
    private String chamada;
    private String livro_ID;
    private int status;
    private String edicao;
    private String autor;
    private String totalLeram;
    private String notaTotal;
    private String notaUsuario;
    public static final long serialVersionUID = 100L;

    public MetaLivro(String titulo, String livro_ID, int status, String edicao) {
        this.titulo = titulo;
        this.livro_ID = livro_ID;
        this.status = status;
        this.edicao=edicao;
    }

    /**
     * @return: retorna uma String a ser exibida correspondente a disponibilidade do livro
     */
    public String getStringStatus(){
        if(this.status==1){
            return "Disponível";
        }else{
            return "Indisponível";
        }
    }

    /**
     * @param livro_chamada : valor de chamada de um livro
     * @return: retorna um objeto MetaLivro com as propriedades setadas
     */
    @NonNull
    public static MetaLivro getMetaLivro(String livro_chamada) throws IOException, JSONException {

        JSONObject metaLivro = Conexao.jConectaServidor("103",livro_chamada);
        String livro_ID = metaLivro.getString("livro_id");
        int status = metaLivro.getInt("status");
        String titulo = metaLivro.getString("titulo");
        String edicao = metaLivro.getString("edicao");
        return new MetaLivro(titulo, livro_ID, status, edicao);
    }

    public String getTitulo() { return titulo; }
    public String getLivroID() { return livro_ID; }
    public String getEdicao() { return edicao; }
}
