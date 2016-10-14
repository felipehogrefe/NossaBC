package com.ufal.ic.fh.nossabc.Mapa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

public class MapaActivity extends AppCompatActivity implements View.OnClickListener{
    private boolean primeiraTela=true;
    private TextView txtEntrada;
    private Button btnZoom;
    private ImageView imgMapa;
    private Bitmap mapaFull, mapaZoom;
    static CountDownLatch latch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        //pegar o mapa aqui, desabilitado, metodo: não-funcional
        //recebeMapa();

        imgMapa = (ImageView) findViewById(R.id.imgMapa);
        txtEntrada = (TextView) findViewById(R.id.txtEntrada);
        btnZoom = (Button) findViewById(R.id.btnZoom);
        btnZoom.setOnClickListener(this);

        if(primeiraTela){
            txtEntrada.setText("ENTRADA");
            btnZoom.setText("ZOOM +");
            imgMapa.setImageBitmap(mapaFull);
            //exibe o mapa certo de acordo com o livro
            if(pegaLivro(getIntent().getStringExtra("LIVRO_ID"))){
                imgMapa.setImageResource(R.drawable.mapasetor1);
            }else{
                imgMapa.setImageResource(R.drawable.mapasetor4);
            }
        }else{
            txtEntrada.setText("");
            btnZoom.setText("ZOOM -");
            imgMapa.setImageBitmap(mapaZoom);
            if(pegaLivro(getIntent().getStringExtra("LIVRO_ID"))){
                imgMapa.setImageResource(R.drawable.zoom1);
            }else{
                imgMapa.setImageResource(R.drawable.zoom4);
            }
        }
    }

    /**
     * método criado a titulo de demonstracao
     * @param id
     * @return
     */
    private boolean pegaLivro(String id) {
        if(id.equals("1001")||id.equals("1002")||id.equals("1011")||id.equals("1012")){
            return true;
        }
        return false;
    }

    private void recebeMapa() {
        latch = new CountDownLatch(1);
        Thread conexaoThread = new HandlerThread("conexaoHandler") {
            @Override
            public void run() {
                try {
                    BufferedReader resposta = Conexao.conectaServidor(getIntent().getStringExtra("LIVRO_ID"),"600");

                    String imageMF = resposta.readLine();
                    byte[] encodedMF = Base64.encode(imageMF.getBytes(),Base64.DEFAULT);
                    byte[] decodedMF = Base64.decode(encodedMF, Base64.DEFAULT);
                    mapaFull = BitmapFactory.decodeByteArray(decodedMF, 0, decodedMF.length);
                    System.out.println(imageMF);

                    String imageMZ = resposta.readLine();
                    byte[] encodedMZ = Base64.encode(imageMZ.getBytes(),Base64.DEFAULT);
                    byte[] decodedMZ = Base64.decode(encodedMZ, Base64.DEFAULT);
                    mapaZoom = BitmapFactory.decodeByteArray(decodedMZ, 0, decodedMZ.length);

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
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnZoom:
                //faz alterações de acordo com a tela em que está
                if(primeiraTela){
                    txtEntrada.setText("");
                    txtEntrada.setBackgroundColor(0xFFFFFFFF);
                    btnZoom.setText("ZOOM -");
                    //imgMapa.setImageBitmap(mapaZoom);
                    if(pegaLivro(getIntent().getStringExtra("LIVRO_ID"))){
                        imgMapa.setImageResource(R.drawable.zoom1);
                    }else{
                        imgMapa.setImageResource(R.drawable.zoom4);
                    }
                    primeiraTela=false;
                }else{
                    txtEntrada.setText("ENTRADA");
                    btnZoom.setText("ZOOM +");
                    txtEntrada.setBackgroundColor(0xe7c0c0);
                    //imgMapa.setImageBitmap(mapaFull);
                    if(pegaLivro(getIntent().getStringExtra("LIVRO_ID"))){
                        imgMapa.setImageResource(R.drawable.mapasetor1);
                    }else{
                        imgMapa.setImageResource(R.drawable.mapasetor4);
                    }
                    primeiraTela=true;
                }
                break;
        }
    }
}
