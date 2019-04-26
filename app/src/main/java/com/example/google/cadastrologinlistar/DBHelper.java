package com.example.google.cadastrologinlistar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    /**
     * OBJETIVO.......: NOME E VERSAO DO BANCO
     */
    private static String NOME  = "sqliteappcadastro.DB";
    private static int VERSAO   = 3;

    /**
     * OBJETIVO.......: NOME E VERSAO DO BANCO
     * @param context
     */
    public DBHelper(Context context){
        super(context, NOME, null, VERSAO);
    }

    /**
     * OBJETIVO.......: TABELA E SEUS CAMPOS, CADASTRAR SUPER USUARIO
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE [usuario] (\n" +
                "[id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[nome] VARCHAR(60)  NOT NULL,\n" +
                "[email] VARCHAR(60)  NOT NULL,\n" +
                "[senha] VARCHAR(60)  NOT NULL,\n" +
                "[dataNascimento] VARCHAR(11)  NOT NULL\n" +
                ")"
        );

        db.execSQL(
                "INSERT INTO usuario (nome, email, senha, dataNascimento) VALUES ('admin', 'admin@admin.com', '123', '01/01/1980')"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
