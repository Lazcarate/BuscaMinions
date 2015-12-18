package com.example.lazcarate.minions2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by lazcarate on 16/12/15.
 */
public class DialogoConfiguracion extends DialogFragment{

    onCambioConfiguracionListener actividad;
/*
Unimos el fragmento a la actividad
 */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        actividad = (onCambioConfiguracionListener) activity;
    }
/*
Creamos el Dialogo
 */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        JuegoActivity actMinions = (JuegoActivity) getActivity();//Ref a la actividad principal
        builder.setTitle(R.string.nivelJuego);
        //Eleccion de muchas opciones que se pasan como array, pero solo se puede elgir una.
        builder.setSingleChoiceItems(R.array.nivel_Juego, actMinions.nivelJuego, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                actividad.onCambioConfiguracion(which);
            }
        });
        builder.setPositiveButton(R.string.volver, null);
        return builder.create();
    }
    /*
    Interfaz para la comunicacion entre el fragmento y la actividad
     */
    public interface onCambioConfiguracionListener{

        void onCambioConfiguracion(int position);
    }
}
