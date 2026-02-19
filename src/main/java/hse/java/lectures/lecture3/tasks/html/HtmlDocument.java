package hse.java.lectures.lecture3.tasks.html;

//import hse.java.lectures.lecture3.tasks.atm.CannotDispenseException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;



class Helper {
    HashMap<String , Integer> countTags = new HashMap<>(Map.of(
            "html" , 0 ,
            "head" , 0 ,
            "body" ,0 ,
            "div" ,0 ,
            "p" , 0
            ) );


    public Boolean headHasAlreadyBeen = false ;



    public Boolean checkInsideText(String tag) throws  UnsupportedTagException {
        String tagNoCarets;
        if (tag.startsWith("/")) {
            tagNoCarets = tag.substring(1) ;
        }
        else {
            tagNoCarets= tag ;
        }

        for ( TagNames name : TagNames.values() ) {
//            System.out.println(name.getTagName()+ " vs " + tagNoCarets);

            if (Objects.equals(name.getTagName(), tagNoCarets)) {
                countTags.put(tagNoCarets , countTags.get(tagNoCarets) + 1 ) ;
                return true;
            }
        }
        throw new UnsupportedTagException("не нашлось пары ( ") ;
    }
    public Boolean checkCountTags() {
        return  countTags.get("html") == 2 &&
                countTags.get("body") <=2  &&
                countTags.get("head") <=2 ;
    }

}

public class HtmlDocument {


    public HtmlDocument(Path filePath) {
        String content = readFile(filePath);
        validate(content);
    }

    public HtmlDocument(String filePath) {
        this(Path.of(filePath));
    }

    private String readFile(Path filePath) {
        try {
            return Files.readString(filePath, StandardCharsets.UTF_8); //предназначен для быстрого чтения всего содержимого текстового файла в одну строку (String) с использованием кодировки UTF-8.
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    private void validate(String content){
        Helper helper = new Helper();
        Stack<String> stack = new Stack<>();
        for (int i = 0 ; i < content.length() ;i++) {

            if (content.charAt(i) == '<' && content.charAt(i+1) != '/') {
                i++;
                StringBuilder word = new StringBuilder();
                boolean flagAtributes = false;
                while(content.charAt(i) != '>') {
                    if (flagAtributes) {
                        i++;
                        continue;
                    }
                    if ( content.charAt(i) == ' ') {
                        i++;
                        flagAtributes = true;
                        continue;
                    }
                    word.append(content.charAt(i));
                    i++;
                }
                String normalWord = String.valueOf(word).toLowerCase();
                if (helper.checkInsideText(normalWord)) {
                    System.out.println("normal word is " + normalWord + " flag is" + helper.headHasAlreadyBeen);


                    stack.push(normalWord);
                }
            } else if ( content.charAt(i) == '<' && content.charAt(i+1) == '/') {
                i = i  +2 ;
                StringBuilder word = new StringBuilder();
                while(content.charAt(i) != '>') {
                    word.append(content.charAt(i)) ;
                    i++;
                }
                String normalWord = String.valueOf(word).toLowerCase();
                if (helper.checkInsideText(normalWord)) {
                    if (stack.empty()) {
                        throw new UnexpectedClosingTagException("нет парного элемента") ;
                    }
                    String element = stack.pop();
//                    System.out.println(" word : " + word);
                    if (! Objects.equals(element, normalWord)) {
                        throw new MismatchedClosingTagException("не тот тэг использован ") ;
                    }
                    if(normalWord.equals("head")) {
                        helper.headHasAlreadyBeen = true;
                    }
                    if (normalWord.equals("body") && !helper.headHasAlreadyBeen) {
                        throw  new InvalidStructureException("нарушена структура head --> body") ;
                    }
                }
            }
        }


        if (!stack.empty()) {
            throw new UnclosedTagException("еще остались тэги ") ;
        }
        if (!helper.checkCountTags()) {
            throw  new InvalidStructureException("проеб с правилами ") ;
        }
//        System.out.println(helper.countTags);
        if (! Objects.equals(helper.countTags.get("body"), helper.countTags.get(("head")))) {
            throw  new InvalidStructureException("kizaru") ;
        }


        //         a a' b c c' b'
        // stack :



    }

}
