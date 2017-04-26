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

import com.github.stagirs.lingvo.morpho.model.Rule;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Dmitriy Malakhov
 */
public class RuleIndex {
    private static RuleIndex ruleIndex;
    static {
        try {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            URL url = cl.getResource("rules");
            InputStream in = url.openStream();
            try{
                ruleIndex = new RuleIndex(IOUtils.readLines(in, "utf-8"));
            }finally{
                in.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    private Rule[] rules;

    private RuleIndex(List<String> rules) {
        this.rules = new Rule[rules.size()];
        for (int i = 0; i < rules.size(); i++) {
            this.rules[i] = new Rule(rules.get(i));
        }
    }
    
    
    public Rule getRule(int index){
        return rules[index];
    }
    
    public static RuleIndex get(){
        return ruleIndex;
    }
}
