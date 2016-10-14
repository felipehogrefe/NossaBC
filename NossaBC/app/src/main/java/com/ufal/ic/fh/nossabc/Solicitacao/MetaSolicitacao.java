package com.ufal.ic.fh.nossabc.Solicitacao;

import android.os.HandlerThread;

import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Felipe on 13/04/2016.
 */
public class MetaSolicitacao {
    private String solicitante_ID;

    public MetaSolicitacao(String solicitante_ID){
        this.solicitante_ID=solicitante_ID;
    }
    public String getSolicitanteId() { return solicitante_ID; }

    public static ArrayList<MetaSolicitacao> constroiListaSolicitacoes(final String usuario_ID) throws IOException {

        ArrayList<MetaSolicitacao> solicitacoes = new ArrayList<>();

        int qtdSolicitacoes, i;
        BufferedReader respostaServidor = Conexao.conectaServidor(usuario_ID, "304");

        switch(respostaServidor.readLine()){
            case "304":
                //leitura da quantidade de comentarios
                qtdSolicitacoes=Integer.parseInt(respostaServidor.readLine());
                String listaIDs[] = new String[qtdSolicitacoes];

                for(i=0;i<qtdSolicitacoes;i++){
                    //leitura do ID do solicitante
                    listaIDs[i]=respostaServidor.readLine();
                    //dado o ID, adicionamos aquele elemento na lista de solicitacoes
                    MetaSolicitacao essaMetaSolicitacao = new MetaSolicitacao(listaIDs[i]);
                    solicitacoes.add(essaMetaSolicitacao);
                }
                break;
            default:
                //nao há solicitacoes
                break;
        }

        return solicitacoes;
    }
}
