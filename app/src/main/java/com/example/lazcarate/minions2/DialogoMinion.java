package com.example.lazcarate.minions2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by lazcarate on 16/12/15.
 */
public class DialogoMinion extends DialogFragment{

    onEleccionMinionListener minionElegido;//
    String [] nombreMinion;
    int [] iconosMinions = {R.drawable.minion_happy, R.drawable.minion_read, R.drawable.minion_dance, R.drawable.minion_chica,
            R.drawable.minion2, R.drawable.minion};



    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        minionElegido = (onEleccionMinionListener)activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        nombreMinion = getActivity().getResources().getStringArray(R.array.tipo_minion);// Ref al String-Array
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//Objeto constructor del dialogo

        //Obtenemos la vista del layout que representa el spinner inflandolo.
        View minionsView = getActivity().getLayoutInflater().inflate(R.layout.spinnerminions_layout, null);

        //Referencia al spinner obtenido del inflado del layout
        final Spinner spinerMinions = (Spinner) minionsView.findViewById(R.id.spinnerMinion);

        //Creamos el adaptador con sus parametros
        AdaptedTypoMinion adataminion = new AdaptedTypoMinion(getActivity(), R.layout.minion_layout, nombreMinion);

        spinerMinions.setAdapter(adataminion);//Lo añadimos al spinner
        builder.setView(minionsView);//Añadimos la vista al constructor del dialogo
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                minionElegido.onEleccionMinion(spinerMinions.getSelectedItemPosition());
            }
        });
        return builder.create();
    }
    public class AdaptedTypoMinion extends ArrayAdapter<String> {


        public AdaptedTypoMinion(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View miFila = inflater.inflate(R.layout.minion_layout, parent, false);

            TextView nameMinion = (TextView) miFila.findViewById(R.id.txtminion);
            ImageView iconMinion = (ImageView) miFila.findViewById(R.id.iconominion);

            nameMinion.setText(nombreMinion[position]);
            iconMinion.setImageResource(iconosMinions[position]);
            return miFila;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent);
        }
    }
    public interface onEleccionMinionListener{
        void onEleccionMinion(int posion);
    }
}

