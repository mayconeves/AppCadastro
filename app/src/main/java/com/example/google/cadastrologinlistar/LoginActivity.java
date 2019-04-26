package com.example.google.cadastrologinlistar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUsuario;
    private EditText txtSenha;
    private Button btnEntrar;

    private final Usuario usuario = new Usuario(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsuario  = findViewById(R.id.txtUsuario);
        txtSenha    = findViewById(R.id.txtSenha);
        btnEntrar   = findViewById(R.id.btnEntrar);

        /**
         * OBJETIVO.......: AO CLICAR EM ENTRAR, FAZER VERIFICAOES E PREFERENCIAS
         */
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validar = true;
                String email  = txtUsuario.getText().toString().toLowerCase();
                String senha = txtSenha.getText().toString().toLowerCase();

                if(email.equals("")){
                    txtUsuario.setError("Campo obrigatório");
                    validar = false;
                }

                if(senha.equals("")){
                    txtSenha.setError("Campo obrigatório");
                    validar = false;
                }

                if(validar){

                    /**
                     * OBJETIVO.......: VALIDAR USUARIO AO BANCO
                     */
                    boolean validado = usuario.validarUsuario( email, senha );

                    if( validado ){
                        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.ArquivoPreferencia), 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email);
                        editor.putString("senha", senha);
                        editor.putBoolean("autenticado", true);
                        editor.commit();

                        boolean entrou = sharedPreferences.getBoolean("autenticado", true);

                        if( sharedPreferences.contains("autenticado") && entrou == true){

                            Intent intent = new Intent(LoginActivity.this, ConsultarActivity.class);
                            startActivity(intent);
                        }

                    }else{
                        /**
                         * OBJETIVO.......: MENSAGEM DE FALHA NA AUTENTICACAO
                         */
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, "Usuário ou senha não cadastrados.", duration);
                        toast.show();
                    }
                }
            }
        });
    }
}
