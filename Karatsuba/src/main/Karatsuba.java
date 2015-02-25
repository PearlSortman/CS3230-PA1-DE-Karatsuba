/****************************************************************************************
 * 
 * Assignment: PA1-DE
 * Name: Pearl Sortman
 * Matric Number: A0135005
 * Course: CS3230 Design & Analysis of Algorithms
 * 
 * Task D (60 points)
 * Numbers in the input are non-negative integers, given in decimal form, that is B=10. 
 * Length of V and M is at least 1 and at most 10005 digits. 
 * Total length of all numbers in all T test cases is less than 20010 digits. 
 *
 * Task E (40 points)
 * Numbers in the input are non-negative integers, given in decimal form, that is B=10. 
 * Length of V and M is at least 1 and at most 20005 digits. 
 * Total length of all numbers in all T test cases is less than 40010 digits. 
 * 
 * Constraints: 
 * 		Memory: 256M 
 * 		Running time: 2 seconds per T test cases
 * 
 * Sample input:
 * 4 	< number of test cases (0 < numTestCases > 21)
 * 10 		< base (ALWAYS 10)
 * 1234 	< velocity
 * 98 		< mass
 * 10 				< base (ALWAYS 10)
 * 7 				< velocity
 * 987754 			< mass
 * 10							< base (ALWAYS 10)
 * 9484857578438355666555 		< velocity
 * 23445879438579345305843092 	< mass
 * 10 				< base (ALWAYS 10)
 * 0 				< velocity
 * 12393948545757 	< mass
 * 
 * Sample output:
 * 120932
 * 6914278
 * 222380827276161322987671196928949534929902188060
 * 0
 * 
 **********************************************************************************************/

package main;

import java.io.*;
import java.util.*;

public class Karatsuba {

	public static final int MAX_DIGITS = 20010;
	public static final int CUT_OFF = 1000;
	public static final int BASE = 10;
	public static int[] momentumResultArray;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		PrintWriter printWriter = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(System.out)));

		int[] velocityArray = new int[MAX_DIGITS + 1];
		int[] massArray = new int[MAX_DIGITS + 1];
		int numTestCases = scanner.nextInt();

		for (int i = 1; i <= numTestCases; ++i) {
			scanner.nextInt(); // BASE already defined as 10
			scanner.nextLine();
			String velocityString = scanner.nextLine();
			String massString = scanner.nextLine();

			int velocityFixedPoint = scanArray(velocityString, velocityArray);
			int massFixedPoint = scanArray(massString, massArray);

			momentumResultArray = new int[MAX_DIGITS * 2];
			int resultLength = karatsubaMultiply(velocityArray, massArray);
			String output = resultToString(momentumResultArray, resultLength,
					velocityFixedPoint + massFixedPoint);

			printWriter.write(trimZeros(output));
			printWriter.write("\n");
		}
		printWriter.close(); // do not forget to use this
	}

	private static int karatsubaMultiply(int[] velocityArray, int[] massArray) {
		// base case:
		if (velocityArray.length < CUT_OFF && massArray.length < CUT_OFF) {
			return multArrays(velocityArray, massArray); // quadratic long
		} else {
			result = 
			return 0;
		}

		// splitting by halves:
		// R = max(length of X, length of Y) / 2;
		// HighX, LowX = split X at R;
		// HighY, LowY = split Y at R;

		// recursive calls: 3 times:
		// Z0 = karatsubaMultiply(LowX, LowY);
		// Z2 = karatsubaMultiply(HighX, HighY);
		// Z1 = karatsubaMultiply(LowX+HIghX, LowY+HighY);

		// adding and subtracting:
		// Res = Z2 * B^(2*R) + (Z1-Z2-Z0)*B^R + Z0

		// return:
		return 0; // size of result as int
	}

	private static int multArrays(int[] velocityArray, int[] massArray) {
		int sizeVelocity = velocityArray[velocityArray.length - 1];
		int sizeMass = massArray[massArray.length - 1];
		int i = 0;
		int j = 0;

		for (i = 0; i < sizeMass; i++) {
			int carry = 0;
			for (j = 0; j < sizeVelocity; j++) {
				momentumResultArray[i + j] += velocityArray[j] * massArray[i]
						+ carry;
				carry = momentumResultArray[i + j] / BASE;
				momentumResultArray[i + j] %= BASE;
			}
			momentumResultArray[i + j] += carry;
		}
		return i + j; // size of result
	}

	private static String resultToString(int[] resultArray, int sizeResult,
			int fixedPoint) {
		StringBuilder resultString = new StringBuilder(sizeResult + 1);
		for (int i = sizeResult - 1; i >= 0; i--) {
			resultString.append(toDigit(resultArray[i]));
			if (i == fixedPoint) {
				resultString.append('.');
			}
		}
		return resultString.toString();
	}

	private static int scanArray(String inputString, int[] inputArray) {
		int digitCounter = 0;
		int fixedPoint = 0;

		for (int i = inputString.length() - 1; i >= 0; i--) {
			if (inputString.charAt(i) == '.') {
				fixedPoint = digitCounter;
			} else {
				inputArray[digitCounter] = parseDigit(inputString.charAt(i));
				digitCounter++;
			}
		}
		inputArray[inputArray.length - 1] = digitCounter; // store size
		return fixedPoint;
	}

	private static String trimZeros(String input) {
		int left = 0;
		int right = input.length() - 1;
		int fixedPoint = input.indexOf('.');
		if (fixedPoint == -1) {
			fixedPoint = input.length();
		}

		while (left < fixedPoint - 1) {
			if (input.charAt(left) != '0')
				break;
			left++;
		}

		while (right >= fixedPoint) {
			if (input.charAt(right) != '0') {
				if (input.charAt(right) == '.')
					right--;
				break;
			}
			right--;
		}

		if (left >= fixedPoint)
			return "0" + input.substring(left, right + 1);
		return input.substring(left, right + 1);
	}

	private static int parseDigit(char c) {
		if (c <= '9') {
			return c - '0';
		}
		return c - 'A' + 10;
	}

	private static char toDigit(int digit) {
		if (digit <= 9) {
			return (char) (digit + '0');
		}
		return (char) (digit - 10 + 'A');
	}
}
