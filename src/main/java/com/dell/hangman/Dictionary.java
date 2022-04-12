package com.dell.hangman;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Component
public class Dictionary {
    private List<String> words;
    private Random randomGenerator;

    @PostConstruct
    private void loadWords() throws Exception {
        words = new ArrayList<>();
        randomGenerator = new Random();

        String resourcesPath = "gameAvaiableWords.xml";
        InputStream stream = getClass().getClassLoader().getResourceAsStream(resourcesPath);

        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(stream);

        NodeList nodeList = document.getElementsByTagName("word");

        Stream.iterate(0, i -> i + 1)
                .limit (nodeList.getLength())
                .map (nodeList::item)
                .forEach(this::accept);

    }

    private void accept(Node node) {
        words.add(node.getFirstChild().getNodeValue());
    }

    public String pickWord() {
        int n = randomGenerator.nextInt(words.size());
        return words.get(n);
    }
}
