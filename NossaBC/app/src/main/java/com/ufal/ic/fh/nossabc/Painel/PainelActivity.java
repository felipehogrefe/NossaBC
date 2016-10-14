package com.ufal.ic.fh.nossabc.Painel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ufal.ic.fh.nossabc.Mensagens.MensagensActivity;
import com.ufal.ic.fh.nossabc.R;
import com.ufal.ic.fh.nossabc.Solicitacao.SolicitacaoActivity;
import com.ufal.ic.fh.nossabc.Usuario.ConsultaUsuarios;
import com.ufal.ic.fh.nossabc.Usuario.UsuarioArmazLocal;

/**
 * Classe PainelActivity:
 * Dá a opção de logout. Em construção.
 */
public class PainelActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogout, btnSolicitacoes, btnMensagens, btnUsuarios;

    UsuarioArmazLocal usuarioArmazLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel);

        usuarioArmazLocal = new UsuarioArmazLocal(this);

        btnSolicitacoes = (Button) findViewById(R.id.btnSolicitações);
        btnSolicitacoes.setOnClickListener(this);
        btnMensagens = (Button) findViewById(R.id.btnMensagens);
        btnMensagens.setOnClickListener(this);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        btnUsuarios = (Button) findViewById(R.id.btnUsuarios);
        btnUsuarios.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnLogout:
                if(UsuarioArmazLocal.autenticar(usuarioArmazLocal)){
                    usuarioArmazLocal.limparDadosUsuario();
                    usuarioArmazLocal.setUsuarioLogado(false);
                }
                finish();
                break;
            case R.id.btnSolicitações:
                final Intent s = new Intent(this, SolicitacaoActivity.class);
                startActivity(s);
                break;
            case R.id.btnMensagens:
                final Intent m = new Intent(this, MensagensActivity.class);
                startActivity(m);
                break;
            case R.id.btnUsuarios:

                final Intent u = new Intent(this, ConsultaUsuarios.class);
                startActivity(u);
                break;
            default:
                break;
        }

    }
}
