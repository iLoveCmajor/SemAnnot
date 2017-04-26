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

import com.github.stagirs.lingvo.morpho.model.Morpho;
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
public class MorphoIndex {
    private static MorphoIndex morphoIndex;
    static {
        try {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            URL url = cl.getResource("morphos");
            InputStream in = url.openStream();
            try{
                morphoIndex = new MorphoIndex(IOUtils.readLines(in, "utf-8"));
            }finally{
                in.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    private Morpho[] morphos;

    private MorphoIndex(List<String> morphos) {
        this.morphos = new Morpho[morphos.size()];
        for (int i = 0; i < morphos.size(); i++) {
            this.morphos[i] = new Morpho(morphos.get(i));
        }
    }
    
    
    public Morpho getMorpho(int index){
        return morphos[index];
    }
    
    public static MorphoIndex get(){
        return morphoIndex;
    }
}
