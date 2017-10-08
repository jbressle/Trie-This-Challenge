package bressler.captivation_software;

import bressler.captivation_software.memory_backed_trie.Trie;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

/**
 * Expose Trie data structure as web service
 */
@Controller
public class TrieController {
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final Trie TRIE = new Trie();


    @RequestMapping(
            value = "/trie/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public void add(
            @RequestBody String json
    ) throws Exception {
        JsonNode jsonObj = JSON.reader().readTree(json);
        if(!(jsonObj instanceof ArrayNode)){
            throw new IllegalArgumentException("expected input to be json array");
        }
        Iterator<JsonNode> elements = jsonObj.getElements();
        while(elements.hasNext()){
            String nextWord = elements.next().asText();
            TRIE.add(nextWord);
        }
    }

    @RequestMapping(
            value = "/trie/find",
            method = RequestMethod.GET
    )
    @ResponseBody
    public String find(
            @RequestParam(value = "search") String search
    ) throws Exception {
        List<String> words = TRIE.search(search);
        ArrayNode arr = JSON.createArrayNode();
        for(String word : words){
            arr.add(word);
        }
        return arr.toString();
    }
}
