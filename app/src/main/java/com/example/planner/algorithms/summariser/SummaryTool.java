package com.example.planner.algorithms.summariser;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

// Based on Text Summariser by Github user ajhalthor. Available at: https://github.com/ajhalthor/text-summarizer
public class SummaryTool {
    private FileInputStream in;
    private ArrayList<Sentence> sentences, contentSummary;
    private ArrayList<Paragraph> paragraphs;
    private int noOfSentences, noOfParagraphs;

    private double[][] intersectionMatrix;

    // Writing string from arraylist to file
    private void writeToFile(String data, Context context) {
        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    private void init(Context context) {

        sentences = new ArrayList<>();
        paragraphs = new ArrayList<>();
        contentSummary = new ArrayList<>();
        noOfSentences = 0;
        noOfParagraphs = 0;

        try {
            in = context.openFileInput("config.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Gets the sentences from the entire passage */
    private void extractSentenceFromContext() {
        int nextChar, j;
        int prevChar = -1;

        try {

            // Read entire file
            while ((nextChar = in.read()) != -1) {
                j = 0;
                // Assigning temp with a size of 100000
                char[] temp = new char[100000];
                // Find end of a sentence
                while ((char) nextChar != '.') {
                    // Creating the sentence by adding individual characters
                    temp[j] = (char) nextChar;

                    // Finding end of the file
                    if ((nextChar = in.read()) == -1) {
                        break;
                    }
                    // Finding end of the paragraph
                    if ((char) nextChar == '\n' && (char) prevChar == '\n') {
                        noOfParagraphs++;
                    }

                    j++;
                    prevChar++;
                }

                // Adding current sentence to sentence ArrayList
                sentences.add(new Sentence(noOfSentences, (new String(temp)).trim(), noOfParagraphs));
                noOfSentences++;
                prevChar++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Creates new paragraph using the string arraylist
    private void groupSentencesIntoParagraphs() {
        int paraNum = 0;
        Paragraph paragraph = new Paragraph(0);

        // Loops through sentences
        for (int i = 0; i < noOfSentences; i++) {
            // Checks if sentence is in new paragraph
            if (sentences.get(i).paragraphNumber != paraNum) {
                // Adds paragraph object
                paragraphs.add(paragraph);
                paraNum++;
                // Creates new paragraph
                paragraph = new Paragraph(paraNum);
            }
            // Puts sentences ArrayList into paragraph
            paragraph.sentences.add(sentences.get(i));
        }


        paragraphs.add(paragraph);
    }

    // Finds number of common words between the two sentences
    private double noOfCommonWords(Sentence sentence1, Sentence sentence2) {
        double commonCount = 0;

        // Getting different words from both sentences
        for (String str1Word : sentence1.value.split("\\s+")) {
            for (String str2Word : sentence2.value.split("\\s+")) {
                // Checking if there are any words that occur in both sentences
                if (str1Word.compareToIgnoreCase(str2Word) == 0) {
                    commonCount++;
                }
            }
        }

        return commonCount;
    }

    // Comparing every sentence with each other and generating score
    private void createIntersectionMatrix() {
        // Creating matrix using the sentence count in order to compare every sentence
        intersectionMatrix = new double[noOfSentences][noOfSentences];
        for (int i = 0; i < noOfSentences; i++) {
            for (int j = 0; j < noOfSentences; j++) {

                if (i <= j) {
                    Sentence str1 = sentences.get(i);
                    Sentence str2 = sentences.get(j);
                    // Checking common words between two sentences and normalising by using the average length of both sentences
                    intersectionMatrix[i][j] = noOfCommonWords(str1, str2) / ((double) (str1.noOfWords + str2.noOfWords) / 2);
                } else {
                    intersectionMatrix[i][j] = intersectionMatrix[j][i];
                }

            }
        }
    }

    // Calculating the score for each sentence
    private void createDictionary() {
        for (int i = 0; i < noOfSentences; i++) {
            double score = 0;
            for (int j = 0; j < noOfSentences; j++) {
                // Adding total score for each row of matrix
                score += intersectionMatrix[i][j];
            }

            sentences.get(i).score = score;
        }
    }


    // Choosing the best sentences according to their score
    private String createSummary() {
        String summary = "";
        StringBuilder summary1 = new StringBuilder();

        for (int j = 0; j <= noOfParagraphs; j++) {
            int primary_set = paragraphs.get(j).sentences.size() / 5;

            //Sort based on score (importance)
            Collections.sort(paragraphs.get(j).sentences, new SentenceComparator());

            for (int i = 0; i <= primary_set; i++) {
                contentSummary.add(paragraphs.get(j).sentences.get(i));
            }

        }

        // Make sure sentences are in correct order to maintain context
        Collections.sort(contentSummary, new SentenceComparatorForSummary());

        // Concatenating different sentences in contentSummary
        for (Sentence sentence : contentSummary) {
            summary1.append(sentence.value);
            summary1.append(". ");
            summary = summary1.toString();
        }

        return summary;
    }

    public String summarise(String content, Context context) {

        writeToFile(content, context);
        init(context);

        extractSentenceFromContext();
        groupSentencesIntoParagraphs();
        createIntersectionMatrix();
        createDictionary();

        return createSummary();
    }

}