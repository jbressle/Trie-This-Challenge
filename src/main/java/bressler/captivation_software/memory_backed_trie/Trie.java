package bressler.captivation_software.memory_backed_trie;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Trie data structure.
 *
 *  - https://en.wikipedia.org/wiki/Trie
 */
public class Trie {
    // MAX_LENGTH value is based off of this link as of 2017/10/17
    //  - https://en.wikipedia.org/wiki/Longest_word_in_English
    private static final int MAX_LENGTH = 2000;

    private final TrieNode root = new TrieNode();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    /**
     * Add word to Trie. Normalizes input to lower case.
     *
     * Does not allow null or empty words. Does not allow not alphabetic characters. Maximum length of word is 2000 characters.
     *
     * @param word
     * @return true if added, false if already contained in Trie
     */
    public boolean add(@Nonnull String word){
        ImmutableList<Character> chars = parseAndValidate(word);
        Iterator<Character> iter = chars.listIterator();

        writeLock.lock();
        try {
            return root.addWordSuffix(iter);
        }finally {
            writeLock.unlock();
        }
    }

    /**
     *
     * @param word
     * @return true if word is contained in the Trie, false if not
     */
    public boolean contains(@Nullable String word){
        if(isNullOrEmpty(word) || word.length() > MAX_LENGTH){return false;}

        ImmutableList<Character> chars = Lists.charactersOf(word);
        Iterator<Character> iter = chars.listIterator();
        readLock.lock();
        try {
            return root.containsWord(iter);
        }finally {
            readLock.unlock();
        }
    }

    /**
     *
     * @param prefix prefix to search and match results against
     * @return word matches contained in the Trie
     */
    public List<String> search(@Nullable String prefix){
        if(null == prefix || prefix.length() > MAX_LENGTH){return new ArrayList<>();}

        ImmutableList<Character> chars = Lists.charactersOf(prefix);
        Iterator<Character> iter = chars.listIterator();
        List<String> results = new ArrayList<>();
        readLock.lock();
        try {
            TrieNode found = root.findMatchingNode(iter);
            if(null != found){
                found.buildResults(results, prefix);
            }
            return results;
        }finally {
            readLock.unlock();
        }
    }

    private static ImmutableList<Character> parseAndValidate(String word){
        if(isNullOrEmpty(word)){
            throw new IllegalArgumentException("null or empty word is not allowed");
        }

        int len = word.length();
        if(len > MAX_LENGTH){
            throw new IllegalArgumentException("word with length more than "+MAX_LENGTH+" is not allowed");
        }

        //normalize
        word = word.toLowerCase();

        ImmutableList.Builder<Character> builder = ImmutableList.builder();
        for(int i = 0; i < len; i++){
            Character c = word.charAt(i);
            if(!Character.isAlphabetic(c)){
                throw new IllegalArgumentException("character "+c+" is not alphabetic, only alphabetic characters are allowed");
            }
            builder.add(c);
        }
        return builder.build();
    }

    private static boolean isNullOrEmpty(String word){
        return null == word || "".equals(word);
    }

}
