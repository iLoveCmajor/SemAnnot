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

import com.github.stagirs.lingvo.morpho.model.Lexeme;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.morphology.russian.RussianMorphology;
import org.junit.Test;
import org.tartarus.snowball.ext.RussianStemmer;

/**
 *
 * @author Dmitriy Malakhov
 */
public class HighLoadTest {
    @Test
    public void test() throws IOException, InterruptedException{
        List<String> all = FileUtils.readLines(new File("all"), "utf-8");
        WordIndex.get();
        MorphoIndex.get();
        RuleIndex.get();
        long time = - System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            for (String str : all) {
                Lexeme lexeme = new Lexeme(str);
                MorphoDictionary.get(lexeme.getWord()).getNorm();
            }
        }
        System.out.println("Stagirs: " + (System.currentTimeMillis() + time) + " " + all.size());
        RussianStemmer rs = new RussianStemmer();
        time = - System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            for (String str : all) {
                Lexeme lexeme = new Lexeme(str);
                rs.setCurrent(lexeme.getWord());
                rs.stem();
                rs.getCurrent();
            }
        }
        System.out.println("Stemmer: " + (System.currentTimeMillis() + time) + " " + all.size());
        RussianMorphology ra = new RussianMorphology();
        time = - System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            for (String str : all) {
                Lexeme lexeme = new Lexeme(str);
                if(lexeme.getWord().matches("[а-я]*")){
                    ra.getNormalForms(lexeme.getWord());
                }
            }
        }
        System.out.println("AOT(Lucene): " + (System.currentTimeMillis() + time) + " " + all.size());
        
    }
}
