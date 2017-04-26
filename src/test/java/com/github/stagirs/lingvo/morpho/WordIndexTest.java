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
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Dmitriy Malakhov
 */
public class WordIndexTest {
    @Test
    public void test() throws IOException, InterruptedException{
        List<String> all = FileUtils.readLines(new File("all"), "utf-8");
        WordIndex wordIndex = WordIndex.get();
        for (int i = 0; i < all.size(); i++) {
            Lexeme lexeme = new Lexeme(all.get(i));
            assertEquals("index: " + i + "; word: " + lexeme.getWord() , wordIndex.find(lexeme.getWord()), lexeme.getMorphoId());
        }
    }
}
