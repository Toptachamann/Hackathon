package test.back.code;


import test.back.antlr.cpp.CPP14Lexer;
import test.back.antlr.csharp.CSharpLexer;
import test.back.antlr.java.Java9Lexer;
import com.sun.corba.se.impl.oa.toa.TOA;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.apache.commons.text.similarity.CosineDistance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SimilarityFinder {
    static class LangSpec {
        int intLiteral;
        int floatLiteral;
        int charLiteral;
        int stringLiteral;
        int comment;
        int lineComment;
        int maxId;

        public LangSpec(int intLiteral, int floatLiteral,
                        int charLiteral, int stringLiteral,
                        int maxId, int comment, int lineComment) {
            this.intLiteral = intLiteral;
            this.floatLiteral = floatLiteral;
            this.charLiteral = charLiteral;
            this.stringLiteral = stringLiteral;
            this.maxId = maxId;
            this.comment = comment;
            this.lineComment = lineComment;
        }
    }

    public enum Lang {
        JAVA, CPP, CSHARP,
    }

    private static LangSpec javaSpec = new LangSpec(62, 63, 65, 66, 120, 117, 118);
    private static LangSpec cppSpec = new LangSpec(134, 141, 140, 142, 151, 149, 150);
    private static LangSpec csharpSpec = new LangSpec(114, 116, 117, 118, 195, 4, 5);


    private Map<String, Integer> stringMap = new HashMap<>();
    private Map<String, Integer> intMap = new HashMap<>();
    private Map<String, Integer> floatMap = new HashMap<>();
    private Map<String, Integer> charMap = new HashMap<>();

    private List<? extends Token> tokens1;
    private List<? extends Token> tokens2;
    private List<Integer> encoding1;
    private List<Integer> encoding2;

    private LangSpec getLangSpec(Lang lang) {
        switch (lang) {
            case JAVA:
                return javaSpec;
            case CPP:
                return cppSpec;
            case CSHARP:
                return csharpSpec;
            default:
                throw new RuntimeException();
        }
    }

    private MatchingBlock decode(EqualsBlock block) {
        String first = decodeBlock(block.start1, block.finish1, tokens1);
        String second = decodeBlock(block.start2, block.finish2, tokens2);
        return new MatchingBlock(first, second);
    }

    private String decodeBlock(int start, int end, List<? extends Token> tokens) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i <= end; i++) {
            builder.append(tokens.get(i).getText()).append(" ");
        }
        return builder.toString();
    }


    public double findSimilarity(String code1, String code2, Lang lang, List<MatchingBlock> result) {
        LangSpec spec = getLangSpec(lang);

        tokens1 = findTokens(code1, spec, lang);
        tokens2 = findTokens(code2, spec, lang);
        Map<String, Integer> encodingMap = fillMaps(tokens1, tokens2, spec);
        encoding1 = getEncoding(tokens1, encodingMap);
        encoding2 = getEncoding(tokens2, encodingMap);
        System.out.println(encoding1);
        System.out.println(encoding2);

        EncodedSequenceMatcher matcher = new EncodedSequenceMatcher(encoding1, encoding2);
        List<EqualsBlock> blocks = matcher.getRate();
        System.out.println("Blocks = " + blocks);
        blocks.stream().map(this::decode).forEach(result::add);

        int plagLength = blocks.stream().mapToInt(EqualsBlock::lengthSecond).sum();
        return (double) plagLength / encoding2.size();
    }

    public Token getFirstToken(int pos) {
        return tokens1.get(pos);
    }

    public Token getSecondToken(int pos) {
        return tokens2.get(pos);
    }

    public int getFirstSize() {
        return tokens1.size();
    }

    public int getSecondSize() {
        return tokens2.size();
    }

    private Map<String, Integer> fillMaps(List<? extends Token> tokens1, List<? extends Token> tokens2, LangSpec spec) {
        int maxId = spec.maxId;
        maxId = fillMap(tokens1, spec.intLiteral, maxId, intMap);
        maxId = fillMap(tokens2, spec.intLiteral, maxId, intMap);
        maxId = fillMap(tokens1, spec.floatLiteral, maxId, floatMap);
        maxId = fillMap(tokens2, spec.floatLiteral, maxId, floatMap);
        maxId = fillMap(tokens1, spec.charLiteral, maxId, charMap);
        maxId = fillMap(tokens2, spec.charLiteral, maxId, charMap);
        maxId = fillMap(tokens1, spec.stringLiteral, maxId, stringMap);
        maxId = fillMap(tokens2, spec.stringLiteral, maxId, stringMap);
        Map<String, Integer> encodingMap = new HashMap<>();
        encodingMap.putAll(intMap);
        encodingMap.putAll(floatMap);
        encodingMap.putAll(charMap);
        encodingMap.putAll(stringMap);
        for (Token token : tokens1) {
            if (token.getType() != spec.intLiteral && token.getType() != spec.floatLiteral && token.getType() != spec.charLiteral && token.getType() != spec.stringLiteral) {
                encodingMap.put(token.getText(), token.getType());
            }
        }
        for (Token token : tokens2) {
            if (token.getType() != spec.intLiteral && token.getType() != spec.floatLiteral && token.getType() != spec.charLiteral && token.getType() != spec.stringLiteral) {
                encodingMap.put(token.getText(), token.getType());
            }
        }
        return encodingMap;
    }

    private int fillMap(List<? extends Token> tokens, int tokenType, int maxId, Map<String, Integer> output) {
        for (Token token : tokens) {
            if (token.getType() == tokenType) {
                if (!output.containsKey(token.getText())) {
                    output.put(token.getText(), ++maxId);
                }
            }
        }
        return maxId;
    }

    private List<? extends Token> findTokens(String code, LangSpec spec, Lang lang) {
        CharStream input = CharStreams.fromString(code);
        Lexer lexer;
        switch (lang) {
            case JAVA:
                lexer = new Java9Lexer(input);
                break;
            case CPP:
                lexer = new CPP14Lexer(input);
                break;
            case CSHARP:
                lexer = new CSharpLexer(input);
                break;
            default:
                throw new RuntimeException();
        }
        List<? extends Token> tokens = lexer.getAllTokens();
        return tokens.stream().filter(t -> t.getType() != spec.lineComment && t.getType() != spec.comment).collect(Collectors.toList());
    }

    private <T, S> void putAll(Map<T, S> source, Map<S, T> target) {
        for (Map.Entry<T, S> entry : source.entrySet()) {
            target.put(entry.getValue(), entry.getKey());
        }
    }

    private List<Integer> getEncoding(List<? extends Token> tokens, Map<String, Integer> encodingMap) {
        List<Integer> result = new ArrayList<>();
        Map<Integer, String> decodingMap = new HashMap<>();
        putAll(encodingMap, decodingMap);

        for (Token token : tokens) {
            String tokenText = token.getText();
            result.add(encodingMap.get(tokenText));
        }
//        StringBuilder builder = new StringBuilder();
//        result.forEach(val -> builder.append(val).append(" - > ").append(decodingMap.get(val)).append(", "));
//        System.out.println(builder.toString());
//        System.out.flush();
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        SimilarityFinder finder = new SimilarityFinder();
        String firstPath = "/home/timofey/Documents/All_info/Java_Projects/HackathonProject/src/main/resources/base/code1.txt";
        String secondPath = "/home/timofey/Documents/All_info/Java_Projects/HackathonProject/src/main/resources/input/code1.txt";

        String code1 = new Scanner(new File(firstPath)).useDelimiter("\\Z").next();
        String code2 = new Scanner(new File(secondPath)).useDelimiter("\\Z").next();
        List<MatchingBlock> blocks = new ArrayList<>();
        double coef = finder.findSimilarity(code1, code2, Lang.CSHARP, blocks);
        System.out.println("Matching blocks:\n" + blocks);
        System.out.println("Coef = " + coef);
//        for (int i = 0; i < finder.getFirstSize(); i++) {
//            System.out.println((i + 1) + ". " + finder.getFirstToken(i));
//        }

    }
}
