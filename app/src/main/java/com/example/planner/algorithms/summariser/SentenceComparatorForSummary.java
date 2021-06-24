package com.example.planner.algorithms.summariser;

import java.util.Comparator;

// Based on Text Summariser by Github user ajhalthor. Available at: https://github.com/ajhalthor/text-summarizer
public class SentenceComparatorForSummary implements Comparator<Sentence>{
	// Putting sentence back into original order
	@Override
	public int compare(Sentence obj1, Sentence obj2) {
		return Integer.compare(obj1.number, obj2.number);
	}
}