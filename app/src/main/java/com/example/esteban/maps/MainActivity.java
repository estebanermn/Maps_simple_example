package com.example.esteban.maps;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public Button btnIr;
    private final int GPS_PERMISO = 1;
    private boolean tienepermiso = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnIr = (Button) findViewById(R.id.btnIr);

        int currentapiVersion = Build.VERSION.SDK_INT;
        //Validar que la versión sea igual o superio a 23 (M)
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            validarUbicacion();
        } else {
            tienepermiso = true;
        }

        btnIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tienepermiso) {
                    Intent intent = new Intent().setClass(getBaseContext(), MapsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "NO TIENE PERMISO PARA ACCEDER A SU UBICACION",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void validarUbicacion() {
        //Obtiene el estado actual del permiso.
        final int iGPS = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        //Comprobamos si el usuario otorgó el permiso
        if (iGPS != PackageManager.PERMISSION_GRANTED) {
            //Solicitando el permiso a gps del dispositivo

            String[] permiso = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            };

            //GPS_PERMISO es una constante global declarada en
            //la parte superior
            requestPermissions(permiso, GPS_PERMISO);
        } else {
            tienepermiso = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case GPS_PERMISO:
                //Validamos si la respuesta no es vacia o si se
                //otorgó otro permiso.
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    tienepermiso = true;
                }else {
                    Toast.makeText(getApplicationContext(), "No podrá usar su GPS" ,
                           Toast.LENGTH_SHORT).show();
                    tienepermiso = false;

                }

        }
    }
}
