package com.example.lazcarate.minions2;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class JuegoActivity extends Activity implements DialogoMinion.onEleccionMinionListener, DialogoConfiguracion.onCambioConfiguracionListener{

    private int filas= 16;//Pongo el maximo de filas y columnas que se crearan.
    private int columnas = 16;
    Menu men;//Referencia al menu.
    int nivelJuego= 0;
    int minions = 60;//Pongo el maximo de minions a crear
    int total;//Nos dirá el numero de celdas que hay que pulsar para ganar.
    Celdas celda [][];//Matriz de objetos Celdas que extienden de Button.
    TextView txtCelda [][];//Matriz de TextViews
    boolean tieneMinion [][];//Matriz marca con true donde hay un minion
    int valorNumericoCelda [][] = new int[columnas][filas];//Matriz que almacena enteros
    boolean finPartida = false;
    boolean enJuego = false;
    int contador = 0;//Cuenta las celdas pulsadas
    int ancho = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        Display display = getWindowManager().getDefaultDisplay();//Obtenemos una referencia a la pantalla
        ancho = display.getWidth();//Obtenemos el ancho de la pantalla
       onCambioConfiguracion(this.nivelJuego);//Llamamos al método que inicia el juego según el nivel
        //yo he inicializado la variable nivelJuego a 0 para que empieze en el nivel Principiante.

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.men = menu;
        return true;
    }
    //Según la opción elegida del menú se llama a su método
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.configurar:
                configuracion();
                return true;
            case R.id.intruciones:
                instrucciones();
                return true;
            case R.id.minion:
                eligeMinion();
                return true;
            case R.id.comenzar:
                inicio();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*Método sobrecargado definido en la interface OnEleccionMinionListener 
    * que sirve de comunicacion entre la actividad y el DialogoMinion, 
    * Parametro de entrada la posición del item pulsado, accedemoa el por la referencia 
    * men del Menu.*/
    @Override
    public void onEleccionMinion(int posicion) {

        switch(posicion) {
            case 0:
                men.getItem(0).setIcon(R.drawable.minion_happy);
                break;
            case 1:
                men.getItem(0).setIcon(R.drawable.minion_read);
                break;
            case 2:
                men.getItem(0).setIcon(R.drawable.minion_dance);
                break;
            case 3:
                men.getItem(0).setIcon(R.drawable.minion_chica);
                break;
            case 4:
                men.getItem(0).setIcon(R.drawable.minion2);
                break;
            case 5:
                men.getItem(0).setIcon(R.drawable.minion);
                break;
        }
    }
    /*Método sobrecargado definido en la interface OnCambioConfiguracionListener
   * que sirve de comunicacion entre la actividad y el DialogoConfiguracion,
   * Parametro de entrada la posición del item pulsado, inicia el método creaTablero
   * con los parametros de entrada segun sea el nivel.*/
    @Override
    public void onCambioConfiguracion(int position) {

        switch (position){
            case 0:
                creaTablero(8,8,10);
                break;
            case 1:
                creaTablero(12,12,30);
                break;
            case 2:
                creaTablero(16,16,60);
                break;
        }
    }
    // Bloque de metodos donde se instancian los dialogos y se muestran

    public void configuracion(){
        DialogoConfiguracion dicon = new DialogoConfiguracion();
        dicon.show(getFragmentManager(), "Dialogo Configuracion");
    }

    public void instrucciones(){
        DialogoInstrucciones diinst = new DialogoInstrucciones();
        diinst.show(getFragmentManager(), "Dialogo Instrucciones");
    }
    public void eligeMinion(){
        DialogoMinion diminion = new DialogoMinion();
        diminion.show(getFragmentManager(), "Dialogo eleccion Minion");
    }
    public void inicio(){
        finPartida=false;
        onCambioConfiguracion(nivelJuego);
    }
    /*
    * Crea y añade los Minions en el tablero. Asigana a 2 variables un valor aleatorio
    * entre nº de col y nº de filas, la variable "i", vigila que se produzcan el nº de minions
    * que entran por parametro gracias al bucle while.
    */
    public void ponerMinions(int columnas, int filas, int minions){

        int i = 0;
        int colAleatoria;
        int filaAleatoria;
        Random rand = new Random();
        tieneMinion = new boolean[columnas][filas];
        contador = 0;

        while (i < minions) {
            colAleatoria = rand.nextInt(columnas);
            filaAleatoria = rand.nextInt(filas);

            if (!tieneMinion[colAleatoria][filaAleatoria])//si no está ya ocupada esta plaza
                tieneMinion[colAleatoria][filaAleatoria] = true;//la ocupamos
            else
                continue;
            i++;
        }
    }
    /*
	 * Pone los numeros en la matriz valorNumericoCelda, en funcion de los minions que hay alrededor.
	  *Llama al metodo poneValorNumericoAlrededorMinion().
	 */
    public void poneValorNumericoEnCelda(int columnas, int filas) {

        for (int i = 0; i < columnas; i++) {
            for (int j = 0; j < filas; j++) {

                if (tieneMinion[i][j])
                    valorNumericoCelda [i][j] = 9;//Marco la posicion de Minion.
                else
                    valorNumericoCelda [i][j] = poneValorNumericoAlrededorMinion(j, i, columnas, filas);
            }
        }
    }
    /*
	 * Chequea las posiciones alrededor de la posicion [c,l], nos devuelve el numero de minionss
	 * que hay alrededor de esta, estara entre 0 y 8.
	 */
    public int poneValorNumericoAlrededorMinion(int l, int c, int columnas, int filas) {
        
        int valorCelda = 0;//Variable que devuelve el método

        if (l - 1 >= 0) {
            if (tieneMinion[c][l - 1])
                valorCelda++;

            if (c - 1 >= 0)
                if (tieneMinion[c - 1][l - 1])
                    valorCelda++;
            if (c + 1 < columnas)
                if (tieneMinion[c + 1][l - 1])
                    valorCelda++;
        }

        if (l + 1 < filas) {
            if (tieneMinion[c][l + 1])
                valorCelda++;

            if (c - 1 >= 0)
                if (tieneMinion[c - 1][l + 1])
                    valorCelda++;
            if (c + 1 < columnas)
                if (tieneMinion[c + 1][l + 1])
                    valorCelda++;
        }

        if (c - 1 >= 0)
            if (tieneMinion[c - 1][l])
                valorCelda++;

        if (c + 1 < columnas)
            if (tieneMinion[c + 1][l])
                valorCelda++;

        return valorCelda;
    }

