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
package com.github.stagirs.lingvo.morpho.builder;

import com.github.stagirs.lingvo.morpho.builder.Dictionary;
import com.github.stagirs.lingvo.morpho.builder.Term;
import com.github.stagirs.lingvo.morpho.model.Lexeme;
import com.github.stagirs.lingvo.morpho.model.Morpho;
import java.io.File;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import static java.lang.Integer.parseInt;

/**
 *
 * @author Dmitriy Malakhov
 */
public class IndexBuilder {
    
    
    public static void build(File dictFile) throws IOException{
        Dictionary dic = new Dictionary(dictFile);
        Map<String, Integer> morpho2id = new HashMap<>();
        List<String> words = new ArrayList<>();
        List<Morpho> morphos = new ArrayList<>();
        for (Term lexeme : dic.getLexems()) {
            if(morphos.isEmpty() || !words.get(words.size() - 1).equals(lexeme.getWord())){
                words.add(lexeme.getWord());
                morphos.add(new Morpho(Arrays.asList(lexeme.getRulePos())));
            } else {
                morphos.get(morphos.size() - 1).getRulePoses().add(lexeme.getRulePos());
            }
        }
        LinkedList<Lexeme> all = new LinkedList<>();
        for (int i = 0; i < words.size(); i++) {
            String morpho = morphos.get(i).toString();
            if(!morpho2id.containsKey(morpho)){
                morpho2id.put(morpho, morpho2id.size());
            }
            all.add(new Lexeme(words.get(i), morpho2id.get(morpho)));
        }
        LinkedList<Lexeme> reduced = new LinkedList<>();
        reduced.add(all.get(0));
        for (Lexeme lc : all) {
            if(lc.getMorphoId() != reduced.getLast().getMorphoId()){
                reduced.add(lc);
            }
        }
        FileUtils.writeLines(new File("all"), "utf-8", all);
        FileUtils.writeLines(new File(new File("src\\main\\resources"), "reduced"), "utf-8", reduced);
        FileUtils.writeLines(new File(new File("src\\main\\resources"), "morphos"), "utf-8", toList(morpho2id));
        FileUtils.writeLines(new File(new File("src\\main\\resources"), "rules"), "utf-8", toList(dic.getRulesMap()));
    }
    
    private static List toList(Map<String, Integer> map){
        Object[] morpho2indexArray = new Object[map.size()];
        for (Map.Entry<String, Integer> morphoIndex : map.entrySet()) {
            morpho2indexArray[morphoIndex.getValue()] = morphoIndex.getKey();
        }
        return Arrays.asList(morpho2indexArray);
    }
    
}
