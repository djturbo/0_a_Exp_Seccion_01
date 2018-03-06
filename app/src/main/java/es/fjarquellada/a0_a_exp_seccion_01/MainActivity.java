package es.fjarquellada.a0_a_exp_seccion_01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnShow;
    private Button btnGoCall;

    private static final String GREETING = "Hola que ase tú!!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Cargar icono en el action bar */
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_rabbit);

        this.btnShow = (Button)findViewById(R.id.btnShow);
        this.btnShow.setOnClickListener(this);

        this.btnGoCall = (Button) findViewById(R.id.btnGoCall);
        this.btnGoCall.setOnClickListener(this);
    }


    private void goToSecondActivity(){
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("greeting", GREETING);
        intent.putExtras(bundle);

        this.startActivity(intent);

    }

    private void goToThirdActivity(){
        Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if( v.getId() == this.btnShow.getId()){
            Toast.makeText(this, "Me has clicado", Toast.LENGTH_LONG).show();
            this.goToSecondActivity();
        }else if(v.getId() == this.btnGoCall.getId()){
            this.goToThirdActivity();
        }
    }
}
