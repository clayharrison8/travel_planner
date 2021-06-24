package com.example.planner.algorithms.summariser;

// Based on Text Summariser by Github user ajhalthor. Available at: https://github.com/ajhalthor/text-summarizer
class Sentence {
    // indicates the paragraph number with respect to the entire text.
    int paragraphNumber;
    // number indicates the sentence number with respect to the entire text.
    int number;
    // Every sentence will have a score that indicates it’s importance. This is initialized to 0.
    double score;
    // The number of words is computed by manually word tokenizing a sentence.
    int noOfWords;
    // Content of the sentence
    String value;

    Sentence(int number, String value, int paragraphNumber) {
        this.number = number;
        this.value = value;
        noOfWords = value.split("\\s+").length;
        score = 0.0;
        this.paragraphNumber = paragraphNumber;
    }
}