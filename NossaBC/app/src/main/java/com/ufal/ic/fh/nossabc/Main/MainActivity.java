package com.ufal.ic.fh.nossabc.Main;

import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ufal.ic.fh.nossabc.ConsultaLivros.ConsultaActivity;
import com.ufal.ic.fh.nossabc.R;
import com.ufal.ic.fh.nossabc.Painel.LoginActivity;
import com.ufal.ic.fh.nossabc.Painel.PainelActivity;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;

/**
 * Classe MainActivity:
 * Primeiro layout a ser exibido, possui a propriedade usuarioArmazLocal pois se o usuário estiver
 * logado deverá exibir o botão PERFIL, caso contrário o botão LOGIN. Tem como objeto exbir o
 * layout activity_main_logado que se trata de um conjunto de opções iniciais
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnConsulta, btnLogin, btnPerfil;
    UsuarioArmazLocal usuarioArmazLocal;
    private ImageButton btnMainMain,btnMainMsg,btnMainSol,btnMainPain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        usuarioArmazLocal = new UsuarioArmazLocal(this);

        //definimos o layout a ser apresentado de acordo com o usuario logado
        //é possível apenas alterar o botão e checar no clique, entretanto foi feito dessa maneira a
        //titulo de aprendizado
        setaCampos();
    }

    /**
     * Implementação do onRestart é necessária pois ao fazer login (ou logout) deve ser exibido um
     * conjunto diferente de botões.
     */
    @Override
    protected void onRestart(){
        super.onRestart();



        setaCampos();
    }

    private void setaCampos() {

        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int tamanhoBtn = (int) (display.getWidth()/2);
        LinearLayout layoutMenu = (LinearLayout) findViewById(R.id.layoutMenu);
        ViewGroup.LayoutParams params = layoutMenu.getLayoutParams();
        params.width = tamanhoBtn;
        layoutMenu.setLayoutParams(params);


        btnPerfil =(Button) findViewById(R.id.btnPerfil);
        btnPerfil.setOnClickListener(this);
        if(UsuarioArmazLocal.autenticar(usuarioArmazLocal)){
            btnPerfil.setText("PAINEL");
        }else{
            btnPerfil.setText("LOGIN");
        }
        btnConsulta = (Button)findViewById(R.id.btnConsulta);
        btnConsulta.setOnClickListener(this);
        btnMainMain=(ImageButton)findViewById(R.id.btnMainMain);
        btnMainMain.setOnClickListener(this);
        btnMainMsg=(ImageButton)findViewById(R.id.btnMainMsg);
        btnMainMsg.setOnClickListener(this);
        btnMainSol=(ImageButton)findViewById(R.id.btnMainSol);
        btnMainSol.setOnClickListener(this);
        btnMainPain=(ImageButton)findViewById(R.id.btnMainPain);
        btnMainPain.setOnClickListener(this);
    }

    private void setaTamBtn() {

    }

    /**
     * Trata o clique do usuário
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnConsulta:
                final Intent t = new Intent(this, ConsultaActivity.class);
                startActivity(t);
                break;
            case R.id.btnLogin:
                //final Intent l = new Intent(this, LoginActivity.class);
                //startActivity(l);
                break;
            case R.id.btnPerfil:
                if(UsuarioArmazLocal.autenticar(usuarioArmazLocal)){
                    final Intent p = new Intent(this, PainelActivity.class);
                    startActivity(p);
                }else{
                    final Intent l = new Intent(this, LoginActivity.class);
                    startActivity(l);
                }
                break;
            case R.id.btnGuia:
                break;
        }
    }
}
