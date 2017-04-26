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

import com.github.stagirs.lingvo.morpho.MorphoIndex;
import com.github.stagirs.lingvo.morpho.RuleIndex;
import com.github.stagirs.lingvo.morpho.WordIndex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Morpho {
    private List<RulePos> rulePoses = new ArrayList<>();

    public Morpho(List<RulePos> rulePoses) {
        this.rulePoses.addAll(rulePoses);
    }

    public Morpho(String str) {
        for (String item : str.split(" ")) {
            rulePoses.add(new RulePos(item));
        }
    }

    public List<RulePos> getRulePoses() {
        return rulePoses;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Collections.sort(rulePoses);
        for (RulePos item : rulePoses) {
            sb.append(item.getPos()).append(",").append(item.getRuleId()).append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
