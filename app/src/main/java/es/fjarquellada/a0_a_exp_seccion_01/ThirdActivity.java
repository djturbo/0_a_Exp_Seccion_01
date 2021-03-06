package es.fjarquellada.a0_a_exp_seccion_01;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ThirdActivity";
    private static final int PHONE_CALL_CODE = 100;
    private static final int PICTURE_FROM_CAMERA = 50;

    private EditText editTextPhone;
    private EditText editTextWeb;
    private EditText editTextMail;

    private ImageButton imageButtonPhone;
    private ImageButton imageButtonWeb;
    private ImageButton imageButtonCamera;
    private ImageButton imageButtonMail;

    private Button btnContacts;
    private Button btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        /** Mostrar flecha volver */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.editTextPhone  = findViewById(R.id.editTextPhone);
        this.editTextWeb    = findViewById(R.id.editTextWeb);
        this.editTextMail   = findViewById(R.id.editTextMail);

        this.imageButtonCamera = (ImageButton) findViewById(R.id.imageButtonCamera);
        this.imageButtonPhone = (ImageButton) findViewById(R.id.imageButtonPhone);
        this.imageButtonWeb = (ImageButton) findViewById(R.id.imageButtonWeb);
        this.imageButtonMail = (ImageButton) findViewById(R.id.imageButtonMail);

        this.imageButtonPhone.setOnClickListener(this);
        this.imageButtonCamera.setOnClickListener(this);
        this.imageButtonWeb.setOnClickListener(this);
        this.imageButtonMail.setOnClickListener(this);

        this.btnContacts = (Button) findViewById(R.id.btnShowContacts);
        this.btnCall = (Button) findViewById(R.id.btnPhoneCall);


        this.btnContacts.setOnClickListener(this);
        this.btnCall.setOnClickListener(this);
    }

    private void onClickPhoneButton() {
        if (!"".equals(this.editTextPhone.getText().toString())) {
            String phone = this.editTextPhone.getText().toString();
            /* Se comprueba la versión del SDK de Android que se está ejecutando */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Comprobar si ha aceptado o nunca se le ha preguntado
                if (checkPermission(Manifest.permission.CALL_PHONE)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                } else {
                    // Si ha denegado, o es la primera vez que se le pregunta
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                        // no se le ha preguntado
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                    } else {
                        // Ha denegado
                        Toast.makeText(this, "Usted denegó los permisos de llamada", Toast.LENGTH_LONG).show();
                        Intent intentSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intentSettings.addCategory(Intent.CATEGORY_DEFAULT);
                        intentSettings.setData(Uri.parse("package:" + getPackageName()));
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

    private void onClickWebButton() {
        String url = this.editTextWeb.getText().toString();

        if (!"".equals(url)) {
            // Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+url));
            Intent intentWeb = new Intent();
            intentWeb.setAction(Intent.ACTION_VIEW);
            intentWeb.setData(Uri.parse("http://" + url));

            startActivity(intentWeb);
        }
    }

    private void openContactIntentView() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people"));
        startActivity(intent);
    }

    private void sendQuickMail() {
        String mail = this.editTextMail.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mail));
        startActivity(intent);
    }

    private void sendCompleteMail() {
        String mail = this.editTextMail.getText().toString();


        try
        {
            Intent gmailIntent = new Intent(Intent.ACTION_SEND);
            gmailIntent.setType("text/html");

            final PackageManager pm = getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(gmailIntent, 0);
            String gmailActivityClass = null;

            for (final ResolveInfo info : matches){
                if (info.activityInfo.packageName.equals("com.google.android.gm")){
                    gmailActivityClass = info.activityInfo.name;

                    if (gmailActivityClass != null && !gmailActivityClass.isEmpty()) {
                        break;
                    }
                }
            }

            gmailIntent.setClassName("com.google.android.gm", gmailActivityClass);
            gmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "yourmail@gmail.com" });
            gmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            gmailIntent.putExtra(Intent.EXTRA_CC, "cc@gmail.com"); // if necessary
            gmailIntent.putExtra(Intent.EXTRA_TEXT, "Email message");
            gmailIntent.setData(Uri.parse("yourmail@gmail.com"));
            startActivity(gmailIntent);
            /* O se puede quitar el setClassName y usrar lo siguiente */
            // startActivity(Intent.createChooser(gmailIntent, "Seleccione un cliente de correo");

        }catch(Exception e){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_EMAIL, new String[] { "yourmail@gmail.com" });
            i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            i.putExtra(Intent.EXTRA_CC, "cc@gmail.com"); // if necessary
            i.putExtra(Intent.EXTRA_TEXT, "Email message");
            i.setType("plain/text");
            startActivity(i);
        }


    }

    /**
     * No requiere permisos para esta forma de llamar, usa la app nativa de android, por tanto no pide permisos.
     */
    private void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:555555555"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
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

    private void openCamera(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, PICTURE_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case PICTURE_FROM_CAMERA:
                if(resultCode == Activity.RESULT_OK) {
                    String result = data.toUri(0);
                    Toast.makeText(this, "Foto tomada: "+result, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

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
            this.openCamera();
        }else if(this.imageButtonPhone.getId() == id){
            this.onClickPhoneButton();
        }else if(this.imageButtonWeb.getId() == id) {
            this.onClickWebButton();
        }else if(imageButtonMail.getId() == id){
            this.sendCompleteMail();
        }else if(this.btnContacts.getId() == id){
            this.openContactIntentView();
        }else if(this.btnCall.getId() == id){
            this.makePhoneCall();
        }

    }
}
