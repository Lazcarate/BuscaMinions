package com.example.lazcarate.minions2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class JuegoActivity extends Activity {

    private int filas = 8;
    private int columnas = 8;

    private int minions = 10;

    int total;
    int numbanderas = 0;

    Celdas celda [][];
    TextView txtCelda [][];

    boolean tieneMinion [][];//Matriz pone true donde hay minion
    boolean tieneBenderas [][];
    int surrounding[][] = new int[columnas][filas];//Matriz que almacena enteros

    //boolean isHappy = true;
    boolean finPartida = false;
    //boolean esBandera = false;
    boolean isStarted = false;

    int contador = 0;
    int width = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        total = columnas * filas - minions;
        disponeJuego();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        creaTablero();

    }
    public void disponeJuego(){
        ponerMinions();
        rodearMinions();
    }
    /*
	 * Populate the number board based on mine position. Uses CheckSurround().
	 */
    public void rodearMinions() {

        for (int i = 0; i < columnas; i++) {
            for (int j = 0; j < filas; j++) {

                if (tieneMinion[i][j])
                    surrounding[i][j] = 9;
                else
                    surrounding[i][j] = poneNumeros(j, i);
            }
        }
    }
    /*
	 * Check the immediate surroundings (8 or less grids) at a position [c,r]
	 * and return the number of mines surrounding it. INPUT: Row position,
	 * Column position OUTPUT: Number of mines surrounding the position
	 */
    public int poneNumeros(int r, int c) {
        int count = 0;

        if (r - 1 >= 0) {
            if (tieneMinion[c][r - 1])
                count++;

            if (c - 1 >= 0)
                if (tieneMinion[c - 1][r - 1])
                    count++;
            if (c + 1 < columnas)
                if (tieneMinion[c + 1][r - 1])
                    count++;
        }

        if (r + 1 < filas) {
            if (tieneMinion[c][r + 1])
                count++;

            if (c - 1 >= 0)
                if (tieneMinion[c - 1][r + 1])
                    count++;
            if (c + 1 < columnas)
                if (tieneMinion[c + 1][r + 1])
                    count++;
        }

        if (c - 1 >= 0)
            if (tieneMinion[c - 1][r])
                count++;

        if (c + 1 < columnas)
            if (tieneMinion[c + 1][r])
                count++;

        return count;
    }

    /*
	 * Creates and adds mines to the board.
	 */
    public void ponerMinions(){//Podriamos meter el numero de minions por parametro

        int i = 0;
        int colAleatoria;
        int filaAleatoria;
        Random rand = new Random();

        tieneMinion = new boolean[columnas][filas];
        tieneBenderas = new boolean[columnas][filas];

        contador = 0;

        while (i < minions) {
            colAleatoria = rand.nextInt(columnas);
            filaAleatoria = rand.nextInt(filas);

            if (!tieneMinion[colAleatoria][filaAleatoria])
                tieneMinion[colAleatoria][filaAleatoria] = true;
            else
                continue;
            i++;
        }
    }
    public void creaTablero(){

        Celdas b;
        TextView tv;

        int xmargen = 0;
        int ymargen = 0;
        int anchoaltoCelda = width / columnas;

        RelativeLayout gv = (RelativeLayout) findViewById(R.id.grid);
        RelativeLayout.LayoutParams rel_b;

        celda = new Celdas[columnas][filas];
        txtCelda = new TextView[columnas][filas];

        for (int i = 0; i < columnas; i++) {
            for (int j = 0; j < filas; j++) {

                rel_b = new RelativeLayout.LayoutParams(anchoaltoCelda, anchoaltoCelda);
                rel_b.leftMargin = xmargen;
                rel_b.topMargin = ymargen;

                b = new Celdas(this);
                b.setId(contador);
                b.setLayoutParams(rel_b);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Celdas b = (Celdas) v;
                        b.setClicked(true);

                        if (finPartida || tieneBenderas[b.getPosY()][b.getPosX()])
                            return;

                        isStarted = true;

                        if (tieneMinion[b.getPosY()][b.getPosX()]) {
                            finPartida = true;
                            /*System.out.println("Boom - y, x: " + b.getyPos()
                                    + b.getxPos());
                            ((Button) findViewById(R.id.face))
                                    .setBackgroundResource(R.drawable.sad);*/
                            return;
                        }
                        // Comprobamos si la tecla pulsada es 0, si es así llama al método que recorre el tablero
                        //poniendo el numero en cada celda que corresponde al numero de minions alrededor.
                        if (surrounding[b.getPosY()][b.getPosX()] == 0) {
                            ponerCeros(b.getPosY(), b.getPosX());
                        }
                        //Hecho lo anterior hacemos invisibles las celdas y visible el texto.
                        celda[b.getPosY()][b.getPosX()]
                                .setVisibility(View.INVISIBLE);
                        txtCelda[b.getPosY()][b.getPosX()]
                                .setVisibility(View.VISIBLE);
                        contador++;//Aumenta el numero de tecla pulsada

                        if (contador == total) {//Cuando es igual al total el jugador gana
                            // Gana
                            /*((Button) findViewById(R.id.face))
                                    .setBackgroundResource(R.drawable.win);*/
                            finPartida = true;
                            // attempt highscore adding
                            /*System.out.print("Time was " + time);
                            popUpScreen(addScoreFromGame(time));*/

                        }

                    }
                });

                b.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Celdas b = (Celdas) v;
                        tieneBenderas[b.getPosY()][b.getPosX()] = !tieneBenderas[b
                                .getPosY()][b.getPosX()];
                        if (tieneBenderas[b.getPosY()][b.getPosX()]) {
                            b.setText("F");
                            numbanderas++;
                        } else {
                            b.setText("");
                            numbanderas--;
                        }
                        /*((TextView) findViewById(R.id.mineView))
                                .setText(Integer.toString(mines - numflags));*/

                        return true;
                    }
                });

                b.setGravity(Gravity.CENTER);

                gv.addView(b);

                tv = new TextView(this);
                tv.setLayoutParams(rel_b);
                tv.setVisibility(View.INVISIBLE);

                if (surrounding[i][j] == 0)
                    tv.setText("");
                else
                    tv.setText(Integer.toString(surrounding[i][j]));
                tv.setGravity(Gravity.CENTER);
                gv.addView(tv);

                b.setPosX(j);
                b.setPosY(i);

                celda[i][j] = b;
                txtCelda[i][j] = tv;

                xmargen += anchoaltoCelda;
            }
            ymargen += anchoaltoCelda;
            xmargen = 0;
        }
    }
    public void ponerCeros(int r, int c) { // NOTE: R AND C ARE SWITCHED
        if (r == 0) { // top row
            if (c == 0) { // left top side
                clickIfUnclicked(r, c + 1);
                clickIfUnclicked(r + 1, c);
                clickIfUnclicked(r + 1, c + 1);
            } else if (c == columnas - 1) { // right top side
                clickIfUnclicked(r, c - 1);
                clickIfUnclicked(r + 1, c - 1);
                clickIfUnclicked(r + 1, c);
            } else { // middle top
                clickIfUnclicked(r, c - 1);
                clickIfUnclicked(r, c + 1);
                clickIfUnclicked(r + 1, c - 1);
                clickIfUnclicked(r + 1, c);
                clickIfUnclicked(r + 1, c + 1);
            }
        }

        else if (r == filas - 1) { // bottom row
            if (c == 0) { // left bottom side
                clickIfUnclicked(r - 1, c);
                clickIfUnclicked(r - 1, c + 1);
                clickIfUnclicked(r, c + 1);
            } else if (c == columnas - 1) { // right bottom side
                clickIfUnclicked(r - 1, c - 1);
                clickIfUnclicked(r - 1, c);
                clickIfUnclicked(r, c - 1);
            } else { // middle bottom
                clickIfUnclicked(r - 1, c - 1);
                clickIfUnclicked(r - 1, c);
                clickIfUnclicked(r - 1, c + 1);
                clickIfUnclicked(r, c - 1);
                clickIfUnclicked(r, c + 1);

            }
        }

        else if (c == 0) { // left middle side
            clickIfUnclicked(r - 1, c);
            clickIfUnclicked(r - 1, c + 1);
            clickIfUnclicked(r, c + 1);
            clickIfUnclicked(r + 1, c);
            clickIfUnclicked(r + 1, c + 1);
        } else if (c == columnas - 1) { // right middle side
            clickIfUnclicked(r - 1, c - 1);
            clickIfUnclicked(r - 1, c);
            clickIfUnclicked(r, c - 1);
            clickIfUnclicked(r + 1, c - 1);
            clickIfUnclicked(r + 1, c);
        }

        else { // all other cases
            clickIfUnclicked(r - 1, c - 1);
            clickIfUnclicked(r - 1, c);
            clickIfUnclicked(r - 1, c + 1);
            clickIfUnclicked(r, c - 1);
            clickIfUnclicked(r, c + 1);
            clickIfUnclicked(r + 1, c - 1);
            clickIfUnclicked(r + 1, c);
            clickIfUnclicked(r + 1, c + 1);
        }
    }
    public void clickIfUnclicked(int r, int c) {
        if (!celda[r][c].getClicked())
            celda[r][c].performClick();//Activa el evento click del boton a traves de codigo
    }



    }



