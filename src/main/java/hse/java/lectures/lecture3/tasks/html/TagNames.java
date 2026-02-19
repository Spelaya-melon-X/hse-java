package hse.java.lectures.lecture3.tasks.html;

import hse.java.lectures.lecture3.tasks.atm.CannotDispenseException;

import java.util.HashMap;
import java.util.Objects;

public enum TagNames {
    HTML("html") ,
    HEAD("head") ,
    BODY("body") ,
    DIV("div") ,
    P("p") ;



    private String tagName ;

    TagNames(String tagName) {
        this.tagName = tagName;
    }

    // control + return - ахеренная команда
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }




}
