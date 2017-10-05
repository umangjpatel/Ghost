/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private SimpleDictionary simpleDictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    private String computerWordNew = null;
    private String wordFragment = null;
    private String computerWord = null;
    private String yourWord = null;
    TextView ghostText;
    TextView gameStatus;
    Button challengeButton;
    Button restartButton;
    int whoEndFirst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        // Takes the words.txt file to the word reader method in SimpleDictionary class
        try {
            InputStream dictionaryReader = assetManager.open("words.txt");
            simpleDictionary = new SimpleDictionary(dictionaryReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        challengeButton = (Button) findViewById(R.id.challenge_button);
        restartButton = (Button) findViewById(R.id.restart_button);
        //challengeButton.setOnClickListener(this);
        //restartButton.setOnClickListener(GhostActivity.this);

        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        ghostText = (TextView) findViewById(R.id.ghostText);
        wordFragment = "";
        ghostText.setText(wordFragment);
        gameStatus = (TextView) findViewById(R.id.gameStatus);
        whoEndFirst = userTurn ? 1 : 0;
        if (userTurn) {
            gameStatus.setText(USER_TURN);
        } else {
            gameStatus.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        gameStatus.setText(COMPUTER_TURN);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                computerWord = simpleDictionary.getAnyWordStartingWith(wordFragment);
                if (computerWord == "noWord") {
                    Toast.makeText(GhostActivity.this, "Computer Wins! No such Word", Toast.LENGTH_SHORT).show();
                    onStart(null);
                } else if (computerWord == "sameAsPrefix") {
                    Toast.makeText(GhostActivity.this, "Computer Wins! You ended the word", Toast.LENGTH_SHORT).show();
                    onStart(null);
                } else {
                    if (wordFragment.equals("")) {
                        wordFragment = computerWord.substring(0, 1);
                    } else {
                        wordFragment = computerWord.substring(0, wordFragment.length() + 1);
                    }
                    ghostText.setText(wordFragment);
                    Toast.makeText(GhostActivity.this, USER_TURN, Toast.LENGTH_SHORT).show();
                }
                userTurn = true;
                gameStatus.setText(USER_TURN);
            }
        }, 1000);
    }

    /**
     * Handler for user key presses.
     *
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        //TODO - Your code goes here

        char pressedKey = (char) event.getUnicodeChar();
        pressedKey = Character.toLowerCase(pressedKey);

        if (pressedKey >= 'a' && pressedKey <= 'z') {

            wordFragment = ghostText.getText().toString();
            wordFragment += pressedKey;
            ghostText.setText(wordFragment);
            computerTurn();

        } else {
            Toast.makeText(this, "Invalid input! Try again", Toast.LENGTH_SHORT).show();
        }

        return false;

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.challenge_button:
                if (wordFragment.length() >= 4) {
                    yourWord = simpleDictionary.getAnyWordStartingWith(wordFragment);
                    if (yourWord == "noWord") {
                        Toast.makeText(this, "You Wins! No such Word", Toast.LENGTH_SHORT).show();
                        onStart(null);
                    } else if (yourWord == "sameAsPrefix") {
                        Toast.makeText(this, "You Wins! Computer Ended the word", Toast.LENGTH_SHORT).show();
                        onStart(null);
                    } else {
                        Toast.makeText(this, "Computer Wins! Word Exist", Toast.LENGTH_SHORT).show();
                        onStart(null);
                    }
                } else {
                    Toast.makeText(this, "Computer Wins! \nWord is Still Less then 4 Character", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.restart_button:
                onStart(null);
                break;
        }
    }
}
