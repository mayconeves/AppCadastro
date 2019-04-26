package com.example.google.cadastrologinlistar;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

public class Usuario {

    /**
     * OBJETIVO.......: CAMPOS
     */
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String dataNascimento;

    /**
     * OBJETIVO.......: CAMPOS AUXILIARES
     */
    private boolean excluir;
    private Context context;

    /**
     * OBJETIVO.......: GETTERS E SETTERS
     * @param context
     */
    public Usuario(Context context) {
        this.context = context;
        id = -1;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public boolean isExcluir() {
        return excluir;
    }

    public void setExcluir(boolean excluir) {
        this.excluir = excluir;
    }

    /**
     * OBJETIVO.......: METODO CADASTRAR / EDITAR
     * @return
     */
    public boolean salvar() {

        DBHelper dbHelper               = null;
        SQLiteDatabase sqLiteDatabase   = null;

        try {

            dbHelper        = new DBHelper(context);
            sqLiteDatabase  = dbHelper.getWritableDatabase();
            String sql      = "";

            if (id == -1) {
                // OBJETIVO.......: REGISTRO NOVO
                sql = "INSERT INTO usuario (nome, email, senha, dataNascimento) VALUES (?,?,?,?)";

            } else {
                // OBJETIVO.......: EDITAR
                sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, dataNascimento = ? WHERE id = ?";
            }

            sqLiteDatabase.beginTransaction();
            SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
            sqLiteStatement.clearBindings();

            sqLiteStatement.bindString(1, nome);
            sqLiteStatement.bindString(2, email);
            sqLiteStatement.bindString(3, senha);
            sqLiteStatement.bindString(4, dataNascimento);
            if( id != -1){
                sqLiteStatement.bindLong(5, id);
            }

            sqLiteStatement.executeInsert();

            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();

            return true;

        } catch (Exception e) {

            /**
             * OBJETIVO.......: IMPRIMIR ERRO
             */
            e.printStackTrace();
            sqLiteDatabase.endTransaction();
            return false;

        } finally {

            /**
             * // OBJETIVO.......: FINALIZAR CONEXAO
             */
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
            if (dbHelper != null) {
                dbHelper.close();
            }

        }
    }

    /**
     * OBJETIVO.......: METODO EXCLUIR
     * @return
     */
    public boolean excluir(){

        DBHelper dbHelper = null;
        SQLiteDatabase sqLiteDatabase = null;

        try {

            dbHelper = new DBHelper(context);
            sqLiteDatabase = dbHelper.getWritableDatabase();

            sqLiteDatabase.beginTransaction();

            sqLiteDatabase.delete("usuario", "id = ?", new String[]{ String.valueOf( id )});
            excluir = true;

            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();

            return true;

        } catch (Exception e) {

            /**
             * OBJETIVO.......: IMPRIMIR ERRO
             */
            e.printStackTrace();
            sqLiteDatabase.endTransaction();
            return false;

        } finally {

            /**
             * // OBJETIVO.......: FINALIZAR CONEXAO
             */
            if (dbHelper != null) {
                dbHelper.close();
            }

        }
    }

    /**
     * OBJETIVO.......: SELECIONAR UM USUARIO ESPECIFICO
     * @param id
     */
    public void consultarUsuarioId(int id){

        DBHelper dbHelper = null;
        SQLiteDatabase  sqLiteDatabase = null;
        Cursor cursor = null;

        try{

            dbHelper        = new DBHelper(context);
            sqLiteDatabase  = dbHelper.getReadableDatabase();
            cursor          = sqLiteDatabase.query("usuario", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);

            excluir = true;

            while ( cursor.moveToNext() ){
                this.id         = cursor.getInt(cursor.getColumnIndex("id"));
                nome            = cursor.getString(cursor.getColumnIndex("nome"));
                email           = cursor.getString(cursor.getColumnIndex("email"));
                senha           = cursor.getString(cursor.getColumnIndex("senha"));
                dataNascimento  = cursor.getString(cursor.getColumnIndex("dataNascimento"));

                excluir = false;
            }

        }catch (Exception e){

            /**
             * OBJETIVO.......: IMPRIMIR ERRO
             */
            e.printStackTrace();

        }finally {

            /**
             * OBJETIVO.......: FINALIZAR CONEXAO
             */
            if( ( cursor != null ) && ( !cursor.isClosed() ) ){
                cursor.close();
            }
            if( sqLiteDatabase != null ){
                sqLiteDatabase.close();
            }
            if( dbHelper != null ){
                dbHelper.close();
            }
        }
    }

    /**
     * OBJETIVO.......: SELECIONAR TODOS OS REGISTROS
     * @return
     */
    public ArrayList<Usuario> getUsuarios(){

        DBHelper dbHelper               = null;
        SQLiteDatabase  sqLiteDatabase  = null;
        Cursor cursor                   = null;
        ArrayList<Usuario> usuarios     = new ArrayList<>();

        try{
            /**
             * OBJETIVO.......: SELECT, PERCORRER CADA REGISTRO
             */
            dbHelper        = new DBHelper(context);
            sqLiteDatabase  = dbHelper.getReadableDatabase();
            cursor          = sqLiteDatabase.query("usuario", null, null, null, null, null, null);

            while ( cursor.moveToNext() ){

                Usuario usuario         = new Usuario(context);
                usuario.id              = cursor.getInt(cursor.getColumnIndex("id"));
                usuario.nome            = cursor.getString(cursor.getColumnIndex("nome"));
                usuario.email           = cursor.getString(cursor.getColumnIndex("email"));
                usuario.senha           = cursor.getString(cursor.getColumnIndex("senha"));
                usuario.dataNascimento  = cursor.getString(cursor.getColumnIndex("dataNascimento"));

                usuarios.add(usuario);

            }

        }catch (Exception e){

            /**
             * OBJETIVO.......: IMPRIMIR ERRO
             */
            e.printStackTrace();

        }finally {

            /**
             * OBJETIVO.......: FINALIZAR CONEXAO
             */
            if( ( cursor != null ) && ( !cursor.isClosed() ) ){
                cursor.close();
            }
            if( sqLiteDatabase != null ){
                sqLiteDatabase.close();
            }
            if( dbHelper != null ){
                dbHelper.close();
            }
        }
        return usuarios;
    }

    /**
     * OBJETIVO.......: CONTADOR DE REGISTROS
     * @return
     */
    public long contarUsuarios(){

        DBHelper dbHelper               = null;
        SQLiteDatabase  sqLiteDatabase  = null;
//
        dbHelper        = new DBHelper(context);
        sqLiteDatabase  = dbHelper.getReadableDatabase();

        long quantidade = DatabaseUtils.queryNumEntries( sqLiteDatabase, "usuario");
        return quantidade;
    }

    /**
     * OBJETIVO.......: VERIFICAR SE EXISTE USUARIO NO BANCO PARA AUTENTICAR
     * @param email
     * @param senha
     * @return
     */
    public boolean validarUsuario(String email, String senha){

        DBHelper dbHelper               = null;
        SQLiteDatabase  sqLiteDatabase  = null;
        Cursor cursor                   = null;

        boolean encontrado = false;

        try {

            /**
             * OBJETIVO.......: SELECT REGISTRO
             */
            dbHelper        = new DBHelper(context);
            sqLiteDatabase  = dbHelper.getReadableDatabase();
            cursor          = sqLiteDatabase.query("usuario", null, "email = ? and senha = ?", new String[]{String.valueOf(email), String.valueOf(senha)}, null, null, null);

            if ( cursor.moveToFirst() ){

                this.email = cursor.getString(cursor.getColumnIndex("email"));
                this.senha = cursor.getString(cursor.getColumnIndex("senha"));

                if( this.email.equals(email) && this.senha.equals(senha) ){
                    encontrado = true;
                }
            }
            cursor.close();

        }catch (Exception e){
            /**
             * OBJETIVO.......: IMPRIMIR ERRO
             */
            e.printStackTrace();

        }finally {
            /**
             * OBJETIVO.......: FINALIZAR CONEXAO
             */
            if( ( cursor != null ) && ( !cursor.isClosed() ) ){
                cursor.close();
            }
            if( sqLiteDatabase != null ){
                sqLiteDatabase.close();
            }
            if( dbHelper != null ){
                dbHelper.close();
            }
        }
        return encontrado;
    }
}
