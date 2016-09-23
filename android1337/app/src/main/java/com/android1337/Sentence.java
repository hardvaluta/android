package com.android;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fedh on 2016-09-19.
 */
public class Sentence {
    public String preWord = "";
    public List<String> wordList= new ArrayList<String>();
    public String postWord = "";
    public Sentence() {

    }
    public Sentence(String preWord, List<String> wordList, String postWord) {
        this.preWord = preWord;
        this.postWord = postWord;
        this.wordList = wordList;
    }

    public String toString() {
        return preWord+"______"+postWord;
    }
}