package com.example.google.cadastrologinlistar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ConsultarActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView listViewUsuarios;
    private Button btnFechar;
    private Button btnCadastrar;
    private UsuarioAdapter usuarioAdapter;
    private ArrayList<Usuario> usuarios;
    private Usuario usuarioEdicao;

    /**
     * OBJETIVO.......: CARREGAR OS DADOS DA CONSULTA
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar);

        btnFechar           = findViewById(R.id.btnFechar);
        btnCadastrar    = findViewById(R.id.btnCadastrar);

        listViewUsuarios    = findViewById(R.id.listViewUsuarios);
        listViewUsuarios.setOnItemClickListener(this);

        usuarios            = new Usuario(this).getUsuarios();
        usuarioAdapter      = new UsuarioAdapter(this, usuarios);
        listViewUsuarios.setAdapter(usuarioAdapter);

        /**
         * OBJETIVO.......: IR PARA A TELA DE FUNCOES
         */
        btnFechar.setOnClickListener(this);

        /**
         * OBJETIVO.......: IR PARA A TELA DE CADASTRO
         */
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultarActivity.this, CadastrarActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * // OBJETIVO.......: FUNCAO PARA VOLTAR A MAIN ACTIVITY
     * @param v
     */
    @Override
    public void onClick(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("funcoes", true);
        startActivity(intent);
    }

    /**
     * OBJETIVO.......: QUANDO CLICAR EM UM ITEM DA LISTA, CARREGAR OS DADOS DO USUARIO PARA EDICAO
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Usuario usuario = usuarios.get(position);

        Intent intent = new Intent(this,CadastrarActivity.class);
        intent.putExtra("consulta", usuario.getId());
        usuarioEdicao = usuario;

        startActivity(intent);
    }

    /**
     * OBJETIVO.......: QUANDO ATUALIZAR UM REGISTRO ATUALIZAR O LIST VIEW REMOVENDO O REGISTRO EXCLUIDO
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(usuarioEdicao != null){
            usuarioEdicao.consultarUsuarioId(usuarioEdicao.getId());
            if (usuarioEdicao.isExcluir()){
                usuarios.remove(usuarioEdicao);
            }
            usuarioAdapter.notifyDataSetChanged();
        }
    }
}
