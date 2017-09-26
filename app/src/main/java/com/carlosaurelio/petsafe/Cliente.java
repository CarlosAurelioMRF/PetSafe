package com.carlosaurelio.petsafe;

/**
 * Created by Carlos Aurelio on 21/03/2016.
 */
public class Cliente {

    private int codigoCliente;
    private String nomeCliente;
    private String segundoNomeCliente;
    private String cpfCliente;
    private String enderecoCliente;
    private String telefoneCliente;
    private String emailCliente;
    private String dataNascimentoCliente;
    private String observacaoCliente;

    public Cliente(int codigoCliente, String nomeCliente, String segundoNomeCliente, String cpfCliente, String enderecoCliente, String telefoneCliente, String emailCliente, String dataNascimentoCliente, String observacaoCliente) {
        this.codigoCliente = codigoCliente;
        this.nomeCliente = nomeCliente;
        this.segundoNomeCliente = segundoNomeCliente;
        this.cpfCliente = cpfCliente;
        this.enderecoCliente = enderecoCliente;
        this.telefoneCliente = telefoneCliente;
        this.emailCliente = emailCliente;
        this.dataNascimentoCliente = dataNascimentoCliente;
        this.observacaoCliente = observacaoCliente;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getSegundoNomeCliente() {
        return segundoNomeCliente;
    }

    public void setSegundoNomeCliente(String segundoNomeCliente) {
        this.segundoNomeCliente = segundoNomeCliente;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public String getEnderecoCliente() {
        return enderecoCliente;
    }

    public void setEnderecoCliente(String enderecoCliente) {
        this.enderecoCliente = enderecoCliente;
    }

    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getDataNascimentoCliente() {
        return dataNascimentoCliente;
    }

    public void setDataNascimentoCliente(String dataNascimentoCliente) {
        this.dataNascimentoCliente = dataNascimentoCliente;
    }

    public String getObservacaoCliente() {
        return observacaoCliente;
    }

    public void setObservacaoCliente(String observacaoCliente) {
        this.observacaoCliente = observacaoCliente;
    }
}
