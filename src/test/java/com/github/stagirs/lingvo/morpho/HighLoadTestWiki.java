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
import org.apache.commons.io.FileUtils;
import org.apache.lucene.morphology.russian.RussianMorphology;
import org.junit.Test;
import org.tartarus.snowball.ext.RussianStemmer;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Dmitriy Malakhov
 */
public class HighLoadTestWiki {
    @Test
    public void test() throws IOException, InterruptedException{
        List<String> all = FileUtils.readLines(new File("all_rus_wiki_1M.txt"), "utf-8");
        WordIndex.get();
        MorphoIndex.get();
        RuleIndex.get();
        long time = - System.currentTimeMillis();
        for (int i=0; i<10; i++){
            for (String str : all) {
                try {
                    String norm = MorphoDictionary.get(str).getNorm();
                } catch (java.lang.StringIndexOutOfBoundsException e) {
//                System.out.println(str);
                }
            }
        }
        System.out.println("Stagirs: " + (System.currentTimeMillis() + time)/10.0 + " " + all.size());
    }
}
