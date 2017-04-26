package com.github.smivle.common.text;

import com.github.stagirs.common.model.*;
import com.github.stagirs.lingvo.morpho.MorphoDictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Created by Vladimir on 24.04.2017.
 */
public class SemAnnot {
    private static final String FILENAME1 = "C:\\Users\\Vladimir\\Documents\\GitHub\\docs\\individuals.xml";
    private static final String FILENAME2 = "C:\\Users\\Vladimir\\Documents\\GitHub\\docs\\parsed_xml\\1997 04 03-4.html";
    private static final String DIRNAME =  "C:\\Users\\Vladimir\\Documents\\GitHub\\docs\\parsed_xml";

    //Нормализация понятий тезауруса + пословно
    public static void Normalize(Set<String> keySet, Map<String, Integer> normalizedThesaurus) {

        for(String key : keySet){
            String string = "";
            for(String word : key.split(" ")){
                if(word.length() > 0){
                    string += MorphoDictionary.get(word.toLowerCase()).getNorm() + " ";
                }
                else{
                    string = "BAD_TERM";
                }
            }
            normalizedThesaurus.put(string.substring(0,string.length()-1),0);
        }
    }

    public static void searchForWords(List<Block> blockList, Map<String, Integer> normalizedThesaurus){
        //System.out.println("Blocksize = " + blockList.size());
        Iterator<Block> blockIterator = blockList.iterator();
        while(blockIterator.hasNext()){
            Block block = blockIterator.next();
            if(block.isPoint()){
                Point point = (Point) block;
                //System.out.println("Pointsize = " + point.getSentences().size());
                for(Sentence sentence : point.getSentences()){
                    //System.out.println("-------Sentence-------");
                    for(Text part : sentence.getParts()){
                        //System.out.println("--Part--");
                        //System.out.println(part.getText());
                        //System.out.println(part.getText().split("[.,;()\\s]").length);
                        //searchWindow(1,part.getText().split("[-.,;()\\s][-.,;()\\s]*"), normalizedThesaurus);
                        for(int len = 1; len <= 7; ++len){
                            searchWindow(len,part.getText().split("[-.,;()\\s][-.,;()\\s]*"), normalizedThesaurus);
                        }
                        //searchWindow(3,part.getText().split("[-.,;()\\s][-.,;()\\s]*"), normalizedThesaurus);
//                        for(String word : part.getText().split("[-.,;()\\s][-.,;()\\s]*")){
//                            if(word.length() > 0){
//                                word = MorphoDictionary.get(word.toLowerCase()).getNorm();
//                                //System.out.println(word);
//                                if(normalizedThesaurus.containsKey(word)){
//                                    System.out.println("TERM FOUND: " + word);
//                                }
//                            }
//                        }
                    }
                }
            }
        }
    }

    public static void searchWindow(Integer len, String[] words, Map<String, Integer> Thesaurus){
        if(words.length == 0){
        }
        else {
            Integer counter = 0;
            String candidate = "";

                String[] windowarray = new String[len];
                while (counter < words.length - len + 1) {
                    if (words[counter].length() == 0) {
                        counter++;
                        continue;
                    }
                    for (int i = 0; i < len; ++i) {
                        windowarray[i] = MorphoDictionary.get(words[counter + i].toLowerCase()).getNorm();
                    }
                    counter++;
                    candidate = "";
                    for (int i = 0; i < len; ++i) {
                        candidate += windowarray[i] + " ";
                    }
                    candidate = candidate.substring(0, candidate.length() - 1);
                    if (Thesaurus.containsKey(candidate)) {
                        System.out.println("Found: " + candidate);
                    }
                    //System.out.println(candidate);
                }
            }
    }

    public static void main(String[] args) {

        BufferedReader br = null;
        FileReader fr = null;

        Map<String, Integer> Thesaurus = new HashMap<String, Integer>();
        Map<String, Integer> normThesaurus = new HashMap<String, Integer>();
        String term = null;

        try {
            String currentLine;
            String prevLine = null;
            br = new BufferedReader(new FileReader(FILENAME1));

            //Загрузка тезауруса в мап
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.contains("<lbm:name>") && prevLine.contains("<lbm:PrefferedTerm")) {
                    term = currentLine.substring(currentLine.indexOf(">") + 1, currentLine.lastIndexOf("<"));
//                    term = MorphoDictionary.get(term.toLowerCase()).getNorm();
                    Thesaurus.put(term, 0);
                }
                prevLine = currentLine;
            }

            Thesaurus.put("усиление пространства",0);
            Thesaurus.put("скалярным произведением",0);

            //Нормализация понятий тезауруса + пословно
            Normalize(Thesaurus.keySet(),normThesaurus);
//            normThesaurus.put("произведение являюсь гильбертов",0);

//            normThesaurus.put("оценка",0);
            //normThesaurus.put(MorphoDictionary.get("ОДУ".toLowerCase()).getNorm(),0);
//            normThesaurus.put(MorphoDictionary.get("уравнение".toLowerCase()).getNorm(),0);
//            normThesaurus.put(MorphoDictionary.get("дифференциальный".toLowerCase()).getNorm(),0);
//            normThesaurus.put(MorphoDictionary.get("область".toLowerCase()).getNorm(),0);
            //normThesaurus.put("условие ацикличность",0);

            for(String key : normThesaurus.keySet()){
                System.out.println(key);
            }

//            System.out.println(MorphoDictionary.get("ацикличности".toLowerCase()).getNorm());


//            //Поиск ключа в мапе
//            String testKey = "Невозмущенное движение ";
//            String tst[] = testKey.split(" ");
//            System.out.println(Thesaurus.containsKey(testKey));


            Integer mode = 0;

            if(mode == 1){
                File dir = new File(DIRNAME);
                for(File chld : dir.listFiles()){
                    System.out.println(chld.getName());
                    File file = new File(FILENAME2);
                    Document article = DocumentParser.parse(file);
                    searchForWords(article.getBlocks(), normThesaurus);
                }
                System.out.println("DONE");
            }
            else{
                File file = new File(FILENAME2);
                Document article = DocumentParser.parse(file);
                searchForWords(article.getBlocks(), normThesaurus);
            }



//






            //Iterator<String> terms = Thesaurus.iterator();
//            while(terms.hasNext()){
//                System.out.println(terms.next());
//            }

//            Map<String, String> hashMap = new HashMap<String, String>();
//            hashMap.put("0","KEK");
//            hashMap.put("1","PEK");
//            hashMap.put("0","KEK" + "K");
//            System.out.println(hashMap.size());
//            System.out.println(hashMap.containsKey("0"));
//            System.out.println(hashMap.containsValue("KEK"));
//            System.out.println(hashMap);
//            String str = "KEKK";
//            System.out.println(hashMap.containsValue(str));
//            System.out.println(hashMap.get("0"));


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