package bressler.captivation_software.memory_backed_trie;

import java.util.*;

/**
 * Nodes within the trie!
 */
class TrieNode {
    // True if the particular node represents a word, and not just part of a bigger word
    private boolean isWord = false;
    private Map<Character, TrieNode> children = new HashMap<>();

    TrieNode(){}

    /**
     * Add suffix of length one or more to this node.
     *
     * @param suffix suffix of length one or more
     * @return true if added, false if already present
     */
    boolean addWordSuffix(Iterator<Character> suffix)
    {
        if(!suffix.hasNext()){throw new IllegalArgumentException("should never have an empty suffix");}

        Character nextChar = suffix.next();
        TrieNode child = children.get(nextChar);
        boolean createdChild = false;
        if(null == child){
            child = new TrieNode();
            children.put(nextChar, child);
            createdChild = true;
        }

        if(!suffix.hasNext()){
            child.isWord = true;
            return createdChild;
        }

        return child.addWordSuffix(suffix);
    }

    /**
     *
     * @param charIter representation of word
     * @return true if this word is in the TrieNode
     */
    boolean containsWord(Iterator<Character> charIter){
        if(!charIter.hasNext()){
            return this.isWord;
        }

        Character nextChar = charIter.next();
        TrieNode child = children.get(nextChar);
        if(null == child){
            return false;
        }

        return child.containsWord(charIter);
    }

    /**
     *
     * @param charIter representation of prefix
     * @return sub tree that matches the prefix, null if no match
     */
    TrieNode findMatchingNode(Iterator<Character> charIter){
        if(!charIter.hasNext()){
            return this;
        }

        Character nextChar = charIter.next();
        TrieNode child = children.get(nextChar);
        if(null == child){
            return null;
        }

        return child.findMatchingNode(charIter);
    }

    /**
     *
     * @param results caller provided array to collect results in
     * @param prefix prefix to combine with all matching suffixes contained in node
     */
    void buildResults(List<String> results, String prefix){
        if(this.isWord){
            results.add(prefix);
        }

        List<Character> prefixList = new ArrayList<>();
        int len = prefix.length();
        for(int i = 0; i < len; i++){
            prefixList.add(prefix.charAt(i));
        }

        buildResults(results, prefixList);
    }

    /**
     *
     * @param results caller provided array to collect results in
     * @param prefix prefix to combine with all matching suffixes contained in node
     */
    private void buildResults(List<String> results, List<Character> prefix){
        for(Map.Entry<Character, TrieNode> entry : children.entrySet()){
            Character curChar = entry.getKey();
            TrieNode curChild  = entry.getValue();

            // push character on stack
            prefix.add(curChar);

            if(curChild.isWord){
                // "collect" result
                results.add(fromCharList(prefix));
            }

            // recursive
            curChild.buildResults(results, prefix);

            // "pop" character off stack
            prefix.remove(prefix.size()-1);
        }
    }

    // Convert character list into String
    private static String fromCharList(List<Character> charList){
        StringBuilder builder = new StringBuilder(charList.size());
        for(Character ch: charList)
        {
            builder.append(ch);
        }
        return builder.toString();
    }

}
