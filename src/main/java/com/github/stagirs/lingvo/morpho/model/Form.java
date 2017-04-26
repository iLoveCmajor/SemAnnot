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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Form {
    private List<Attr> attrs = new ArrayList<>();

    public Form(List<Attr> mainAttrs, List<Attr> attrs) {
        this.attrs.addAll(mainAttrs);
        this.attrs.addAll(attrs);
    }
    
    public Form(String line){
        for(String attr : line.split(",")){
            attrs.add(Attr.valueOf(attr));
        }
    }

    public List<Attr> getAttrs() {
        return attrs;
    }
}
