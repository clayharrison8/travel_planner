package com.example.planner.algorithms.summariser;

import java.util.ArrayList;

// Based on Text Summariser by Github user ajhalthor. Available at: https://github.com/ajhalthor/text-summarizer
class Paragraph{
	int number;
	ArrayList<Sentence> sentences;

	Paragraph(int number){
		this.number = number;
		sentences = new ArrayList<>();
	}
}