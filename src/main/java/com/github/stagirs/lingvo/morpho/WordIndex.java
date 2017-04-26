/*
 * Copyright 2017 Dmitriy Malakhov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.stagirs.lingvo.morpho;

import java.io.IOException;
import java.io.InputStream;
import static java.lang.Integer.parseInt;
import java.net.URL;

import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 * TODO хранить в каждой ячейке префикс суф.тек. суф.след
* @author Dmitriy Malakhov
 */
public class WordIndex {
    public static final int MAX_CAPACITY = 8192;
    private static Cache cache;
    private static WordIndex wordIndex;
    static {
        try {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            URL url = cl.getResource("reduced");
            InputStream in = url.openStream();
            try{
                wordIndex = new WordIndex(IOUtils.readLines(in, "utf-8"));
            }finally{
                in.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private int[] wordSize = new int[34 * 34 * 34];
    private long[][] index = new long[34 * 34 * 34][];
    private int[][] morpho = new int[34 * 34 * 34][];
    
    private WordIndex(List<String> words){
        for (String word : words) {
            this.wordSize[getPrefixIndex(word.split("\t")[0])]++;
        }
        for (int i = 0; i < wordSize.length; i++) {
            this.index[i] = new long[(wordSize[i] + 1) * 2];
            this.morpho[i] = new int[wordSize[i] + 1];
        }
        int lastB = -1;
        int cur = 0;
        int lastMorpho = parseInt(words.get(0).split("\t")[1]);
        for (String word : words) {
            String[] parts = word.split("\t");
            int b = getPrefixIndex(parts[0]);
            if(b != lastB){
                for (int j = lastB + 1; j <= b; j++) {
                    this.morpho[j][0] = lastMorpho;
                }
                cur = 1;
                lastB = b;
            }
            long[] id = getReverseWordId(parts[0]);
            this.index[b][cur * 2] = id[0];
            this.index[b][cur * 2 + 1] = id[1];
            this.morpho[b][cur] = parseInt(parts[1]);
            lastMorpho = this.morpho[b][cur];
            cur++;
        }
        for (int j = lastB + 1; j < wordSize.length; j++) {
            this.morpho[j][0] = lastMorpho;
        }
    }
    
    public static void useCache(){
        if(cache != null){
            return;
        }
        cache = new Cache(MAX_CAPACITY, 3);
    }
    
    public static void unuseCache(){
        cache = null;
    }
    
    private int getPrefixIndex(String word){
        int i = char2byte(word.charAt(word.length() - 1));
        i = i * 34 + (word.length() < 2 ? 0 : char2byte(word.charAt(word.length() - 2)));
        i = i * 34 + (word.length() < 3 ? 0 : char2byte(word.charAt(word.length() - 3)));
        return i;
    }
    
    
    private long[] getReverseWordId(String word){
        long[] res = new long[2];
        int lastIndex = word.length() - 1;
        for (int j = 0; j < 12; j++) {
            res[0] = res[0] * 34 + (j < word.length() ? char2byte(word.charAt(lastIndex - j)) : 0);
        }
        for (int j = 0; j < 12; j++) {
            res[1] = res[1] * 34 + (12 + j < word.length() ? char2byte(word.charAt(lastIndex - 12 - j)) : 0);
        }
        return res;
    }

    

    /**
     * @param word искомое в словаре слово
     * @return index типа морфологии
     */
    public int find(String word){
        Integer res;
        if(cache != null){
            res = cache.get(word);
            if (res != null)
                return res;
        }
        int i = getPrefixIndex(word);
        res = searchIn(index[i], morpho[i], getReverseWordId(word), 0, index[i].length / 2 - 1);
        if(cache != null){
            cache.put(word, res);
        }    
        return res;
    }
    
    private int searchIn(long[] words, int[] morpho, long[] word, int begin, int end){

        if(begin >= end){
            return morpho[begin];
        }
        int mid = (begin + end + 1) / 2;
        int midIndex = mid * 2;
        if(word[0] < words[midIndex]){
            return searchIn(words, morpho, word, begin, mid - 1);
        }else if(word[0] > words[midIndex]){
            return searchIn(words, morpho, word, mid, end);
        }
        if(word[1] < words[midIndex + 1]){
            return searchIn(words, morpho, word, begin, mid - 1);
        }else if(word[1] > words[midIndex + 1]){
            return searchIn(words, morpho, word, mid, end);
        }
        return morpho[mid];
    }
    
    private String getWord(int a){
        String res = "";
        res = "" + (char)(a % 34 + 1070);
        a = a / 34;
        res = (char)(a % 34 + 1070) + res;
        a = a / 34;
        res = (char)(a % 34 + 1070) + res;
        return res;
    }
    
    private int char2byte(char c){
        if('а' <= c && c <= 'я'){
            return (c - 'а' + 2);
        }
        if(c == 'ё'){
            return ('е' - 'а' + 2);
        }
        if('А' <= c && c <= 'Я'){
            return (c - 'А' + 2);
        }
        if(c == 'Ё'){
            return ('Е' - 'А' + 2);
        }
        return 1;
    }
    
    public static WordIndex get(){
        return wordIndex;
    }
}
