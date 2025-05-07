package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import java.util.Map;

public class AsQuestion {
    public String question;
    public Map<String, String> choices;  // a, b, c, d
    public String answer;

    public AsQuestion() {
        // Required no-arg constructor for serialization
    }

    public AsQuestion(String question, Map<String, String> choices, String answer) {
        this.question = question;
        this.choices = choices;
        this.answer = answer;
    }
}
