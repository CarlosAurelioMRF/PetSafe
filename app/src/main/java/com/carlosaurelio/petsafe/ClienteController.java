package com.carlosaurelio.petsafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos Aurelio on 22/03/2016.
 */
public class ClienteController extends PetSafeDatabase {

    public ClienteController(Context context) {
        super(context);
    }

    public void insert(Cliente cliente) {
        ContentValues values = new ContentValues();
        values.put(PetSafeContract.PetSafeColumns.COLUMN_NOME, cliente.getNomeCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_SEGUNDO_NOME, cliente.getSegundoNomeCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_CPF, cliente.getCpfCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_ENDERECO, cliente.getEnderecoCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_TELEFONE, cliente.getTelefoneCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_EMAIL, cliente.getEmailCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_DATA_NASCIMENTO, cliente.getDataNascimentoCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_INFORMACOES_ADCIONAIS, cliente.getObservacaoCliente());
        getWritableDatabase().insert(Tables.CLIENTES, null, values);
    }

    public void update(Cliente cliente) {
        ContentValues values = new ContentValues();
        values.put(PetSafeContract.PetSafeColumns.COLUMN_NOME, cliente.getNomeCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_SEGUNDO_NOME, cliente.getSegundoNomeCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_CPF, cliente.getCpfCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_ENDERECO, cliente.getEnderecoCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_TELEFONE, cliente.getTelefoneCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_EMAIL, cliente.getEmailCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_DATA_NASCIMENTO, cliente.getDataNascimentoCliente());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_INFORMACOES_ADCIONAIS, cliente.getObservacaoCliente());
        getWritableDatabase().update(Tables.CLIENTES, values, "codigocliente = ?", new String[]{"" + cliente.getCodigoCliente()});
    }

    public boolean delete(Integer codigoCliente) {
        String[] selectionArgs = new String[]{"" + codigoCliente};
        return getWritableDatabase().delete(Tables.CLIENTES,
                PetSafeContract.PetSafeColumns.COLUMN_CODIGO_CLIENTE + " = ?",
                selectionArgs) > 0;
    }

    public List<Cliente> findCliente(String searcbCriteria) {
        List<Cliente> retorno = new ArrayList<Cliente>();
        String sql = "SELECT * FROM " + Tables.CLIENTES +
                " WHERE " + PetSafeContract.PetSafeColumns.COLUMN_NOME + " LIKE '" + searcbCriteria.trim() + "%'";
        Cursor cursor = getDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                retorno.add(montaCliente(cursor));
            } while (cursor.moveToNext());
        }
        return retorno;
    }

    public List<Cliente> findAll() {
        List<Cliente> retorno = new ArrayList<Cliente>();
        String sql = "SELECT * FROM " + Tables.CLIENTES;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                retorno.add(montaCliente(cursor));
            } while (cursor.moveToNext());
        }
        return retorno;
    }

    public String findById(Integer codigoCliente) {
        String nome = null;
        String sql = "SELECT * FROM " + Tables.CLIENTES +
                " WHERE " + PetSafeContract.PetSafeColumns.COLUMN_CODIGO_CLIENTE + " = " + codigoCliente;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                nome = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_NOME));
            } while (cursor.moveToNext());
        }
        return nome;
    }

    public List<Cliente> findDono(Integer codigoCliente) {
        List<Cliente> retorno = new ArrayList<Cliente>();
        String sql = "SELECT * FROM " + Tables.CLIENTES +
                " WHERE " + PetSafeContract.PetSafeColumns.COLUMN_CODIGO_CLIENTE + " = " + codigoCliente;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                retorno.add(montaCliente(cursor));
            } while (cursor.moveToNext());
        }
        return retorno;
    }

    public Cliente montaCliente(Cursor cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }
        Integer id = cursor.getInt(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_CODIGO_CLIENTE));
        String nome = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_NOME));
        String segundoNome = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_SEGUNDO_NOME));
        String cpf = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_CPF));
        String endereco = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_ENDERECO));
        String telefone = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_TELEFONE));
        String email = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_EMAIL));
        String datanascimento = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_DATA_NASCIMENTO));
        String informacoesAdicionais = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_INFORMACOES_ADCIONAIS));
        return new Cliente(id, nome, segundoNome, cpf, endereco, telefone, email, datanascimento, informacoesAdicionais);
    }
}
