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
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Dmitriy Malakhov
 */
public class MorphoDictionaryTest {
    @Test
    public void test() throws IOException, InterruptedException{
        assertEquals(MorphoDictionary.get("и").getNorm(), "и");
        assertEquals(MorphoDictionary.get("и").getNormForm().getAttrs().toString(), "[CONJ]");
        assertEquals(MorphoDictionary.get("красивого").getNorm(), "красивый");
        assertEquals(MorphoDictionary.get("красивому").getNorm(), "красивый");
        assertEquals(MorphoDictionary.get("цветка").getNorm(), "цветок");
        assertEquals(MorphoDictionary.get("цветку").getNorm(), "цветок");
        assertEquals(MorphoDictionary.get("ежившегося").getNorm(), "ежившийся");
        assertEquals(MorphoDictionary.get("нормой").getNorm(), "норма");
        assertEquals(MorphoDictionary.get("Абеля").getNorm(), "Абель");
        assertEquals(MorphoDictionary.get("Шварца").getNorm(), "Шварц");
        assertEquals(MorphoDictionary.get("Шварца").getNormForm().getAttrs().toString(), "[NOUN, Surn, anim, masc, gent, sing]");
    }
}
