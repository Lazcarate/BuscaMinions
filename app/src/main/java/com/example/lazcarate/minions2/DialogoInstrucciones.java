package com.example.lazcarate.minions2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by lazcarate on 16/12/15.
 */
public class DialogoInstrucciones extends DialogFragment{

    /*
    Creamos un sencillo dialogo para mostrar las instrucciones
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.instruc);//Titulo
        builder.setMessage(R.string.Instrucciones);//Mensaje
        builder.setPositiveButton(R.string.ok, null);//Un boton que valida la accion pero no hace nada
        return builder.create();// Se crea
    }

}
