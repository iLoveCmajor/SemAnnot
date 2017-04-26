/*
 * Copyright 2016 Dmitriy Malakhov.
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
package com.github.smivle.common.text;

import com.github.stagirs.common.text.TextUtils;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Dmitriy Malakhov
 */
public class TextUtilsTest {
    public void charactersClutchTest(){
        assertEquals(TextUtils.charactersClutch("общая психология-\nА в т о р е ф е р а т \nдиооѳртации"), "общая психологияАвтореферат  диооѳртации");
        assertEquals(TextUtils.charactersClutch("А в т о р е ф е р а т \nдиооѳртации"), "Автореферат  диооѳртации");
        assertEquals(TextUtils.charactersClutch("общая психология \nА в т о р е ф е р а т"), "общая психология  Автореферат");
    }
}
