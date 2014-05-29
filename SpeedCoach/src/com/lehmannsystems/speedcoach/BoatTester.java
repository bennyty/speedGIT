package com.lehmannsystems.speedcoach;

public class BoatTester {
//TODO Delete this class once all others are complete

	public static void main(String[] args) {
		Boat entheos = new Boat("Entheos", "Ben");
		entheos.setRate(34);
		entheos.setSplit(123.4);
		String testString = ("The " + entheos.getName() + ", coxed by " + entheos.getCox() + 
				", is rowing a " + entheos.formatSplit(entheos.getRawSplit()) + " (" + entheos.getRawSplit() + 
				" raw seconds) at a " + entheos.getRate());
		System.out.println(testString);
		Boat four = new Boat("1st Four", "Charlie");
		four.setRate(45);
		four.setSplit(111.4);
		testString = ("The " + four.getName() + ", coxed by " + four.getCox() + 
				", is rowing a " + four.formatSplit(four.getRawSplit()) + " (" + four.getRawSplit() + 
				" raw seconds) at a " + four.getRate());
		System.out.println(testString);
	}

}
