package com.example.a3dtictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button [][] buttons = new Button[3][9];
    private int playCount;
    boolean activePlayer;
    //Empty is 0 in button array, X is 1, O is 2.
    int [][] gameState = new int [3][9];

    //You will check for these winning positions on each board, and across boards.
    int [][] winningPositions = {
           //3d win all in the same stack
            {0,1,2}, {3,4,5}, {6,7,8}, //rows
            {0,3,6}, {1,4,7}, {2,5,8}, //columns
            {0,4,8}, {2,4,6} //diagonals
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
           //     WindowManager.LayoutParams.FLAG_FULLSCREEN);

        for (int i = 0; i <buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                String buttonId = "button_" + j + "_grid_" + i;
                int resourceID = getResources().getIdentifier(buttonId, "id", getPackageName());
                buttons[i][j] = (Button) findViewById(resourceID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        playCount = 0;
        activePlayer = true;


    }

    //@Override
    public void onClick(View v) {
        //Log.i("test", "button has been clicked");
        if (!((Button)v).getText().toString().equals(""))
            return;
        String buttonID = v.getResources().getResourceEntryName(v.getId());
        int gameStatePointerBox = Character.getNumericValue(buttonID.charAt(7));
        //14 could be replaced with length - 1
        int gameStatePointerGrid = Character.getNumericValue(buttonID.charAt(14));

        if(activePlayer) {
            ((Button)v).setText("X");
            ((Button)v).setTextColor(Color.parseColor("#ffffff"));
            gameState[gameStatePointerGrid][gameStatePointerBox] = 1;
        } else {
            ((Button)v).setText("O");
            ((Button)v).setTextColor(Color.parseColor("#ffffff"));
            gameState[gameStatePointerGrid][gameStatePointerBox] = 2;
        }
        playCount++;
        if(checkWinner()) {
            if(activePlayer) {
                Toast.makeText(this, "P1 wins", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "P2 wins", Toast.LENGTH_LONG).show();
            }
            playAgain();
        } else if (playCount == 27) {
                Toast.makeText(this, "There is no winner!", Toast.LENGTH_LONG).show();
                playAgain();
        } else {
            activePlayer = !activePlayer;
        }

    }

    public boolean checkWinner () {
        //check each grid for a win

        for (int i = 0; i < 3; i++) {
            //within the grid, check each winning position from winningPositions
            for (int [] win : winningPositions) {
                if (gameState[i][win[0]] != 0) {
                    if(gameState[i][win[0]] == gameState[i][win[1]]
                            && gameState[i][win[1]] == gameState[i][win[2]]) {
                        return true;
                    }
                }
            }
        }
        //check for complex 3d solutions
        for (int [] win : winningPositions) {
            if (gameState[0][win[0]] != 0
                    && gameState[0][win[0]] == gameState[1][win[1]]
                    && gameState[1][win[1]] == gameState[2][win[2]]) {
                return true;
            }
        }

        //Check for simple "stacked" 3d solutions
       for (int i = 0; i < 9; i++)  {
            int temp;
            if (gameState[0][i] == 0) continue;
            else {
                temp = gameState[0][i];
            }
            if (!(gameState[1][i] == temp)) continue;
            if (gameState[2][i] == temp) {
                return true;
            }
        }

        return false;
    }

    public void playAgain () {
        playCount = 0;
        activePlayer = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                gameState[i][j] = 0;
                buttons[i][j].setText("");
            }
        }
    }
}
