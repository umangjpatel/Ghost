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

import java.util.HashMap;


class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    TrieNode() {

        //Initialise the HashMap reference variable
        children = new HashMap<>();

        //Set all the nodes to having a false boolean value
        isWord = false;
    }

    void add(String s) {

        //Creating a temporary hash map for the word to store
        HashMap<String, TrieNode> temp = new HashMap<>();

        //Loop for adding the letters at the nodes of the trie data structure
        for (int i = 0; i < s.length(); i++) {

            //If a letter of the word doesn't exist as a node then add it to the HashMap
            if (!temp.containsKey(String.valueOf(s.charAt(i)))) {
                temp.put(String.valueOf(s.charAt(i)), new TrieNode());
            }

            //If the last letter of the word completes it, then set the boolean flag to true.
            if (i == s.length() - 1) {
                temp.get(String.valueOf(s.charAt(i))).isWord = true;
            }

            //Add the temporary hash map to the entire trie data structure
            temp = temp.get(String.valueOf(s.charAt(i))).children;

        }
    }

    //Helper method to check if the word exists in the trie data structure
    private TrieNode searchingWord(String s) {

        //Get the reference of the TrieNode class
        TrieNode trieNode = this;

        //Run a loop for checking through the word trie data structure
        for (int i = 0; i < s.length(); i++) {

            //If the letter is existing in the node, store the children element in the reference
            if (trieNode.children.containsKey(String.valueOf(s.charAt(i)))) {
                trieNode = trieNode.children.get(String.valueOf(s.charAt(i)));
            }
            //Else return null value because the letter is not a node of its upper node
            else {
                return null;
            }
        }

        return trieNode;
    }

    boolean isWord(String s) {
        TrieNode temp = searchingWord(s);

        //If no word is correctly formed, return a false boolean value
        if (temp == null) {
            return false;
        }
        //If a word exists, then continue to form the correct word by returning it a true value
        else {
            return temp.isWord;
        }
    }

    String getAnyWordStartingWith(String s) {
        return null;
    }

    String getGoodWordStartingWith(String s) {
        return null;
    }
}
