package com.carlosaurelio.petsafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos Aurelio on 27/03/2016.
 */
public class AnimalController extends PetSafeDatabase {

    public AnimalController(Context context) {
        super(context);
    }

    public void insert(Animal animal) {
        ContentValues values = new ContentValues();
        values.put(PetSafeContract.PetSafeColumns.COLUMN_CODIGO_CLIENTE, animal.getCodigoCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_NOME_ANIMAL, animal.getNomeAnimal());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_TIPO_ANIMAL, animal.getTipoAnimal());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_IDADE_ANIMAL, animal.getIdadeAnimal());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_PESO_ANIMAL, animal.getPesoAnimal());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_FOTO_ANIMAL, animal.getPhotoAnimal());
        getWritableDatabase().insert(Tables.ANIMAIS, null, values);
    }

    public void update(Animal animal) {
        ContentValues values = new ContentValues();
        values.put(PetSafeContract.PetSafeColumns.COLUMN_CODIGO_CLIENTE, animal.getCodigoCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_NOME_ANIMAL, animal.getNomeAnimal());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_TIPO_ANIMAL, animal.getTipoAnimal());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_IDADE_ANIMAL, animal.getIdadeAnimal());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_PESO_ANIMAL, animal.getPesoAnimal());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_FOTO_ANIMAL, animal.getPhotoAnimal());
        getWritableDatabase().update(Tables.ANIMAIS, values, "codigoanimal = ?", new String[]{"" + animal.getCodigoAnimal()});
    }

    public boolean delete(Integer codigoAnimal) {
        String[] selectionArgs = new String[]{"" + codigoAnimal};
        return  getWritableDatabase().delete(Tables.ANIMAIS,
                PetSafeContract.PetSafeColumns.COLUMN_CODIGO_ANIMAL + " = ?",
                selectionArgs) > 0;
    }

    public List<Animal> findAll() {
        List<Animal> retorno = new ArrayList<Animal>();
        String sql = "SELECT * FROM " + Tables.ANIMAIS;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                retorno.add(montaAnimal(cursor));
            } while (cursor.moveToNext());
        }
        return retorno;
    }

    public List<Animal> findPorCliente(int codigoCliente) {
        List<Animal> retorno = new ArrayList<Animal>();
        String sql = "SELECT * FROM " + Tables.ANIMAIS +
                " WHERE " + PetSafeContract.PetSafeColumns.COLUMN_CODIGO_CLIENTE + " = " + codigoCliente;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                retorno.add(montaAnimal(cursor));
            } while (cursor.moveToNext());
        }
        return retorno;
    }

    public List<Animal> findAnimal(String searcbCriteria) {
        List<Animal> retorno = new ArrayList<Animal>();
        String sql = "SELECT * FROM " + Tables.ANIMAIS +
                " WHERE " + PetSafeContract.PetSafeColumns.COLUMN_NOME_ANIMAL + " LIKE '" + searcbCriteria.trim() + "%'";
        Cursor cursor = getDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                retorno.add(montaAnimal(cursor));
            } while (cursor.moveToNext());
        }
        return retorno;
    }

    public Animal montaAnimal(Cursor cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }
        Integer id = cursor.getInt(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_CODIGO_ANIMAL));
        Integer idCliente = cursor.getInt(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_CODIGO_CLIENTE));
        String nome = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_NOME_ANIMAL));
        int tipo = cursor.getInt(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_TIPO_ANIMAL));
        String idade = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_IDADE_ANIMAL));
        String peso = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_PESO_ANIMAL));
        String photo = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_FOTO_ANIMAL));
        return new Animal(id, idCliente, nome, tipo, idade, peso, photo);
    }
}