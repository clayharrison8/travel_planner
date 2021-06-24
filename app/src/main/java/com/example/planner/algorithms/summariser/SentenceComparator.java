package com.example.planner.algorithms.summariser;

import java.util.Comparator;

// Based on Text Summariser by Github user ajhalthor. Available at: https://github.com/ajhalthor/text-summarizer
public class SentenceComparator implements Comparator<Sentence>{
	// Ordering sentences by score
	@Override
	public int compare(Sentence obj1, Sentence obj2) {
		return Double.compare(obj2.score, obj1.score);
	}
}