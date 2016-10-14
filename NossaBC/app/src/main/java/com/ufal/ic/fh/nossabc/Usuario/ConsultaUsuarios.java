package com.ufal.ic.fh.nossabc.Usuario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ufal.ic.fh.nossabc.Conexao.Conexao;
import com.ufal.ic.fh.nossabc.ConsultaLivros.ResultadosConsultaActivity;
import com.ufal.ic.fh.nossabc.Livro.MetaLivro;
import com.ufal.ic.fh.nossabc.R;

import java.io.BufferedReader;
import java.util.ArrayList;

public class ConsultaUsuarios extends AppCompatActivity implements View.OnClickListener{
    private Button btnVoltarConsultaUsuario, btnBuscarUsuario;
    private EditText etNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_usuarios);

        etNome = (EditText) findViewById(R.id.etNome);
        btnVoltarConsultaUsuario = (Button) findViewById(R.id.btnVoltarConsultaUsuario);
        btnBuscarUsuario = (Button) findViewById(R.id.btnBuscarUsuario);

        btnVoltarConsultaUsuario.setOnClickListener(this);
        btnBuscarUsuario.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnVoltarConsultaUsuario:
                finish();
                break;
            case R.id.btnBuscarUsuario:
                final Intent intentResultados = new Intent(this, ResultadosUsuarios.class);
                String valor = etNome.getText().toString();
                intentResultados.putExtra("NOME", valor);
                startActivity(intentResultados);
                break;
        }
    }
}
