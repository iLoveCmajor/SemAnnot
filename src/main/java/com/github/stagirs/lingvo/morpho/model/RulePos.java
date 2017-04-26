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

/**
 *
 * @author Dmitriy Malakhov
 */
public class RulePos implements Comparable<RulePos>{
    private int ruleId;
    private int pos;

    public RulePos(int pos, int ruleId) {
        this.ruleId = ruleId;
        this.pos = pos;
    }
    
    public RulePos(String line) {
        String[] parts = line.split(",");
        this.ruleId = parseInt(parts[1]);
        this.pos = parseInt(parts[0]);
    }

    public int getPos() {
        return pos;
    }

    public int getRuleId() {
        return ruleId;
    }
    
    @Override
    public String toString() {
        return pos + "," + ruleId;
    }

    @Override
    public int compareTo(RulePos o) {
        return ruleId - o.ruleId == 0 ? (pos - o.pos) : (ruleId - o.ruleId);
    }
    
}
