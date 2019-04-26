package com.example.google.cadastrologinlistar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CadastrarActivity extends AppCompatActivity {

    private EditText txtNome;
    private EditText txtEmail;
    private EditText txtSenha;
    private EditText txtDataNascimento;

    private Button btnExcluir;
    private Button btnSalvar;
    private Button btnCancelar;

    private final Usuario usuario = new Usuario(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        txtNome             = findViewById(R.id.txtNome);
        txtEmail            = findViewById(R.id.txtEmail);
        txtSenha            = findViewById(R.id.txtSenha);
        txtDataNascimento   = findViewById(R.id.txtDataNascimento);

        btnExcluir          = findViewById(R.id.btnExcluir);
        btnSalvar           = findViewById(R.id.btnSalvar);
        btnCancelar         = findViewById(R.id.btnCancelar);

        /**
         * OBJETIVO.......: APLICAR MASCARAS
         */
        txtDataNascimento.addTextChangedListener(Mascaras.insert("##/##/####", txtDataNascimento));

        /**
         * OBJETIVO.......: PRESSIONADO BOTAO EXCLUIR, EXIBIR TOAST AO CONCLUIR
         */
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CadastrarActivity.this)
                    .setTitle("Cuidado")
                    .setMessage("Tem certeza que deseja remover este registro?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            usuario.excluir();

                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, "Registro removido com sucesso.", duration);
                            toast.show();

                            finish();
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
            }
        });

        /**
         * OBJETIVO.......: PRESSIONADO BOTAO SALVAR, VALIDAR CAMPOS E EXIBIR MENSAGEM DE SUCESSO
         */
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validar = true;
                usuario.setNome(txtNome.getText().toString().trim());
                usuario.setEmail(txtEmail.getText().toString().trim().toLowerCase());
                usuario.setSenha(txtSenha.getText().toString().trim());
                usuario.setDataNascimento(txtDataNascimento.getText().toString());

                if(usuario.getNome().equals("")){
                    txtNome.setError(getString(R.string.CampoObrigatorio));
                    validar = false;
                }
                if(usuario.getEmail().equals("")){
                    txtEmail.setError(getString(R.string.CampoObrigatorio));
                    validar = false;
                }
                if(usuario.getSenha().equals("")){
                    txtSenha.setError(getString(R.string.CampoObrigatorio));
                    validar = false;
                }
                if(usuario.getDataNascimento().equals("")){
                    txtDataNascimento.setError(getString(R.string.CampoObrigatorio));
                    validar = false;
                }

                if(validar){
                    usuario.salvar();

                    // SE FOR POR DIALOG
                    /*AlertDialog.Builder adb = new AlertDialog.Builder(CadastrarActivity.this);
                    adb.setTitle("Sucesso");
                    adb.setMessage("Cadastro realizado com sucesso.");

                    // OBJETIVO.......: BOTAO PARA FECHAR CAIXA DE DIALOGO
                    adb.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            finish();
                        }
                    });
                    adb.show();*/

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "Operação realizada com sucesso.", duration);
                    toast.show();
                    finish();

                    Intent intent = new Intent(CadastrarActivity.this, ConsultarActivity.class);
                    startActivity(intent);
                }
            }
        });

        /**
         * OBJETIVO.......: PRESSIONADO O BOTAO CANCELAR
         */
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * OBJETIVO.......: IDENTIFICAR DE ONDE ESTA VINDO, SE DA TELA PRINCIPAL(sem parametro) OU DA TELA DE LISTAR(com parametro)
         */
        if(getIntent().getExtras() != null ){
            setTitle(getString(R.string.EditarTitleUsuario));
            int id = getIntent().getExtras().getInt("consulta");

            usuario.consultarUsuarioId(id);

            txtNome.setText(usuario.getNome().toString());
            txtEmail.setText(usuario.getEmail().toString());
            txtSenha.setText(usuario.getSenha().toString());
            txtDataNascimento.setText(usuario.getDataNascimento());

        }else{
           setTitle(getString(R.string.CadastrarTitleNovoUsuario));
        }

        /**
         * OBJETIVO.......: DESABILITAR O BOTAO EXCLUIR QNDO NAO HOUVER NENHUM REGISTRO, OU QNDO FOR CADASTRAR UM NOVO USUARIO
         */
        btnExcluir.setEnabled(true);
        btnExcluir.setVisibility(View.VISIBLE);
        if(usuario.getId() == -1) {
            btnExcluir.setEnabled(false);
            btnExcluir.setVisibility(View.INVISIBLE);
        }

    }

}
