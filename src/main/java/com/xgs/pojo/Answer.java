package com.xgs.pojo;

import java.util.ArrayList;
import lombok.Data;

/**
 * @author LENOVO
 */
@Data
public class Answer {

    String AskContent;
    String AnswerContent;

    public String getAskContent() {
        return AskContent;
    }

    public void setAskContent(String askContent) {
        AskContent = askContent;
    }

    public String getAnswerContent() {
        return AnswerContent;
    }

    public void setAnswerContent(String answerContent) {
        AnswerContent = answerContent;
    }
}
