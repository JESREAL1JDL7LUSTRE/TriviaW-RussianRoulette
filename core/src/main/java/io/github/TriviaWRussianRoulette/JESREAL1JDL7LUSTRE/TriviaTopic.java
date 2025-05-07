package io.github.TriviaWRussianRoulette.JESREAL1JDL7LUSTRE;

import com.badlogic.gdx.utils.Array;

public class TriviaTopic {
    private String topic;  // File name of the topic (used to identify the topic)
    private Array<Question> questions;  // List of questions for this topic

    // Default constructor needed for JSON deserialization
    public TriviaTopic() {
        questions = new Array<>();
    }

    // Constructor with topic name
    public TriviaTopic(String topic) {
        this.topic = topic;
        this.questions = new Array<>();
    }

    // Getter and setter for topic name
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    // Getter and setter for questions
    public Array<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Array<Question> questions) {
        this.questions = questions;
    }

    // Add a question to the list
    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void clearQuestions() {
        questions.clear();
    }

    public int getQuestionCount() {
        return questions.size;
    }

    public Question getQuestion(int i) {
        return questions.get(i);
    }
}
