package com.github.smivle.common.text;

import com.github.stagirs.common.text.SentenceExtractor;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author pc
 */
public class SentenceExtractorTest {
    
    @Test
    public void extractTest(){
        SentenceExtractor tp = SentenceExtractor.get();
        List<String> list = tp.extract("общая психология \nАвтореферат \nдиооѳртации М В Ломоносов");
        assertEquals(list.size(), 3);
    }
}
