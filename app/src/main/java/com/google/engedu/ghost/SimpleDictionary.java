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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    //Constructor which involves reading the dictionary asset file and store it in the ArrayList
    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                words.add(line.trim());
        }
    }

    //Returns if the dictionary contains that specific word in the parameter
    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    //Chooses a word which is possible to append
    @Override
    public String getAnyWordStartingWith(String prefix) {
        int possibleWordIndex;
        String possibleWord;

        //If no prefix i.e beginning of the word, then pick a random word from the dictionary
        if (prefix == "") {
            Random random = new Random();
            int randomIndex = random.nextInt(words.size());
            return words.get(randomIndex);
        }
        //If any letter is present in the display field
        else {

            //Finds the index of that possible word in the dictionary
            possibleWordIndex = searchPossibleWords(prefix);

            //If no word found return a message
            if (possibleWordIndex == -1) {
                return "noWord";
            }

            //If a word is indeed found
            else {
                //Find the word from that index of possible word in the dictionary
                possibleWord = words.get(possibleWordIndex);

                //If the word is same as the prefix return a corresponding message
                if (possibleWord.equals(prefix)) {
                    return "sameAsPrefix";
                }
                //If the word isn't same as that of the prefix, return the new word
                else {
                    return possibleWord;
                }
            }
        }
    }

    //Helper method for searching the next possible words in the dictionary (USES THE BINARY SEARCH TREE)
    private int searchPossibleWords(String prefix) {

        //Beginning index
        int lowerIndex = 0;

        //Last index
        int higherIndex = words.size() - 1;

        //Variables to store the middle index and checking flag
        int middleIndex, checkList;

        //That word present on the middle index
        String checkWord;

        //Binary search tree sorting logic
        while (lowerIndex <= higherIndex) {
            //Calculates the middle index
            middleIndex = (lowerIndex + higherIndex) / 2;

            //Finds the word present on the middle index
            checkWord = words.get(middleIndex);

            //Compares the initial letter of the middle word in sorting
            checkList = checkWord.startsWith(prefix) ? 0 : prefix.compareTo(checkWord);

            //If it is that word, return its index
            if (checkList == 0) {
                return middleIndex;
            }

            else if (checkList > 0) {
                lowerIndex = middleIndex + 1;
            } else {
                higherIndex = middleIndex - 1;
            }
        }
        return -1;
    }


    //Reserved for ghost 2 game
    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        return selected;
    }
}
