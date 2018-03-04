package es.fjarquellada.a0_a_exp_seccion_01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private TextView txtViewGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        this.txtViewGreeting = (TextView)findViewById(R.id.txtViewGreeting);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            this.txtViewGreeting.setText(bundle.getString("greeting"));
        }
    }
}
