package com.carlosaurelio.petsafe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Carlos Aurelio on 19/03/2016.
 */
public class PetSafeDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "safepet.db";
    private static final int DATABASE_VERSION = 2;
    private final Context mContext;
    protected SQLiteDatabase database;

    interface Tables {
        String USUARIOS = "usuarios";
        String CLIENTES = "clientes";
        String ANIMAIS = "animais";
    }

    public PetSafeDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USUARIO = "CREATE TABLE IF NOT EXISTS " + Tables.USUARIOS + "("
                + PetSafeContract.PetSafeColumns.COLUMN_CODIGO_USUARIO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PetSafeContract.PetSafeColumns.COLUMN_NOME + " TEXT NOT NULL,"
                + PetSafeContract.PetSafeColumns.COLUMN_USUARIO + " TEXT NOT NULL,"
                + PetSafeContract.PetSafeColumns.COLUMN_SENHA + " TEXT NOT NULL)";

        db.execSQL(CREATE_TABLE_USUARIO);

        String CREATE_TABLE_CLIENTES = "CREATE TABLE IF NOT EXISTS " + Tables.CLIENTES + "("
                + PetSafeContract.PetSafeColumns.COLUMN_CODIGO_CLIENTE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PetSafeContract.PetSafeColumns.COLUMN_NOME + " TEXT NOT NULL,"
                + PetSafeContract.PetSafeColumns.COLUMN_SEGUNDO_NOME + " TEXT NOT NULL,"
                + PetSafeContract.PetSafeColumns.COLUMN_CPF + " TEXT NOT NULL,"
                + PetSafeContract.PetSafeColumns.COLUMN_ENDERECO + " TEXT NOT NULL,"
                + PetSafeContract.PetSafeColumns.COLUMN_TELEFONE + " TEXT NOT NULL,"
                + PetSafeContract.PetSafeColumns.COLUMN_EMAIL + " TEXT NOT NULL,"
                + PetSafeContract.PetSafeColumns.COLUMN_DATA_NASCIMENTO + " TEXT NOT NULL,"
                + PetSafeContract.PetSafeColumns.COLUMN_INFORMACOES_ADCIONAIS + " TEXT NOT NULL)";

        db.execSQL(CREATE_TABLE_CLIENTES);

        String CREATE_TABLE_ANIMAIS = "CREATE TABLE IF NOT EXISTS " + Tables.ANIMAIS + "("
                + PetSafeContract.PetSafeColumns.COLUMN_CODIGO_ANIMAL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PetSafeContract.PetSafeColumns.COLUMN_CODIGO_CLIENTE + " INTEGER NOT NULL,"
                + PetSafeContract.PetSafeColumns.COLUMN_NOME_ANIMAL + " TEXT NOT NULL,"
                + PetSafeContract.PetSafeColumns.COLUMN_TIPO_ANIMAL + " INTEGER NOT NULL,"
                + PetSafeContract.PetSafeColumns.COLUMN_IDADE_ANIMAL + " TEXT,"
                + PetSafeContract.PetSafeColumns.COLUMN_PESO_ANIMAL + " TEXT,"
                + PetSafeContract.PetSafeColumns.COLUMN_FOTO_ANIMAL + " TEXT,"
                + " FOREIGN KEY (" + PetSafeContract.PetSafeColumns.COLUMN_CODIGO_CLIENTE + ") REFERENCES "
                + Tables.CLIENTES + "(" + PetSafeContract.PetSafeColumns.COLUMN_CODIGO_CLIENTE +"))";

        db.execSQL(CREATE_TABLE_ANIMAIS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;

        if (version == 1) {
            // Adicionar campos extras no banco sem deletar existente
            version = 2;
        }

        if (version != DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + Tables.USUARIOS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.CLIENTES);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.ANIMAIS);
            onCreate(db);
        }
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    public SQLiteDatabase getDatabase() {
        if (database == null) {
            database = getWritableDatabase();
        }
        return database;
    }
}