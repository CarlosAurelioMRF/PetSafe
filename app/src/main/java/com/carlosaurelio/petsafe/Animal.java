package com.carlosaurelio.petsafe;

/**
 * Created by Carlos Aurelio on 27/03/2016.
 */
public class Animal {

    private int codigoAnimal;
    private int codigoCliente;
    private String nomeAnimal;
    private int tipoAnimal;
    private String idadeAnimal;
    private String pesoAnimal;
    private String photoAnimal;

    public Animal(int codigoAnimal, int codigoCliente, String nomeAnimal, int tipoAnimal, String idadeAnimal, String pesoAnimal, String photoAnimal) {
        this.codigoAnimal = codigoAnimal;
        this.codigoCliente = codigoCliente;
        this.nomeAnimal = nomeAnimal;
        this.tipoAnimal = tipoAnimal;
        this.idadeAnimal = idadeAnimal;
        this.pesoAnimal = pesoAnimal;
        this.photoAnimal = photoAnimal;
    }

    public String getPhotoAnimal() {
        return photoAnimal;
    }

    public void setPhotoAnimal(String photoAnimal) {
        this.photoAnimal = photoAnimal;
    }

    public int getCodigoAnimal() {
        return codigoAnimal;
    }

    public void setCodigoAnimal(int codigoAnimal) {
        this.codigoAnimal = codigoAnimal;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public int getTipoAnimal() {
        return tipoAnimal;
    }

    public void setTipoAnimal(int tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }

    public String getIdadeAnimal() {
        return idadeAnimal;
    }

    public void setIdadeAnimal(String idadeAnimal) {
        this.idadeAnimal = idadeAnimal;
    }

    public String getPesoAnimal() {
        return pesoAnimal;
    }

    public void setPesoAnimal(String pesoAnimal) {
        this.pesoAnimal = pesoAnimal;
    }
}
