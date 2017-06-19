package com.github.smivle.common.text;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stagirs.common.document.*;
import com.github.stagirs.common.document.Document;
import com.github.stagirs.common.document.Point;
import com.github.stagirs.lingvo.morpho.MorphoDictionary;
import com.github.stagirs.common.model.DocumentSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.log;


/**
 * Created by Vladimir on 24.04.2017.
 */
public class SemAnnot {
    private static final String THESAURUS = "C:\\Users\\Vladimir\\Documents\\GitHub\\docs\\individuals.xml";
    private static final String WIKI = "C:\\Users\\Vladimir\\Documents\\GitHub\\docs\\ruwiki-20170420-all-titles";
    private static final String FILENAME = "C:\\Users\\Vladimir\\Documents\\GitHub\\docs\\json\\collection-udk 511 01-8-2000.html";
    private static final String DIRNAME =  "C:\\Users\\Vladimir\\Documents\\GitHub\\docs\\json";
    private static final String RESULTDIR =  "C:\\Users\\Vladimir\\Documents\\GitHub\\docs\\tagged";
    private static final String REGEX = "[-_.,:;&!()\\s][_-]*[-.,:;&!()\\s]*";
    private static final ArrayList<String> stopwords = new ArrayList<String>() {{
        add("PREP");
        add("CONJ");
        add("PRCL");
        add("Abbr");
        add("INTJ");
    }};
    public static Map<Integer, String> Thesaurus = new HashMap<Integer, String>();
    public static Map<String, Map<Integer, Integer>> termFreq = new HashMap<String, Map<Integer, Integer>>();
    public static Map<Integer, Integer> termsInDoc = new HashMap<Integer, Integer>();
    public static Integer docAmount = 0;
    public static Integer RESULT = 0;

    public static void createIndex(Map<Integer, String> Thesaurus, Map<String, Set<Integer>> Index) {
        for (Integer key : Thesaurus.keySet()) {
            String idxEntry = "";
            for (String word : Thesaurus.get(key).split(REGEX)) {
                if ((word.length() > 0) && (!word.isEmpty())) {
                    if (stopwords.contains(MorphoDictionary.get(word).getNormForm().getAttrs().get(0).toString())) {
                        continue;
                    }
                    idxEntry = MorphoDictionary.get(word.toLowerCase()).getNorm();
                    if (Index.containsKey(idxEntry)) {
                        Index.get(idxEntry).add(key);
                    } else {
                        Set<Integer> set = new HashSet<Integer>();
                        set.add(key);
                        Index.put(idxEntry, set);
                    }
                } else {
                    continue;
                }
            }
        }
    }

    public static void processPoint(Document article, Map<String, Set<Integer>> Index, Integer doc, boolean mode){

        Point pnt = new Point();
        pnt.setText(article.getTitle());

        for (int len = 2; len <= 6; ++len) {
            searchWindow(len, article.getTitle().split(REGEX), Index, pnt , doc, mode);
        }

        for(Point point : article.getPoints()){
            if(point != null){
                for (int len = 2; len <= 6; ++len) {
                    searchWindow(len, point.toString().split(REGEX), Index, point, doc, mode);
                }
            }
        }
        if(mode == false){
            termsInDoc.put(doc, RESULT);
            RESULT = 0;
        }
    }

