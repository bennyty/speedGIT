package com.lehmannsystems.speedcoach;

public class BoatTester {
	//TODO Delete this class once all others are complete

		public static void main(String[] args) {
			Boat entheos = new Boat("Mike", 1);
			entheos.setRate(34);
			entheos.setSplitSeconds(123.4);
			String testString = (", coxed by " + entheos.getCox() + 
					", is rowing a " + entheos.formatSplit(entheos.getRawSplit()) + " (" + entheos.getRawSplit() + 
					" raw seconds) at a " + entheos.getRate());
			System.out.println(testString);
			
		}

	}