package es.fjarquellada.a0_a_exp_seccion_01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnShow;
    private static final String GREETING = "Hola que ase tú!!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnShow = (Button)findViewById(R.id.btnShow);
        this.btnShow.setOnClickListener(this);
    }


    private void goToSecondActivity(){
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("greeting", GREETING);
        intent.putExtras(bundle);

        this.startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        if( v.getId() == this.btnShow.getId()){
            Toast.makeText(this, "Me has clicado", Toast.LENGTH_LONG).show();
            this.goToSecondActivity();
        }
    }
}
