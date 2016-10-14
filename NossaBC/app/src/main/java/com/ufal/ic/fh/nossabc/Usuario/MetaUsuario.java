package com.ufal.ic.fh.nossabc.Usuario;

/**
 * Classe MetaUsuario:
 * Classe criada com o intuido de tratar das ações do usuário, em geral deve armazenar dados sobre o
 * mesmo, além do ID
 */
public class MetaUsuario {
    private String usuario, senha, usuario_ID, nome, curso;

    public MetaUsuario(String usuario_ID){

        this.usuario_ID=usuario_ID;
    }
    public MetaUsuario(String usuario, String senha, String usuario_ID){
        this.usuario=usuario;
        this.senha=senha;
        this.usuario_ID=usuario_ID;
    }

    public String getUsuario() { return usuario; }
    public String getSenha() { return senha; }
    public String getUsuarioId() { return usuario_ID;  }
    public String getNome() { return this.nome; }
    public String getCurso() { return this.curso; }

    public void setCurso(String curso) { this.curso = curso; }
    public void setNome(String nome) { this.nome = nome; }
}
