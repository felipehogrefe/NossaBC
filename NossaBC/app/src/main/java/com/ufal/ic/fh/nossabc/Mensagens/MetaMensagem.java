package com.ufal.ic.fh.nossabc.Mensagens;

import android.content.Context;
import android.os.HandlerThread;
import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Felipe on 13/04/2016.
 */
public class MetaMensagem {
    private String mensagem_ID;
    private String alvo;
    JSONObject dados=null;

    public MetaMensagem(String mensagem_ID){ this.mensagem_ID=mensagem_ID;  }

    public static ArrayList<MetaMensagem> constroiListaMensagens(final String usuario_ID)throws IOException {
        ArrayList<MetaMensagem> mensagens = new ArrayList<>();

        int qtdMensagens, i;
        BufferedReader respostaServidor = Conexao.conectaServidor(usuario_ID,"401");
        switch(respostaServidor.readLine()){
            case "401":
                //leitura da quantidade de comentarios
                qtdMensagens=Integer.parseInt(respostaServidor.readLine());
                String listaIDs[] = new String[qtdMensagens];

                for(i=0;i<qtdMensagens;i++){
                    //leitura do ID do solicitante
                    listaIDs[i]=respostaServidor.readLine();
                    //dado o ID, adicionamos aquele elemento na lista de solicitacoes
                    MetaMensagem essaMetaMensagem = new MetaMensagem(listaIDs[i]);
                    mensagens.add(essaMetaMensagem);
                }
                break;
            default:
                //nao há solicitacoes
                break;
        }
        return mensagens;
    }

    public static ArrayList<MetaMensagem> constroiConversa(final String logado_ID, final String alvo_ID) throws IOException {
        ArrayList<MetaMensagem> mensagens = new ArrayList<>();

            int qtdMensagens, i;
            BufferedReader respostaServidor = Conexao.conectaServidor(logado_ID +"\n"+alvo_ID,"403");
            switch(respostaServidor.readLine()){
                case "403":
                    //leitura da quantidade de comentarios
                    qtdMensagens=Integer.parseInt(respostaServidor.readLine());

                    for(i=0;i<qtdMensagens;i++){
                        //dado o ID, adicionamos aquele elemento na lista de mensagens
                        MetaMensagem essaMetaMensagem = new MetaMensagem(respostaServidor.readLine());
                        mensagens.add(essaMetaMensagem);
                    }
                    for(i=0;i<mensagens.size();i++){
                    }
                    break;
                default:
                    //nao há mensagens
                    break;
            }
        return mensagens;
    }

    public String getMensagemId() {
        return mensagem_ID;
    }

    public void setAlvo(String alvo) {
        this.alvo = alvo;
    }

    public String getAlvo() {
        return alvo;
    }
}
