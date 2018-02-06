package com.parse.starter;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class gameActivity extends AppCompatActivity {

    public void back(View view){
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.crct) {

            Intent intent = new Intent(getApplicationContext(), UserListActivity.class);

            startActivity(intent);

            return true;

        }

        return false;

    }


    int activeState=0;
    boolean gameIsActive=true;

    int[] gameState={2,2,2,2,2,2,2,2,2};
    int[][] winningPosition={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void dropIn(View view){
        ImageView counter= (ImageView) view;
        int tappedCounter=Integer.parseInt(counter.getTag().toString());

        if(gameState[tappedCounter]==2 && gameIsActive){

            gameState[tappedCounter]=activeState;
            counter.setTranslationY(-1000f);
            if(activeState==0) {
                counter.setImageResource(R.drawable.yellow);
                activeState=1;
            }
            else{
                counter.setImageResource(R.drawable.red);
                activeState=0;
            }
            counter.animate().setDuration(300).translationYBy(1000f);

            for(int[] win: winningPosition)
            {
                if(gameState[win[0]]==gameState[win[1]]&& gameState[win[1]]==gameState[win[2]] && gameState[win[0]]!=2)
                {
                    gameIsActive=false;
                    String winner="Red";
                    if (gameState[win[0]]==0){
                        winner="yellow";
                    }
                    TextView winnerMessage=(TextView)findViewById(R.id.winnerMessage);
                    winnerMessage.setText(winner+ " has won");
                    LinearLayout Layout=(LinearLayout)findViewById(R.id.playAgainLayout);
                    Layout.setVisibility(View.VISIBLE);
                }else{
                    boolean gameIsOver=true;
                    for(int counterState:gameState){
                        if(counterState==2) gameIsOver=false;
                    }
                    if(gameIsOver){
                        TextView winnerMessage=(TextView)findViewById(R.id.winnerMessage);
                        winnerMessage.setText("Ashish says Draw");
                        LinearLayout Layout=(LinearLayout)findViewById(R.id.playAgainLayout);
                        Layout.setVisibility(View.VISIBLE);
                    }

                }
            }
        }}
    public void playAgain(View view){
        gameIsActive=true;
        LinearLayout Layout=(LinearLayout)findViewById(R.id.playAgainLayout);
        Layout.setVisibility(View.INVISIBLE);
        activeState=0;

        for (int i=0;i<gameState.length;i++){
            gameState[i]=2;
        }
        GridLayout grid=(GridLayout)findViewById(R.id.gridLayout);
        for (int i=0;i<grid.getChildCount();i++)
        {
            ((ImageView) grid.getChildAt(i)).setImageResource(0);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setTitle("3*3 Game");
    }
}
