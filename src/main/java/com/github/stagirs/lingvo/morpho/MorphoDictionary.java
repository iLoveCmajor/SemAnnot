package com.github.stagirs.lingvo.morpho;

import com.github.stagirs.lingvo.morpho.model.Form;
import com.github.stagirs.lingvo.morpho.model.Morpho;
import com.github.stagirs.lingvo.morpho.model.Rule;
import com.github.stagirs.lingvo.morpho.model.RuleItem;
import com.github.stagirs.lingvo.morpho.model.RulePos;


/**
 * Класс предназначен для получения кандидатов на нормальную форму. И кандидатов на всевозможные формы слова.
 * Для вычисления кандидатов используется только синтаксическая запись слова. 
 * Использование контекста для разрешения неоднозначности планируется на следующем этапе.
 * @author pc
 */
public class MorphoDictionary {
    private static WordIndex wordIndex = WordIndex.get();
    private static MorphoIndex morphoIndex = MorphoIndex.get();
    private static RuleIndex ruleIndex = RuleIndex.get();
    
    private String word;
    private Morpho morpho;

    public MorphoDictionary(String word, Morpho morpho) {
        this.word = word;
        this.morpho = morpho;
    }
    
    public static MorphoDictionary get(String word){
        int index = wordIndex.find(word);
        return new MorphoDictionary(word, morphoIndex.getMorpho(index));
    }
    
    public boolean isNorm(){
        for (RulePos rulePos : morpho.getRulePoses()) {
            if(rulePos.getPos() == 0){
                return true;
            }
        }
        return false;
    }
    
    public String getNorm(){
        return getNorm(morpho.getRulePoses().get(0));
    }
    
    public String[] getNorms(){
        String[] words = new String[morpho.getRulePoses().size()];
        int i = 0;
        for (RulePos rulePos : morpho.getRulePoses()) {
            words[i++] = getNorm(rulePos);
        }
        return words;
    }
    
    public Form getRawForm(){
        return getRawForm(morpho.getRulePoses().get(0));
    }
    
    public Form getNormForm(){
        return getRawForm(morpho.getRulePoses().get(0));
    }
    
    public Form[] getRawForms(){
        Form[] forms = new Form[morpho.getRulePoses().size()];
        int i = 0;
        for (RulePos rulePos : morpho.getRulePoses()) {
            forms[i++] = getRawForm(rulePos);
        }
        return forms;
    }
    
    public Form[] getNormForms(){
        Form[] forms = new Form[morpho.getRulePoses().size()];
        int i = 0;
        for (RulePos rulePos : morpho.getRulePoses()) {
            forms[i++] = getNormForm(rulePos);
        }
        return forms;
    }
    
    private String getNorm(RulePos rulePos){
        if(rulePos.getPos() == 0){
            return word;
        }
        Rule rule = ruleIndex.getRule(rulePos.getRuleId());
        RuleItem norm = rule.getItems().get(0);
        RuleItem raw = rule.getItems().get(rulePos.getPos());
        if(!word.endsWith(raw.getSuffix())){
            return word;
        }
        String stem = word.substring(0, word.length() - raw.getSuffix().length());
        return stem + norm.getSuffix();
    }
    
    private Form getRawForm(RulePos rulePos){
        Rule rule = ruleIndex.getRule(rulePos.getRuleId());
        return rule.getItems().get(rulePos.getPos()).getForm();
    }
    
    private Form getNormForm(RulePos rulePos){
        Rule rule = ruleIndex.getRule(rulePos.getRuleId());
        return rule.getItems().get(0).getForm();
    }
}