/*
*Método que crea el tablero de juego. Parametros de entrada columnas, filas y numeros de minions a destapar.
* LLama a los métodos previos que disponen los minions y el valor numerico de cada celda.
*
 */
    public void creaTablero(int columnas, int filas, int minions ){

        this.columnas=columnas;
        this.filas=filas;
        this.minions=minions;
        total = columnas * filas - minions;//Define el valor de total en función de filas y columnas
        
        ponerMinions(columnas,filas,minions);
        poneValorNumericoEnCelda(columnas, filas);

        Celdas b;//Cada una de las celdas de la matriz de celdas
        TextView tv;//Cada uno de los TextViews de la matriz de TextViews


        int margenX = 0;
        int ymargen = 0;
        //Definimos el tamaño de la celda en función del nº que haya.
        int anchoaltoCelda = ancho / columnas;

        RelativeLayout gv = (RelativeLayout) findViewById(R.id.grid);//Ref al Layout contenedor
        gv.removeAllViews();// Refrescamos el contenedor para que no se nos monten al cambiar de nivel
        RelativeLayout.LayoutParams parametrosBoton;//Variable para definir parametros del boton

        // Instanciamos la matriz de celdas y la de TextViews
        celda = new Celdas[columnas][filas];
        txtCelda = new TextView[columnas][filas];
        
        //Recorremos filas y columnas

        for (int i = 0; i < columnas; i++) {
            for (int j = 0; j < filas; j++) {

                parametrosBoton = new RelativeLayout.LayoutParams(anchoaltoCelda, anchoaltoCelda);
                parametrosBoton.leftMargin = margenX;
                parametrosBoton.topMargin = ymargen;

                //Contruimos cada boton de la matriz Celdas.

                b = new Celdas(this);
                celda[i][j] = b;
                b.setPosX(j);
                b.setPosY(i);
                b.setId(contador);
                b.setLayoutParams(parametrosBoton);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Celdas b = (Celdas) v;
                        b.setClicked(true);

                        if (finPartida)
                            return;

                        enJuego = true;

                        if (tieneMinion[b.getPosY()][b.getPosX()]) {
                            b.setBackgroundResource(R.drawable.minionfallido);//Ponemos la imagen del minion bocabajo.
                            Toast.makeText(JuegoActivity.this, "!!HAS PERDIDO¡¡, Habia un Minion", Toast.LENGTH_LONG).show();
                            finPartida = true;
                            return;
                        }
                        //Comprobamos si la tecla pulsada es 0, si es así llama al método que recorre el tablero
                        //destapando el numero en cada celda que corresponde al numero de minions alrededor.
                        if (valorNumericoCelda[b.getPosY()][b.getPosX()] == 0) {
                            destapaCeldas(b.getPosY(), b.getPosX());

                        }
                        //Hecho lo anterior hacemos invisibles las celdas y visible el texto.
                        celda[b.getPosY()][b.getPosX()]
                                .setVisibility(View.INVISIBLE);
                        txtCelda[b.getPosY()][b.getPosX()]
                                .setVisibility(View.VISIBLE);
                        contador++;//Aumenta el numero de tecla destapadas

                        if (contador == total) {//Cuando el contador es igual al total el jugador gana.
                            //Mensaje de victoria
                            Toast.makeText(JuegoActivity.this, "!!ENHORABUENA¡¡ , Ganaste la partida", Toast.LENGTH_SHORT).show();
                            finPartida = true;
                        }
                    }
                });
                b.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override public boolean onLongClick(View v) {
                        Celdas b = (Celdas) v;

                        if (tieneMinion[b.getPosY()][b.getPosX()]) {
                            b.setBackground(men.getItem(0).getIcon());//Aparece la imagen del minion seleccionado en el menu
                        } else {
                            //Mensaje de Juego perdido
                            Toast.makeText(JuegoActivity.this, "!!HAS PERDIDO¡¡, No habia un Minion", Toast.LENGTH_LONG).show();
                            finPartida = true;
                        }
                        return true;
                    }

                });

                b.setGravity(Gravity.CENTER);//Texto del boton centrado
                gv.addView(b);//Añadimos los botones al contenedor RelativeLayout

                tv = new TextView(this);
                txtCelda[i][j] = tv;
                tv.setLayoutParams(parametrosBoton);
                tv.setVisibility(View.INVISIBLE);
                if (valorNumericoCelda[b.getPosY()][b.getPosX()] == 0) {
                    tv.setText("");//No pongo nada, queda mas estético que ver el 0.
                }else {
                    tv.setText(Integer.toString(valorNumericoCelda[i][j]));//Mostramos en el textView el valor numerico de la celda
                }
                tv.setGravity(Gravity.CENTER);//Centramos el texto
                gv.addView(tv);//Añadimos el textView al Layout contenedor
                margenX += anchoaltoCelda;
            }
            ymargen += anchoaltoCelda;
            margenX = 0;
        }
    }
    /*
    Este método es llamado cuando es pulsada una celda cuyo valor numérico es 0. En este caso recoge sus coordenadas que le entran
    como parametros y pulsa todas las teclas que están a su alrededor, destapandolas.
     */
    public void destapaCeldas(int f, int c) {
        if (f == 0) { // fila de arriba
            if (c == 0) { // columna de la izquierda
                pulsaBoton(f, c + 1);
                pulsaBoton(f + 1, c);
                pulsaBoton(f + 1, c + 1);
            } else if (c == columnas - 1) { // si estamos arriba a la derecha
                pulsaBoton(f, c - 1);
                pulsaBoton(f + 1, c - 1);
                pulsaBoton(f + 1, c);
            } else { // arriba en la mitad
                pulsaBoton(f, c - 1);
                pulsaBoton(f, c + 1);
                pulsaBoton(f + 1, c - 1);
                pulsaBoton(f + 1, c);
                pulsaBoton(f + 1, c + 1);
            }
        }
        else if (f == filas - 1) { // fila de abajo
            if (c == 0) { // abajo a la izquierda
                pulsaBoton(f - 1, c);
                pulsaBoton(f - 1, c + 1);
                pulsaBoton(f, c + 1);
            } else if (c == columnas - 1) { // lado arriba derecha
                pulsaBoton(f - 1, c - 1);
                pulsaBoton(f - 1, c);
                pulsaBoton(f, c - 1);
            } else { // arriba a la mitad
                pulsaBoton(f - 1, c - 1);
                pulsaBoton(f - 1, c);
                pulsaBoton(f - 1, c + 1);
                pulsaBoton(f, c - 1);
                pulsaBoton(f, c + 1);
            }
        }
        else if (c == 0) { // lado izquierdo al medio
            pulsaBoton(f - 1, c);
            pulsaBoton(f - 1, c + 1);
            pulsaBoton(f, c + 1);
            pulsaBoton(f + 1, c);
            pulsaBoton(f + 1, c + 1);
        } else if (c == columnas - 1) { // lado derecho al medio
            pulsaBoton(f - 1, c - 1);
            pulsaBoton(f - 1, c);
            pulsaBoton(f, c - 1);
            pulsaBoton(f + 1, c - 1);
            pulsaBoton(f + 1, c);
        }
        else { // resto
            pulsaBoton(f - 1, c - 1);
            pulsaBoton(f - 1, c + 1);
            pulsaBoton(f, c - 1);
            pulsaBoton(f, c + 1);
            pulsaBoton(f + 1, c - 1);
            pulsaBoton(f + 1, c);
            pulsaBoton(f + 1, c + 1);
        }
    }//Método que pulsa el boton en las coordenadas que entran por parametro
    public void pulsaBoton(int f, int c) {
        if (!celda[f][c].getClicked())
            celda[f][c].performClick();//Activa el evento click del boton.
    }
}



