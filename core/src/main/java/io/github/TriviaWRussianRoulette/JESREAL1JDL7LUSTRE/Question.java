package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.utils.ObjectMap;

public class Question {
    private String question; // Changed from static to instance variable
    private ObjectMap<String, String> choices;  // Store choices in a map
    private String answer;

    // Default constructor for JSON deserialization
    public Question() {
        choices = new ObjectMap<>();
    }

    // Constructor with question data
    public Question(String question, ObjectMap<String, String> choices, String answer) {
        this.question = question; // Changed from static to instance
        this.choices = choices;
        this.answer = answer;
    }

    // Getter and setter for question
    public String getQuestion() { // Changed from static to instance method
        return question;
    }

    public void setQuestion(String question) {
        this.question = question; // Changed from static to instance
    }

    // Getter and setter for choices
    public ObjectMap<String, String> getChoices() {
        return choices;
    }

    public void setChoices(ObjectMap<String, String> choices) {
        this.choices = choices;
    }

    // Getter and setter for the answer
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
