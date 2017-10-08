package bressler.captivation_software.memory_backed_trie;


import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;

/**
 * Created by jbressle on 10/7/17.
 */
public class TrieTest {

    /**

     Requirements

     Create an implementation of a Trie, implementing the algorithms required to support the following interface:

     add(word) => adds word to the trie, returning true if the word was successfully added and false if the word was already present.
     contains(word) => returns true if the trie contains word and false if it does not.
     search(prefix) => returns the list of all words in the trie that begin with prefix.
     Guidelines

     The trie implementation only needs to support lowercase words

     */
    @Test
    public void basic(){
        Trie trie = new Trie();
        // add(word) => adds word to the trie, returning true if the word was successfully added and false if the word was already present.
        Assert.assertTrue(trie.add("cat"));
        Assert.assertFalse(trie.add("cat"));

        // contains(word) => returns true if the trie contains word and false if it does not.
        Assert.assertFalse(trie.contains("cab"));
        trie.add("cab");
        Assert.assertTrue(trie.contains("cab"));

        List<String> found;


        // search(prefix) => returns the list of all words in the trie that begin with prefix.
        found = trie.search("");
        Assert.assertEquals(2, found.size());

        trie.add("cabbage");
        trie.add("cattle");
        trie.add("canada");
        trie.add("canadian");

        found = trie.search("");
        Assert.assertEquals(6, found.size());

        found = trie.search("cabbage");
        assertThat("list equal order agnostic",
                found,
                containsInAnyOrder("cabbage")
        );

        found = trie.search("can");
        assertThat("list equal order agnostic",
                found,
                containsInAnyOrder("canada", "canadian")
        );

        trie.add("do");
        trie.add("dog");
        trie.add("dow");
        trie.add("down");

        found = trie.search("d");
        assertThat("list equal order agnostic",
                found,
                containsInAnyOrder("do", "dog", "dow", "down")
        );

        trie.add("downstairs");
        found = trie.search("d");
        assertThat("list equal order agnostic",
                found,
                containsInAnyOrder("do", "dog", "dow", "down", "downstairs")
        );

    }

    @Test
    public void testBadInputs(){
        // Assumption on my part that it is OK to consider these as bad inputs
        String[] badInputs = new String[]{
          null, "", "abc1z", "asdfafdal;kfdas", "hello world", bigString()
        };

        Trie trie = new Trie();
        for(int i = 0; i < badInputs.length; i++){
            try{
                trie.add(badInputs[i]);
                Assert.fail("expected word '"+badInputs[i]+"' to be bad input");
            }catch(Exception e){
                //expected
            }
        }

    }

    private static String bigString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 200; i++){
            sb.append("abcdefghijklmnop");
        }
        return sb.toString();
    }
}
