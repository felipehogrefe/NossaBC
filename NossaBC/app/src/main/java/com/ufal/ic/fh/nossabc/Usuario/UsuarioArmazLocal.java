package com.ufal.ic.fh.nossabc.Usuario;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Classe UsuarioArmazLocal:
 * Classe chave para tratamento de ações do usuário com o uso da propriedade SharedPreferences torna
 * capaz transitar dados do usuário entre activities sem necessidade da transmissão via intent.
 * Para isso usa um objeto MetaUsuario e guarda suas variáveis, entre as funcionalidades temos autenticar
 * e usuarioLogado usados para check de usuário.
 * Como qualquer objeto UsuarioArmazLocal possui a variável SP_NAME setada como "detalhesUsuario" não
 * há necessidade de setar qual DB é usado ou coisas do tipo.
 */
public class UsuarioArmazLocal {
    private static final String SP_NAME ="detalhesUsuario";
    private SharedPreferences usuarioArmazLocalDB;

    public UsuarioArmazLocal(Context context){
        usuarioArmazLocalDB = context.getSharedPreferences(SP_NAME, 0);
    }

    /**
     * Metodo que armazena os dados do usuário na variável usuarioArmazLocalDB
     * @param metaUsuario
     */
    public void armazenaDadosUsuario(MetaUsuario metaUsuario){
        SharedPreferences.Editor spEditor = usuarioArmazLocalDB.edit();
        spEditor.putString("metaUsuario", metaUsuario.getUsuario());
        spEditor.putString("senha", metaUsuario.getSenha());
        spEditor.putString("usuario_id", metaUsuario.getUsuarioId());
        spEditor.commit();
    }

    public MetaUsuario getUsuarioLogado(){
        String usuario = usuarioArmazLocalDB.getString("usuario", "");
        String senha = usuarioArmazLocalDB.getString("senha", "");
        String usuario_ID = usuarioArmazLocalDB.getString("usuario_id","");

        MetaUsuario metaUsuarioArmazenado = new MetaUsuario(usuario, senha, usuario_ID);
        return metaUsuarioArmazenado;
    }

    public String getIdUsuario(){
        return usuarioArmazLocalDB.getString("usuario_id","");
    }

    public static boolean autenticar(UsuarioArmazLocal usuarioArmazLocal) {
        return usuarioArmazLocal.usuarioLogado();
    }

    public void setUsuarioLogado(boolean logado){
        SharedPreferences.Editor spEditor = usuarioArmazLocalDB.edit();
        spEditor.putBoolean("logado", logado);
        spEditor.commit();
    }

    public void limparDadosUsuario(){
        SharedPreferences.Editor spEditor = usuarioArmazLocalDB.edit();
        spEditor.clear();
        spEditor.commit();
    }

    public boolean usuarioLogado(){
        if(usuarioArmazLocalDB.getBoolean("logado",false)==true){
            return true;
        }else{
            return false;
        }
    }
}
