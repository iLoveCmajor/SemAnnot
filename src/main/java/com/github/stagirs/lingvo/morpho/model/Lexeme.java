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
package com.github.stagirs.lingvo.morpho.model;

import static java.lang.Integer.parseInt;
import java.util.Map;
import static java.lang.Integer.parseInt;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Lexeme {
    private String word;
    private int morphoId;

    public Lexeme(String word, int morphoId) {
        this.word = word;
        this.morphoId = morphoId;
    }

    public Lexeme(String str) {
        String[] parts = str.split("\t");
        word = parts[0];
        morphoId = parseInt(parts[1]);
    }

    public int getMorphoId() {
        return morphoId;
    }

    public String getWord() {
        return word;
    }
    
    @Override
    public String toString() {
        return word + "\t" + morphoId;
    }
}
