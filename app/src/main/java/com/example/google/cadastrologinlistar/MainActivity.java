package com.example.google.cadastrologinlistar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnCadastrar;
    private Button btnConsultar;
    private Button btnSair;

    private final Usuario usuario = new Usuario(this);
    private boolean saiu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * OBJETIVO.......: VERIFICAR AUTENTICACAO
         */
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.ArquivoPreferencia), 0);
        saiu = sharedPreferences.getBoolean("autenticado", false);

        //Log.i("Resultado : Autentic", String.valueOf(saiu));

        /**
         * OBJETIVO.......: SE NAO AUTENTICADO OU SAIU
         */
        if( !sharedPreferences.contains("autenticado") && saiu == false ){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            /**
             * OBJETIVO.......: SE JA ESTIVER AUTENTICADO MAS QUER VER A TELA DE FUNCOES
             */
            if ( getIntent().getExtras() == null ){
                Intent intent = new Intent(MainActivity.this, ConsultarActivity.class);
                startActivity(intent);
            }

            /**
             * OBJETIVO.......: EXIBIR MENSAGEM A CADA 2 MINUTOS
             */
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    /**
                     * OBJETIVO.......: VERIFICAR A QUANTIDADE DE REGISTROS NO BANCO
                     * MENSAGEM INFORMANDO A QUANTIDADE DE USUARIOS CADASTRADOS
                     */
                    long contarUsuarios =  usuario.contarUsuarios();
                    String mensagem;
                    if( contarUsuarios > 1){
                        mensagem = "Existem " + contarUsuarios + " usuários cadastrados.";
                    }else{
                        mensagem = "Existe " + contarUsuarios + " usuário cadastrado.";
                    }

                    /**
                     * SE FOR POR TOAST
                     * OBJETIVO.......: MENSAGEM INFORMANDO A QUANTIDADE DE USUARIOS CADASTRADOS
                     */
                    //int duration = Toast.LENGTH_SHORT;
                    //Toast toast  = Toast.makeText(context, mensagem, duration);
                    //toast.show();

                    /**
                     * SE FOR POR NOTIFICATION
                     * OBJETIVO.......: MENSAGEM INFORMANDO A QUANTIDADE DE USUARIOS CADASTRADOS
                     * EXIBIR APENAS SE ESTIVER AUTENTICADO
                     */
                    SharedPreferences logado = getSharedPreferences(getResources().getString(R.string.ArquivoPreferencia), 0);
                    if ( logado.contains("autenticado") ) {
                        criarNotificacao("Seu App", mensagem);
                    }

                    /**
                     * OBJETIVO.......: REPETIR A CADA 2 MINUTOS
                     */
                    handler.postDelayed(this, 12000); // 2 min = 120000

                }
            }, 3000); // 2 min = 120000
        }

        btnCadastrar    = findViewById(R.id.btnCadastrar);
        btnConsultar    = findViewById(R.id.btnConsultar);
        btnSair         = findViewById(R.id.btnSair);

        /**
         * OBJETIVO.......: IR PARA TELA DE CADASTRO
         */
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastrarActivity.class);
                startActivity(intent);
            }
        });

        /**
         * OBJETIVO.......: IR PARA TELA DE CONSULTAS
         */
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConsultarActivity.class);
                startActivity(intent);
            }
        });

        /**
         * OBJETIVO.......: DESLOGAR, ALTERA O CONTEUDO DO SHAREDPREFERENCES
         */
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.ArquivoPreferencia), 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("email");
                editor.remove("senha");
                editor.remove("autenticado");
                editor.apply();

                if( !sharedPreferences.contains("autenticado") ){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * NOTIFICACAO
     * @param title
     * @param message
     * OBJETIVO.......: CRIAR NOTIFICACAO
     * by https://medium.com/cr8resume/notification-in-android-8-0-oreo-implementing-notification-channels-d65b0f81ca50
     */

    public void criarNotificacao(String title, String message) {

        Context context = getApplicationContext();

        NotificationManager notificationManager;
        NotificationCompat.Builder builder;

        Intent resultIntent = new Intent(context , ConsultarActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_action_notification_icon);
        builder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setContentIntent(resultPendingIntent);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("10001", "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            builder.setChannelId("10001");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        notificationManager.notify(0 /* Request Code */, builder.build());

    }
}
