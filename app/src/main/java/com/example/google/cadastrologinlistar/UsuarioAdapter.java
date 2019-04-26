package com.example.google.cadastrologinlistar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * OBJETIVO.......: EXTENDE DA CLASSE ARRAY ADAPTER, QUE IRA INSERIR OS DADOS DENTRO DO LIST VIEW
 */
public class UsuarioAdapter extends ArrayAdapter<Usuario> {

    /**
     * OBJETIVO.......: VARIAVEIS
     */
    private  ArrayList<Usuario> usuarios;

    /**
     * OBJETIVO.......: CONSTRUTOR
     * @param context
     * @param usuarios
     */
    public UsuarioAdapter(@NonNull Context context, @NonNull ArrayList<Usuario> usuarios) {
        super(context, 0, usuarios);
        this.usuarios = usuarios;
    }

    /**
     * OBJETIVO.......: SOBREESCREVER O METODO GET VIEW
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Usuario usuario = usuarios.get(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_usuario, null);

        TextView textViewNome           = convertView.findViewById(R.id.textViewNome);
        // CAMPO NAO NECESSARIO
        //TextView textViewEmail          = convertView.findViewById(R.id.textViewEmail);
        TextView textViewDatanascimento = convertView.findViewById(R.id.textViewDataNascimento);

        textViewNome.setText(usuario.getNome().toString());
        // CAMPO NAO NECESSARIO
        //textViewEmail.setText(usuario.getEmail().toString());
        textViewDatanascimento.setText(usuario.getDataNascimento().toString());

        return convertView;
    }
}
