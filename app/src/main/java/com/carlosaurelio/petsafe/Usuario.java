package com.carlosaurelio.petsafe;

/**
 * Created by Carlos Aurelio on 19/03/2016.
 */
public class Usuario {

    private int idUsuario;
    private String nomePessoa;
    private String nomeUsuario;
    private String senhaUsuario;

    public Usuario(int idUsuario, String nomePessoa, String nomeUsuario, String senhaUsuario) {
        this.idUsuario = idUsuario;
        this.nomePessoa = nomePessoa;
        this.nomeUsuario = nomeUsuario;
        this.senhaUsuario = senhaUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }
}
