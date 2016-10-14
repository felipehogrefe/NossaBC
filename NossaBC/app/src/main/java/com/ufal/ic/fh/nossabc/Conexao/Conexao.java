package com.ufal.ic.fh.nossabc.Conexao;

import android.os.HandlerThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Felipe on 04/04/2016.
 * Classe criada com a finalidade de concentrar o funcionalidade de conexao com o servidor em um
 * unico metodo
 * Dado um corpo e uma funcao o metodo retorna um buffer que contem a resposta do servidor
 */
public class Conexao {
    final static String ipServidor= "146.148.66.128";
    final static int portaServidor = 4800;
    static CountDownLatch latch;
    static JSONObject jsonObject;

    /**
     * Conexao padrão com o servidor, retorna o buffer de resposta a ser tratado pela classe
     * que chamou o método. Não temos tratamento de thread aqui pois a utilização do buffer
     * requer o uso de thread, dessa forma a msm é definida no caller.
     * @param corpo: dados a serem enviados, como id de usuario, por exemplo
     * @param funcao: funcao a ser chamada no servidor
     * @return
     * @throws IOException
     */
    public static BufferedReader conectaServidor(String corpo, String funcao) throws IOException {
        Socket socketCliente = new Socket(ipServidor, portaServidor);
        DataOutputStream outToServer = new DataOutputStream(socketCliente.getOutputStream());
        BufferedReader respostaServidor = new BufferedReader(new
                InputStreamReader(socketCliente.getInputStream()));
        outToServer.writeBytes(funcao + '\n' + corpo + '\n');
        return respostaServidor;
    }

    /**
     * retorna o InputSream, solucao para transmição de imagem
     * @param funcao
     * @param corpo
     * @return
     * @throws IOException
     */
    public static InputStream getISR(String funcao, String corpo) throws IOException {
        Socket socketCliente = new Socket(ipServidor, portaServidor);
        DataOutputStream outToServer = new DataOutputStream(socketCliente.getOutputStream());
        InputStream iSR = socketCliente.getInputStream();
        //BufferedReader respostaServidor = new BufferedReader(iST);
        outToServer.writeBytes(funcao + '\n' + corpo + '\n');
        return iSR;
    }

    /**
     * retorna um soquete, de forma que o caller terá maior controle sobre as ações, podendo enviar
     * e receber dados diversas vezes
     * @return
     * @throws IOException
     */
    public static Socket conectaPersistenteServidor() throws IOException {
        Socket socketCliente = new Socket(ipServidor, portaServidor);
        return socketCliente;
    }

    /**
     * retorna um objeto JSON de acordo com os dados enviados ao servidor, como tratamos o buffer
     * aqui o caller n precisa estar dentro de uma thread
     * @param funcao
     * @param corpo
     * @return
     * @throws IOException
     */
    public static JSONObject jConectaServidor(final String funcao,final String corpo) throws
            IOException {
        latch = new CountDownLatch(1);
        Thread conexaoThread = new HandlerThread("conexaoHandler") {
            @Override
            public void run() {
                try {

                    Socket socketCliente = new Socket(ipServidor, portaServidor);
                    DataOutputStream outToServer = new
                            DataOutputStream(socketCliente.getOutputStream());
                    BufferedReader respostaServidor = new BufferedReader(new
                            InputStreamReader(socketCliente.getInputStream()));
                    outToServer.writeBytes(funcao+"\n"+corpo+"\n");
                    if(respostaServidor.readLine().equals(funcao)){
                        String entrada = respostaServidor.readLine();
                        jsonObject = new JSONObject(entrada);
                    }else{
                    }
                    latch.countDown();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        conexaoThread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
