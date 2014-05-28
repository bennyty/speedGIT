package com.lehmannsystems.speedcoach;

public class BoatTester {

	public static void main(String[] args) {
		Boat entheos = new Boat("Entheos", "Ben");
		entheos.setRate(34);
		entheos.setSplit(123.4);
		String testString = ("The " + entheos.getName() + ", coxed by " + entheos.getCox() + ", is rowing a " + 
				entheos.getFormattedSplit() + " (" + entheos.getRawSplit() + 
				" raw seconds) at a " + entheos.getRate());
		System.out.print(testString);
	}

}
