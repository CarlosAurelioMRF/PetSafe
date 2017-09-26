package com.carlosaurelio.petsafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos Aurelio on 19/03/2016.
 */
public class UsuarioController extends PetSafeDatabase {

    public UsuarioController(Context context) {
        super(context);
    }

    public void insert(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put(PetSafeContract.PetSafeColumns.COLUMN_NOME, usuario.getNomePessoa());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_USUARIO, usuario.getNomeUsuario());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_SENHA, usuario.getSenhaUsuario());
        getWritableDatabase().insert(Tables.USUARIOS, null, values);
    }

    public void update(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put(PetSafeContract.PetSafeColumns.COLUMN_NOME, usuario.getNomePessoa());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_USUARIO, usuario.getNomeUsuario());
        values.put(PetSafeContract.PetSafeColumns.COLUMN_SENHA, usuario.getSenhaUsuario());
        getWritableDatabase().update(Tables.USUARIOS, values, "codigousuario = ?", new String[]{"" + usuario.getIdUsuario()});
    }

    public Usuario findById(Integer id) {
        String sql = "SELECT * FROM " + Tables.USUARIOS + " WHERE codigousuario = ?";
        String[] selectionArgs = new String[]{"" + id};
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();
        return montaUsuario(cursor);
    }

    public boolean delete(Integer codigoUsuario) {
        String[] selectionArgs = new String[]{"" + codigoUsuario};
        return getWritableDatabase().delete(PetSafeDatabase.Tables.USUARIOS,
                PetSafeContract.PetSafeColumns.COLUMN_CODIGO_USUARIO + " = ?",
                selectionArgs) > 0;
    }

    public List<Usuario> findUsuario(String searcbCriteria) {
        List<Usuario> retorno = new ArrayList<Usuario>();
        String sql = "SELECT * FROM " + Tables.USUARIOS +
                " WHERE " + PetSafeContract.PetSafeColumns.COLUMN_NOME + " LIKE '" + searcbCriteria.trim() + "%'" +
                " OR " + PetSafeContract.PetSafeColumns.COLUMN_USUARIO + " LIKE '" + searcbCriteria.trim() + "%'";
        Cursor cursor = getDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                retorno.add(montaUsuario(cursor));
            } while (cursor.moveToNext());
        }
        return retorno;
    }

    public String findUsuarioNome(String nomeUsuario) {
        String retorno = null;
        String sql = "SELECT * FROM " + Tables.USUARIOS +
                " WHERE " + PetSafeContract.PetSafeColumns.COLUMN_USUARIO + " = '" + nomeUsuario.trim() + "'";
        Cursor cursor = getDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            retorno = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_NOME));
        }

        return retorno;
    }

    public List<Usuario> findAll() {
        List<Usuario> retorno = new ArrayList<Usuario>();
        String sql = "SELECT * FROM " + Tables.USUARIOS;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                retorno.add(montaUsuario(cursor));
            } while (cursor.moveToNext());
        }
        return retorno;
    }

    public Usuario montaUsuario(Cursor cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }
        Integer id = cursor.getInt(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_CODIGO_USUARIO));
        String nome = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_NOME));
        String usuario = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_USUARIO));
        String senha = cursor.getString(cursor.getColumnIndex(PetSafeContract.PetSafeColumns.COLUMN_SENHA));
        return new Usuario(id, nome, usuario, senha);
    }

    public boolean validaLogin(String usuario, String senha) {
        String sql = "SELECT * FROM " + Tables.USUARIOS + " WHERE nomeusuario = ? AND senhausuario = ?";
        String[] selectionArgs = new String[]{usuario, senha};
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();

        Usuario user = montaUsuario(cursor);
        if (user == null || user.getNomeUsuario() == null || user.getSenhaUsuario() == null) {
            return false;
        }
        String informado = usuario + senha;
        String esperado = user.getNomeUsuario() + user.getSenhaUsuario();
        if (informado.equals(esperado)) {
            return true;
        }
        return false;
    }
}

