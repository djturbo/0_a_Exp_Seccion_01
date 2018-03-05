package es.fjarquellada.a0_a_exp_seccion_01;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ThirdActivity";
    private static final int PHONE_CALL_CODE = 100;

    private EditText editTextPhone;
    private EditText editTextWeb;

    private ImageButton imageButtonPhone;
    private ImageButton imageButtonWeb;
    private ImageButton imageButtonCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        this.editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        this.editTextWeb = (EditText) findViewById(R.id.editTextWeb);

        this.imageButtonCamera = (ImageButton) findViewById(R.id.imageButtonCamera);
        this.imageButtonPhone = (ImageButton) findViewById(R.id.imageButtonPhone);
        this.imageButtonWeb = (ImageButton) findViewById(R.id.imageButtonWeb);

        this.imageButtonPhone.setOnClickListener(this);
        this.imageButtonCamera.setOnClickListener(this);
        this.imageButtonWeb.setOnClickListener(this);
    }

    private void onClickPhoneButton() {
        if (!"".equals(this.editTextPhone.getText().toString())) {
            String phone = this.editTextPhone.getText().toString();
            /* Se comprueba la versión del SDK de Android que se está ejecutando */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Comprobar si ha aceptado o nunca se le ha preguntado
                if(checkPermission(Manifest.permission.CALL_PHONE)){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
                    startActivity(intent);
                }else{
                    // Si ha denegado, o es la primera vez que se le pregunta
                    if(!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                        // no se le ha preguntado
                        requestPermissions(new String[]{ Manifest.permission.CALL_PHONE }, PHONE_CALL_CODE);
                    }else{
                        // Ha denegado
                        Toast.makeText(this, "Usted denegó los permisos de llamada", Toast.LENGTH_LONG).show();
                        Intent intentSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intentSettings.addCategory(Intent.CATEGORY_DEFAULT);
                        intentSettings.setData(Uri.parse("package:"+ getPackageName()));
                        intentSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentSettings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intentSettings.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                        startActivity(intentSettings);

                    }
                }

                // this.doCallWithNewerVersionsOfPhones(phone);
            } else {
                this.doCallWithOlderVersionsOfPhones(phone);
            }
        }
    }

    private void onClickWebButton(){
        String url = this.editTextWeb.getText().toString();

        if(!"".equals(url)){
            // Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+url));
            Intent intentWeb = new Intent();
            intentWeb.setAction(Intent.ACTION_VIEW);
            intentWeb.setData(Uri.parse("http://"+url));

            startActivity(intentWeb);
        }
    }


    /**
     * COMPRUEBA SI EL PERMISO ESTÁ PRESENTE EN EL MANIFEST
     * @param permission
     * @return
     */
    private boolean checkPermission(String permission) {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Hace una llamada con versiones del SDK menores que la 23
     * @param phone
     */
    private void doCallWithOlderVersionsOfPhones(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            this.startActivity(intent);
        } else {
            Toast.makeText(ThirdActivity.this, "Usted no aprobó este tipo de acción", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Realiza llamadas con versiones de Android superiores a la 23
     * @param phone
     */
    private void doCallWithNewerVersionsOfPhones(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case PHONE_CALL_CODE:
                String perm = permissions[0];
                if(perm.equals(Manifest.permission.CALL_PHONE)){
                    /** Comprobar que haya sido aceptada o denegada la petición del permiso */
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        // Permiso concedido
                        String phone = this.editTextPhone.getText().toString();
                        this.doCallWithNewerVersionsOfPhones(phone);
                    }else{
                        // Permiso denegado
                    }
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if(this.imageButtonCamera.getId() == id){

        }else if(this.imageButtonPhone.getId() == id){
            onClickPhoneButton();
        }else if(this.imageButtonWeb.getId() == id) {
            this.onClickWebButton();
        }

    }
}
