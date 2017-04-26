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

import com.github.stagirs.lingvo.morpho.model.Form;
import com.github.stagirs.lingvo.morpho.model.RuleItem;
import com.github.stagirs.lingvo.morpho.model.Rule;
import com.github.stagirs.lingvo.morpho.model.RulePos;
import com.github.stagirs.lingvo.morpho.model.Attr;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Dictionary {
    private final static Map<String, String> GRAMS_MAP = new HashMap<String, String>(){{
        put("NOUN,inan,neut,nomn,sing/ие NOUN,inan,neut,V_be,nomn,sing/ье NOUN,inan,neut,gent,sing/ия NOUN,inan,neut,V_be,gent,sing/ья NOUN,inan,neut,datv,sing/ию NOUN,inan,neut,V_be,datv,sing/ью NOUN,inan,neut,accs,sing/ие NOUN,inan,neut,V_be,accs,sing/ье NOUN,inan,neut,ablt,sing/ием NOUN,inan,neut,V_be,ablt,sing/ьем NOUN,inan,neut,loct,sing/ии NOUN,inan,neut,V_be,loct,sing/ье",
            "NOUN,inan,neut,nomn,sing/ие NOUN,inan,neut,V_be,nomn,sing/ье NOUN,inan,neut,gent,sing/ия NOUN,inan,neut,V_be,gent,sing/ья NOUN,inan,neut,datv,sing/ию NOUN,inan,neut,V_be,datv,sing/ью NOUN,inan,neut,accs,sing/ие NOUN,inan,neut,V_be,accs,sing/ье NOUN,inan,neut,ablt,sing/ием NOUN,inan,neut,V_be,ablt,sing/ьем NOUN,inan,neut,loct,sing/ии NOUN,inan,neut,V_be,loct,sing/ье NOUN,inan,neut,V_be,V_bi,loct,sing/ьи NOUN,inan,neut,nomn,plur/ия NOUN,inan,neut,V_be,nomn,plur/ья NOUN,inan,neut,gent,plur/ий NOUN,inan,neut,datv,plur/иям NOUN,inan,neut,V_be,datv,plur/ьям NOUN,inan,neut,accs,plur/ия NOUN,inan,neut,V_be,accs,plur/ья NOUN,inan,neut,ablt,plur/иями NOUN,inan,neut,V_be,ablt,plur/ьями NOUN,inan,neut,loct,plur/иях NOUN,inan,neut,V_be,loct,plur/ьях");
        
        put("VERB,N1per,indc,pres,sing/ую VERB,N1per,indc,plur,pres/уем VERB,N2per,indc,pres,sing/уешь VERB,N2per,indc,plur,pres/уете VERB,N3per,indc,pres,sing/ует VERB,N3per,indc,plur,pres/уют VERB,indc,masc,past,sing/овал VERB,femn,indc,past,sing/овала VERB,indc,neut,past,sing/овало VERB,indc,past,plur/овали VERB,excl,impr,sing/уй VERB,excl,impr,plur/уйте",
            "VERB,indc,masc,past,sing/овал VERB,femn,indc,past,sing/овала VERB,indc,neut,past,sing/овало VERB,indc,past,plur/овали VERB,N1per,futr,indc,sing/ую VERB,N1per,futr,indc,plur/уем VERB,N2per,futr,indc,sing/уешь VERB,N2per,futr,indc,plur/уете VERB,N3per,futr,indc,sing/ует VERB,N3per,futr,indc,plur/уют VERB,impr,incl,sing/уем VERB,impr,incl,plur/уемте VERB,excl,impr,sing/уй VERB,excl,impr,plur/уйте");
        
        put("VERB,indc,masc,past,sing/л VERB,femn,indc,past,sing/ла VERB,indc,neut,past,sing/ло VERB,indc,past,plur/ли VERB,N1per,futr,indc,sing/ю VERB,N1per,futr,indc,plur/ем VERB,N2per,futr,indc,sing/ешь VERB,N2per,futr,indc,plur/ете VERB,N3per,futr,indc,sing/ет VERB,N3per,futr,indc,plur/ют VERB,impr,incl,sing/ем VERB,impr,incl,plur/емте VERB,excl,impr,sing/й VERB,excl,impr,plur/йте",
            "VERB,indc,masc,past,sing/л VERB,femn,indc,past,sing/ла VERB,indc,neut,past,sing/ло VERB,indc,past,plur/ли VERB,N1per,futr,indc,sing/ю VERB,N1per,futr,indc,plur/ем VERB,N2per,futr,indc,sing/ешь VERB,N2per,futr,indc,plur/ете VERB,N3per,futr,indc,sing/ет VERB,N3per,futr,indc,plur/ют VERB,impr,incl,sing/ем VERB,impr,incl,plur/емте VERB,excl,impr,sing/й VERB,excl,impr,plur/йте VERB,N1per,indc,pres,sing/ю VERB,N1per,indc,plur,pres/ем VERB,N2per,indc,pres,sing/ешь VERB,N2per,indc,plur,pres/ете VERB,N3per,indc,pres,sing/ет VERB,N3per,indc,plur,pres/ют");
        
        put("VERB,N1per,indc,pres,sing/ю VERB,N1per,indc,plur,pres/ем VERB,N2per,indc,pres,sing/ешь VERB,N2per,indc,plur,pres/ете VERB,N3per,indc,pres,sing/ет VERB,N3per,indc,plur,pres/ют VERB,indc,masc,past,sing/л VERB,femn,indc,past,sing/ла VERB,indc,neut,past,sing/ло VERB,indc,past,plur/ли VERB,excl,impr,sing/й VERB,excl,impr,plur/йте",
            "VERB,indc,masc,past,sing/л VERB,femn,indc,past,sing/ла VERB,indc,neut,past,sing/ло VERB,indc,past,plur/ли VERB,N1per,futr,indc,sing/ю VERB,N1per,futr,indc,plur/ем VERB,N2per,futr,indc,sing/ешь VERB,N2per,futr,indc,plur/ете VERB,N3per,futr,indc,sing/ет VERB,N3per,futr,indc,plur/ют VERB,impr,incl,sing/ем VERB,impr,incl,plur/емте VERB,excl,impr,sing/й VERB,excl,impr,plur/йте VERB,N1per,indc,pres,sing/ю VERB,N1per,indc,plur,pres/ем VERB,N2per,indc,pres,sing/ешь VERB,N2per,indc,plur,pres/ете VERB,N3per,indc,pres,sing/ет VERB,N3per,indc,plur,pres/ют");
        
        
        put("VERB,indc,masc,past,sing/овался VERB,femn,indc,past,sing/овалась VERB,indc,neut,past,sing/овалось VERB,indc,past,plur/овались VERB,N1per,futr,indc,sing/уюсь VERB,N1per,futr,indc,plur/уемся VERB,N2per,futr,indc,sing/уешься VERB,N2per,futr,indc,plur/уетесь VERB,N3per,futr,indc,sing/уется VERB,N3per,futr,indc,plur/уются VERB,impr,incl,sing/уемся VERB,impr,incl,plur/уемтесь VERB,excl,impr,sing/уйся VERB,excl,impr,plur/уйтесь",
            "VERB,indc,masc,past,sing/овался VERB,femn,indc,past,sing/овалась VERB,indc,neut,past,sing/овалось VERB,indc,past,plur/овались VERB,N1per,futr,indc,sing/уюсь VERB,N1per,futr,indc,plur/уемся VERB,N2per,futr,indc,sing/уешься VERB,N2per,futr,indc,plur/уетесь VERB,N3per,futr,indc,sing/уется VERB,N3per,futr,indc,plur/уются VERB,impr,incl,sing/уемся VERB,impr,incl,plur/уемтесь VERB,excl,impr,sing/уйся VERB,excl,impr,plur/уйтесь VERB,N1per,indc,pres,sing/уюсь VERB,N1per,indc,plur,pres/уемся VERB,N2per,indc,pres,sing/уешься VERB,N2per,indc,plur,pres/уетесь VERB,N3per,indc,pres,sing/уется VERB,N3per,indc,plur,pres/уются");
        
        put("VERB,N1per,indc,pres,sing/уюсь VERB,N1per,indc,plur,pres/уемся VERB,N2per,indc,pres,sing/уешься VERB,N2per,indc,plur,pres/уетесь VERB,N3per,indc,pres,sing/уется VERB,N3per,indc,plur,pres/уются VERB,indc,masc,past,sing/овался VERB,femn,indc,past,sing/овалась VERB,indc,neut,past,sing/овалось VERB,indc,past,plur/овались VERB,excl,impr,sing/уйся VERB,excl,impr,plur/уйтесь",
            "VERB,indc,masc,past,sing/овался VERB,femn,indc,past,sing/овалась VERB,indc,neut,past,sing/овалось VERB,indc,past,plur/овались VERB,N1per,futr,indc,sing/уюсь VERB,N1per,futr,indc,plur/уемся VERB,N2per,futr,indc,sing/уешься VERB,N2per,futr,indc,plur/уетесь VERB,N3per,futr,indc,sing/уется VERB,N3per,futr,indc,plur/уются VERB,impr,incl,sing/уемся VERB,impr,incl,plur/уемтесь VERB,excl,impr,sing/уйся VERB,excl,impr,plur/уйтесь VERB,N1per,indc,pres,sing/уюсь VERB,N1per,indc,plur,pres/уемся VERB,N2per,indc,pres,sing/уешься VERB,N2per,indc,plur,pres/уетесь VERB,N3per,indc,pres,sing/уется VERB,N3per,indc,plur,pres/уются");
        
        
        put("PRTF,past,pssv,masc,nomn,sing/ый PRTF,past,pssv,gent,masc,sing/ого PRTF,past,pssv,datv,masc,sing/ому PRTF,past,pssv,accs,anim,masc,sing/ого PRTF,past,pssv,accs,inan,masc,sing/ый PRTF,past,pssv,ablt,masc,sing/ым PRTF,past,pssv,loct,masc,sing/ом PRTF,past,pssv,femn,nomn,sing/ая PRTF,past,pssv,femn,gent,sing/ой PRTF,past,pssv,datv,femn,sing/ой PRTF,past,pssv,accs,femn,sing/ую PRTF,past,pssv,ablt,femn,sing/ой PRTF,past,pssv,V_oy,ablt,femn,sing/ою PRTF,past,pssv,femn,loct,sing/ой PRTF,past,pssv,neut,nomn,sing/ое PRTF,past,pssv,gent,neut,sing/ого PRTF,past,pssv,datv,neut,sing/ому PRTF,past,pssv,accs,neut,sing/ое PRTF,past,pssv,ablt,neut,sing/ым PRTF,past,pssv,loct,neut,sing/ом PRTF,past,pssv,nomn,plur/ые PRTF,past,pssv,gent,plur/ых PRTF,past,pssv,datv,plur/ым PRTF,past,pssv,accs,anim,plur/ых PRTF,past,pssv,accs,inan,plur/ые PRTF,past,pssv,ablt,plur/ыми PRTF,past,pssv,loct,plur/ых",
            "ADJF,masc,nomn,sing/ый ADJF,gent,masc,sing/ого ADJF,datv,masc,sing/ому ADJF,accs,anim,masc,sing/ого ADJF,accs,inan,masc,sing/ый ADJF,ablt,masc,sing/ым ADJF,loct,masc,sing/ом ADJF,femn,nomn,sing/ая ADJF,femn,gent,sing/ой ADJF,datv,femn,sing/ой ADJF,accs,femn,sing/ую ADJF,ablt,femn,sing/ой ADJF,V_oy,ablt,femn,sing/ою ADJF,femn,loct,sing/ой ADJF,neut,nomn,sing/ое ADJF,gent,neut,sing/ого ADJF,datv,neut,sing/ому ADJF,accs,neut,sing/ое ADJF,ablt,neut,sing/ым ADJF,loct,neut,sing/ом ADJF,nomn,plur/ые ADJF,gent,plur/ых ADJF,datv,plur/ым ADJF,accs,anim,plur/ых ADJF,accs,inan,plur/ые ADJF,ablt,plur/ыми ADJF,loct,plur/ых");
        put("PRTF,pres,pssv,masc,nomn,sing/ый PRTF,pres,pssv,gent,masc,sing/ого PRTF,pres,pssv,datv,masc,sing/ому PRTF,pres,pssv,accs,anim,masc,sing/ого PRTF,pres,pssv,accs,inan,masc,sing/ый PRTF,pres,pssv,ablt,masc,sing/ым PRTF,pres,pssv,loct,masc,sing/ом PRTF,pres,pssv,femn,nomn,sing/ая PRTF,pres,pssv,femn,gent,sing/ой PRTF,pres,pssv,datv,femn,sing/ой PRTF,pres,pssv,accs,femn,sing/ую PRTF,pres,pssv,ablt,femn,sing/ой PRTF,pres,pssv,V_oy,ablt,femn,sing/ою PRTF,pres,pssv,femn,loct,sing/ой PRTF,pres,pssv,neut,nomn,sing/ое PRTF,pres,pssv,gent,neut,sing/ого PRTF,pres,pssv,datv,neut,sing/ому PRTF,pres,pssv,accs,neut,sing/ое PRTF,pres,pssv,ablt,neut,sing/ым PRTF,pres,pssv,loct,neut,sing/ом PRTF,pres,pssv,nomn,plur/ые PRTF,pres,pssv,gent,plur/ых PRTF,pres,pssv,datv,plur/ым PRTF,pres,pssv,accs,anim,plur/ых PRTF,pres,pssv,accs,inan,plur/ые PRTF,pres,pssv,ablt,plur/ыми PRTF,pres,pssv,loct,plur/ых",
            "ADJF,masc,nomn,sing/ый ADJF,gent,masc,sing/ого ADJF,datv,masc,sing/ому ADJF,accs,anim,masc,sing/ого ADJF,accs,inan,masc,sing/ый ADJF,ablt,masc,sing/ым ADJF,loct,masc,sing/ом ADJF,femn,nomn,sing/ая ADJF,femn,gent,sing/ой ADJF,datv,femn,sing/ой ADJF,accs,femn,sing/ую ADJF,ablt,femn,sing/ой ADJF,V_oy,ablt,femn,sing/ою ADJF,femn,loct,sing/ой ADJF,neut,nomn,sing/ое ADJF,gent,neut,sing/ого ADJF,datv,neut,sing/ому ADJF,accs,neut,sing/ое ADJF,ablt,neut,sing/ым ADJF,loct,neut,sing/ом ADJF,nomn,plur/ые ADJF,gent,plur/ых ADJF,datv,plur/ым ADJF,accs,anim,plur/ых ADJF,accs,inan,plur/ые ADJF,ablt,plur/ыми ADJF,loct,plur/ых");
        put("PRTF,actv,past,masc,nomn,sing/ий PRTF,actv,past,gent,masc,sing/его PRTF,actv,past,datv,masc,sing/ему PRTF,actv,past,accs,anim,masc,sing/его PRTF,actv,past,accs,inan,masc,sing/ий PRTF,actv,past,ablt,masc,sing/им PRTF,actv,past,loct,masc,sing/ем PRTF,actv,past,femn,nomn,sing/ая PRTF,actv,past,femn,gent,sing/ей PRTF,actv,past,datv,femn,sing/ей PRTF,actv,past,accs,femn,sing/ую PRTF,actv,past,ablt,femn,sing/ей PRTF,actv,past,V_ey,ablt,femn,sing/ею PRTF,actv,past,femn,loct,sing/ей PRTF,actv,past,neut,nomn,sing/ее PRTF,actv,past,gent,neut,sing/его PRTF,actv,past,datv,neut,sing/ему PRTF,actv,past,accs,neut,sing/ее PRTF,actv,past,ablt,neut,sing/им PRTF,actv,past,loct,neut,sing/ем PRTF,actv,past,nomn,plur/ие PRTF,actv,past,gent,plur/их PRTF,actv,past,datv,plur/им PRTF,actv,past,accs,anim,plur/их PRTF,actv,past,accs,inan,plur/ие PRTF,actv,past,ablt,plur/ими PRTF,actv,past,loct,plur/их",
            "ADJF,masc,nomn,sing/ий ADJF,gent,masc,sing/его ADJF,datv,masc,sing/ему ADJF,accs,anim,masc,sing/его ADJF,accs,inan,masc,sing/ий ADJF,ablt,masc,sing/им ADJF,loct,masc,sing/ем ADJF,femn,nomn,sing/ая ADJF,femn,gent,sing/ей ADJF,datv,femn,sing/ей ADJF,accs,femn,sing/ую ADJF,ablt,femn,sing/ей ADJF,V_ey,ablt,femn,sing/ею ADJF,femn,loct,sing/ей ADJF,neut,nomn,sing/ее ADJF,gent,neut,sing/его ADJF,datv,neut,sing/ему ADJF,accs,neut,sing/ее ADJF,ablt,neut,sing/им ADJF,loct,neut,sing/ем ADJF,nomn,plur/ие ADJF,gent,plur/их ADJF,datv,plur/им ADJF,accs,anim,plur/их ADJF,accs,inan,plur/ие ADJF,ablt,plur/ими ADJF,loct,plur/их");
   
        put("PRTF,actv,pres,masc,nomn,sing/ий PRTF,actv,pres,gent,masc,sing/его PRTF,actv,pres,datv,masc,sing/ему PRTF,actv,pres,accs,anim,masc,sing/его PRTF,actv,pres,accs,inan,masc,sing/ий PRTF,actv,pres,ablt,masc,sing/им PRTF,actv,pres,loct,masc,sing/ем PRTF,actv,pres,femn,nomn,sing/ая PRTF,actv,pres,femn,gent,sing/ей PRTF,actv,pres,datv,femn,sing/ей PRTF,actv,pres,accs,femn,sing/ую PRTF,actv,pres,ablt,femn,sing/ей PRTF,actv,pres,V_ey,ablt,femn,sing/ею PRTF,actv,pres,femn,loct,sing/ей PRTF,actv,pres,neut,nomn,sing/ее PRTF,actv,pres,gent,neut,sing/его PRTF,actv,pres,datv,neut,sing/ему PRTF,actv,pres,accs,neut,sing/ее PRTF,actv,pres,ablt,neut,sing/им PRTF,actv,pres,loct,neut,sing/ем PRTF,actv,pres,nomn,plur/ие PRTF,actv,pres,gent,plur/их PRTF,actv,pres,datv,plur/им PRTF,actv,pres,accs,anim,plur/их PRTF,actv,pres,accs,inan,plur/ие PRTF,actv,pres,ablt,plur/ими PRTF,actv,pres,loct,plur/их",
            "ADJF,masc,nomn,sing/ий ADJF,gent,masc,sing/его ADJF,datv,masc,sing/ему ADJF,accs,anim,masc,sing/его ADJF,accs,inan,masc,sing/ий ADJF,ablt,masc,sing/им ADJF,loct,masc,sing/ем ADJF,femn,nomn,sing/ая ADJF,femn,gent,sing/ей ADJF,datv,femn,sing/ей ADJF,accs,femn,sing/ую ADJF,ablt,femn,sing/ей ADJF,V_ey,ablt,femn,sing/ею ADJF,femn,loct,sing/ей ADJF,neut,nomn,sing/ее ADJF,gent,neut,sing/его ADJF,datv,neut,sing/ему ADJF,accs,neut,sing/ее ADJF,ablt,neut,sing/им ADJF,loct,neut,sing/ем ADJF,nomn,plur/ие ADJF,gent,plur/их ADJF,datv,plur/им ADJF,accs,anim,plur/их ADJF,accs,inan,plur/ие ADJF,ablt,plur/ими ADJF,loct,plur/их");
   
    }};
    
    private final static String[] IGNORE_FORMS = new String[]{
        "impf", "perf",
        "tran", "intr", "Infr", 
        "Slng", "Arch", "Litr",
        "Subx", 
        "Supr", "Qual", "Apro", "Anum", "Poss",//виды прилагательного
        "Sgtm", "Pltm"
    };
    
    private List<Term> lexems = new ArrayList<>();
    private Map<String, Integer> rulesMap = new HashMap<>();
    
    public Dictionary(File file) throws IOException{
        String dictionary = null;
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
        try{
            ZipEntry ze = zis.getNextEntry();
            if(ze == null){
                throw new RuntimeException("can't unzip file");
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(zis, baos);
            dictionary = new String(baos.toByteArray(), "utf-8");
        }finally{
            zis.close();
        }
        if(dictionary == null){
            throw new RuntimeException("can't unzip file");
        }
        Pattern normPattern = Pattern.compile("<l.*?t=\"(.*?)\">(.*?)</l>", Pattern.MULTILINE|Pattern.DOTALL);
        Pattern rawPattern = Pattern.compile("<f.*?t=\"(.*?)\">(.*?)</f>", Pattern.MULTILINE|Pattern.DOTALL);
        Pattern gPattern = Pattern.compile("<g.*?v=\"(.*?)\".*?/>", Pattern.MULTILINE|Pattern.DOTALL);
        Matcher m = Pattern.compile("<lemma id=\"(.*?)\".*?/lemma>", Pattern.MULTILINE|Pattern.DOTALL).matcher(dictionary);
        while(m.find()){
            String lemma = m.group(0);
            Matcher normMatcher = normPattern.matcher(lemma);
            if(normMatcher.find()){
                
                List<Attr> mainAttr = fillAttributes(gPattern.matcher(normMatcher.group(2)));
                if(mainAttr.contains(Attr.COMP)){
                    continue;
                }
                if(mainAttr.contains(Attr.GRND)){
                    Matcher rawMatcher = rawPattern.matcher(lemma);
                    while(rawMatcher.find()){
                        String rule = new Rule(Arrays.asList(new RuleItem(new Form(Arrays.asList(Attr.GRND), Collections.EMPTY_LIST), "/"))).toString();
                        if(!rulesMap.containsKey(rule)){
                            rulesMap.put(rule, rulesMap.size());
                        }
                        lexems.add(new Term(rawMatcher.group(1).replace('ё', 'е').replace("’", ""), new RulePos(0, rulesMap.get(rule))));
                    }
                    continue;
                }
                List<String> words = new ArrayList<>();
                List<Form> attrs = new ArrayList<>();
                Matcher rawMatcher = rawPattern.matcher(lemma);
                while(rawMatcher.find()){
                    String word = rawMatcher.group(1).replace('ё', 'е').replace("’", "");
                    words.add(word);
                    attrs.add(new Form(mainAttr, fillAttributes(gPattern.matcher(rawMatcher.group(2)))));
                }
                Rule rule = getRule(words, attrs);
                String normForm = words.get(0);
                int sufSize = rule.getItems().get(0).getSuffix().length();
                String ruleStr = rule.toString();
                if(GRAMS_MAP.containsKey(ruleStr)){
                    ruleStr = GRAMS_MAP.get(ruleStr);
                    rule = new Rule(ruleStr);
                }
                if(!rulesMap.containsKey(ruleStr)){
                    rulesMap.put(ruleStr, rulesMap.size());
                }
                for (int i = 0; i < rule.getItems().size(); i++) {
                    lexems.add(new Term(getWord(normForm, sufSize, rule.getItems().get(i)), new RulePos(i, rulesMap.get(ruleStr))));
                }
            }
        }
        Collections.sort(lexems);
    }
    
    private String getWord(String normForm, int sufSize, RuleItem attr){
        String mainPart = normForm.substring(0, normForm.length() - sufSize);
        if(attr.getForm().getAttrs().contains(Attr.Cmp2)){
            mainPart = "по" + mainPart;
        }
        return mainPart + attr.getSuffix();
    }
    
    private Rule getRule(List<String> forms, List<Form> attrs){
        int count = getPrefixSize(forms, attrs);
        List<RuleItem> result = new ArrayList<>();
        for (int i = 0; i < forms.size(); i++) {
            result.add(new RuleItem(attrs.get(i), forms.get(i).substring(count + (attrs.get(i).getAttrs().contains(Attr.Cmp2) ? 2 : 0))));
        }
        return new Rule(result);
    }
    
    private int getPrefixSize(List<String> forms, List<Form> attrs){
        int count = 0;
        while(count < forms.get(0).length()){
            char c = forms.get(0).charAt(count);
            for (int i = 1; i < forms.size(); i++) {
                if(forms.get(i) == null){
                    continue;
                }
                if(count >= forms.get(i).length() || c != forms.get(i).charAt(count + (attrs.get(i).getAttrs().contains(Attr.Cmp2) ? 2 : 0))){
                    return count;
                }
            }
            count++;
        }
        return count;
    }
    
    private List<Attr> fillAttributes(Matcher attrMatcher){
        List<String> list = new ArrayList<>();
        while(attrMatcher.find()){
            String attr = attrMatcher.group(1).replace("-", "_");
            if(Character.isDigit(attr.charAt(0))){
                attr = "N" + attr;
            }
            boolean b = false;
            for (String str : IGNORE_FORMS) {
                if(str.equals(attr)){
                    b = true;
                    break;
                }
            }
            if(b){
                continue;
            }
            list.add(attr);
        }
        Collections.sort(list);
        List<Attr> result = new ArrayList<>();
        for (String attr : list) {
            result.add(Attr.valueOf(attr));
        }
        return result;
    }

    public Map<String, Integer> getRulesMap() {
        return rulesMap;
    }

    public List<Term> getLexems() {
        return lexems;
    }

    
}