    public static void searchWindow(Integer len, String[] words, Map<String, Set<Integer>> Index, Point point, Integer doc, boolean mode){
        if(words.length == 0 || words.length < len){
        }
        else {
            List<Tag> tags = new ArrayList<>();
            Tag tag = new Tag();
            Integer counter = 0;
            String[] normWords = new String[words.length];
            for (int i = 0; i < words.length; ++i) {
                if(words[i] == null || words[i].isEmpty() || stopwords.contains(MorphoDictionary.get(words[i]).getNormForm().getAttrs().get(0).toString())){
                    normWords[i] = null;
                }else{
                    normWords[i] = MorphoDictionary.get(words[i].toLowerCase()).getNorm();
                }
            }

            String[] windowArray = new String[len];
            while (counter < words.length - len + 1) {
                if (words[counter] == null || words[counter].isEmpty()) {
                    counter++;
                    continue;
                }
                for (int i = 0; i < len; ++i) {
                    windowArray[i] = normWords[counter + i];
                }
                if(Index.containsKey(windowArray[0])){
                    boolean inMap = false;
                    Set<Integer> hits = new HashSet<Integer>(Index.get(windowArray[0]));
                    inMap = true;
                    for (int i = 1; i < len && inMap; ++i) {
                        if (hits.size() != 0) {
                            if (Index.containsKey(windowArray[i]) && !windowArray[i].equals(windowArray[i-1])) {
                                hits.retainAll(Index.get(windowArray[i]));
                            } else {
                                inMap = false;
                            }
                        } else {
                            inMap = false;
                        }
                    }

                    if (hits.size() != 0 && inMap) {
                        Integer docSize = termsInDoc.get(doc);
                        for(Integer hit : hits ){
                            if(Thesaurus.get(hit).split(REGEX).length == len){
                                if(mode == false){
                                    String term = Thesaurus.get(hit);
                                    if (!termFreq.containsKey(term)) {
                                        Map<Integer, Integer> termMap = new HashMap<Integer, Integer>();
                                        termMap.put(doc, 1);
                                        termFreq.put(term, termMap);
                                    }else{
                                        if (termFreq.get(term).containsKey(doc)) {
                                            termFreq.get(term).put(doc, termFreq.get(term).get(doc) + 1);
                                        } else {
                                            termFreq.get(term).put(doc, 1);
                                        }
                                    }
                                    RESULT++;
                                }else {
                                    String term = Thesaurus.get(hit);
                                    String[] termWords = term.split(" ");

                                    tag.setWords(termWords);
                                    Double sem = (double)termFreq.get(term).get(doc) / docSize * docAmount/termFreq.get(term).keySet().size();
                                    tag.setSemantic(sem);

                                    if (!point.getTags().contains(tag)) {
                                        point.getTags().add(tag);
                                    }
                                    RESULT++;
                                }
                            }
                        }

                    }
                    hits.clear();
                }
                counter++;
            }
        }
    }

    public static void main(String[] args) {

        Map<String, Set<Integer>> invIndex = new HashMap<String, Set<Integer>>();

        //1 = ВИКИ
        //0 = ОДУ
        Integer thesaurusType = 0;

        BufferedReader br = null;
        FileReader fr = null;


        try {
            String currentLine;
            String prevLine = null;

            //Загрузка тезауруса в мап
            if(thesaurusType == 1){
                String term = null;
                br = new BufferedReader(new FileReader(THESAURUS));
                Integer value = 0;
                while ((currentLine = br.readLine()) != null) {
                    if (currentLine.contains("<lbm:name>") && prevLine.contains("<lbm:PrefferedTerm")) {
                        term = currentLine.substring(currentLine.indexOf(">") + 1, currentLine.lastIndexOf("<"));
                        Thesaurus.put(value, term);
                        value++;
                    }
                    prevLine = currentLine;
                }
            }else{
                String[] terms = null;
                br = new BufferedReader(new FileReader(WIKI));
                for(int i = 0; i<321426; ++i){
                    currentLine = br.readLine();
                }
                Integer value = 0;
                while ((currentLine = br.readLine()) != null) {
                    if (!currentLine.isEmpty()) {
                        terms = currentLine.split("\\s");
                        if(!terms[0].equals("0")){
                            break;
                        }
                        Thesaurus.put(value, terms[1]);
                        value++;
                    }
                }
            }
            System.out.println("Thesaurus loaded");
            System.out.println(Thesaurus.size());

            createIndex(Thesaurus, invIndex);
            System.out.println("Index created");
            System.out.println(invIndex.size());

            ObjectMapper objectMapper = new ObjectMapper();

            File dir = new File(DIRNAME);
            docAmount = dir.listFiles().length;

            Integer docNum = 0;
            for(File file : dir.listFiles()){
                try{
                    Document article = objectMapper.readValue(file, Document.class);
                    processPoint(article, invIndex, docNum, false);
                    docNum++;
                }catch (Exception e){}
            }

            System.out.println("Scan complete");

            docNum = 0;
            for(File file : dir.listFiles()){
                try{
                    Document article = objectMapper.readValue(file, Document.class);
                    processPoint(article, invIndex, docNum,  true);
                    String fName = file.getName().substring(0,file.getName().length()-5);
                    objectMapper.writeValue(new File(RESULTDIR + "\\" + fName + ".json"), article);
                    docNum++;
                }catch (Exception e){}
            }


            System.out.println("RESULT: " + RESULT);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}