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

import com.github.stagirs.lingvo.morpho.model.RulePos;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Term implements Comparable<Term>{
    private String word;
    private String reverseWord;
    private RulePos rulePos;

    public Term(String word, RulePos rulePos) {
        this.word = word;
        this.reverseWord = new StringBuilder(word).reverse().toString();
        this.rulePos = rulePos;
    }

    public RulePos getRulePos() {
        return rulePos;
    }
    
    
    public String getReverseWord() {
        return reverseWord;
    }

    public String getWord() {
        return word;
    }

    @Override
    public int compareTo(Term o) {
        return reverseWord.compareTo(o.reverseWord);
    }
}
