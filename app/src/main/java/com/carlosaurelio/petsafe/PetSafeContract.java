package com.carlosaurelio.petsafe;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Carlos Aurelio on 19/03/2016.
 */
public class PetSafeContract {

    interface PetSafeColumns {
        String COLUMN_CODIGO_USUARIO = "codigousuario";
        String COLUMN_NOME = "nomepessoa";
        String COLUMN_USUARIO = "nomeusuario";
        String COLUMN_SENHA = "senhausuario";

        String COLUMN_CODIGO_CLIENTE = "codigocliente";
        String COLUMN_SEGUNDO_NOME = "segundonome";
        String COLUMN_CPF = "cpfcliente";
        String COLUMN_ENDERECO = "endereco";
        String COLUMN_TELEFONE = "telefone";
        String COLUMN_EMAIL = "email";
        String COLUMN_DATA_NASCIMENTO = "datanascimento";
        String COLUMN_INFORMACOES_ADCIONAIS = "informacoesadicionais";

        String COLUMN_CODIGO_ANIMAL = "codigoanimal";
        String COLUMN_NOME_ANIMAL = "nomeanimal";
        String COLUMN_TIPO_ANIMAL = "tipoanimal";
        String COLUMN_IDADE_ANIMAL = "idadeanimal";
        String COLUMN_PESO_ANIMAL = "pesoanimal";
        String COLUMN_FOTO_ANIMAL = "photoanimal";
    }

    public static final String CONTENT_AUTHORITY = "com.carlosaurelio.petsafe.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_USUARIOS = "usuarios"; // Tabela "usuarios"
    private static final String PATH_CLIENTES = "clientes"; // Tabela "clientes"
    public static final Uri URI_TABLE = Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATH_USUARIOS);

    public static final String[] TOP_LEVEL_PATHS = {
            PATH_USUARIOS, PATH_CLIENTES
    };

    public static class PetSafe implements PetSafeColumns, BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_USUARIOS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd" + CONTENT_AUTHORITY + ".usuarios";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd" + CONTENT_AUTHORITY + ".usuarios";

        public static Uri buildUsuarioUri(String friendId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(friendId).build();
        }

        public static String getUsuarioId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

}
