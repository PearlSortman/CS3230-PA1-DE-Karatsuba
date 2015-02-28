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
	public static final int CUT_OFF = 2;
	public static final int BASE = 10;
	public static int sizeResult = 0;

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

			int[] momentumResultArray = new int[MAX_DIGITS * 2];
			momentumResultArray = karatsubaMultiply(velocityArray, massArray);
			String output = resultToString(momentumResultArray, sizeResult,
					velocityFixedPoint + massFixedPoint);

			printWriter.write(trimZeros(output));
			printWriter.write("\n");
		}
		printWriter.close(); // do not forget to use this
	}

	// z0 = karatsuba(low1,low2)
	// z1 = karatsuba((low1+high1),(low2+high2))
	// z2 = karatsuba(high1,high2)
	// return (z2*10^(2*m2))+((z1-z2-z0)*10^(m2))+(z0)
	private static int[] karatsubaMultiply(int[] velocityArray, int[] massArray) {
		// BASE CASE:
		if ((velocityArray[velocityArray.length - 1] <= CUT_OFF)
				|| (massArray[massArray.length - 1] <= CUT_OFF)) {
			return multArrays(velocityArray, massArray); // quadratic long
		} else {
			int sizeVelocity = velocityArray[velocityArray.length - 1];
			int sizeMass = massArray[massArray.length - 1];
			int splitIndex = (int) (Math
					.floor(Math.max(sizeVelocity, sizeMass) / 2));

			int[] highVelocity = splitArray(velocityArray, splitIndex, "high");
			int[] lowVelocity = splitArray(velocityArray, splitIndex, "low");

			int[] highMass = splitArray(massArray, splitIndex, "high");
			int[] lowMass = splitArray(massArray, splitIndex, "low");

			int[] Z0 = karatsubaMultiply(lowVelocity, lowMass);
			int[] Z1 = karatsubaMultiply(highVelocity, highMass);
			int[] Z2 = karatsubaMultiply(addArrays(lowVelocity, highVelocity),
					addArrays(lowMass, highMass));
			
			int[] R1temp = stringToArray(Integer.toString((int) Math.pow(BASE,
					(2 * splitIndex))));
			int[] R2temp = stringToArray(Integer.toString((int) Math.pow(BASE,
					splitIndex)));

			int[] R1 = multArrays(Z1, R1temp);
			int[] subtractZ1 = subtractArrays(Z2, Z1);
			int[] totalDiff = subtractArrays(subtractZ1, Z0);
			int[] R2 = multArrays(totalDiff, R2temp);
			int[] R3 = Z0;

			return addArrays(addArrays(R1, R2), R3);
		}
	}

	private static int[] multArrays(int[] velocityArray, int[] massArray) {
		int sizeVelocity = velocityArray[velocityArray.length - 1];
		int sizeMass = massArray[massArray.length - 1];
		int numDigits = 0;
		int i = 0;
		int j = 0;
		int[] tempResultArray = new int[MAX_DIGITS * 2];

		for (i = 0; i < sizeMass; i++) {
			int carry = 0;
			for (j = 0; j < sizeVelocity; j++) {
				tempResultArray[i + j] += velocityArray[j] * massArray[i]
						+ carry;
				carry = tempResultArray[i + j] / BASE;
				tempResultArray[i + j] %= BASE;
			}
			tempResultArray[i + j] += carry;
		}

		int x = tempResultArray.length - 2;
		Boolean numDigitsFound = false;

		while (!numDigitsFound && x >= 0) {
			if (tempResultArray[x] != 0) {
				numDigits = x + 1;
				numDigitsFound = true;
			}
			x--;
		}

		tempResultArray[tempResultArray.length - 1] = numDigits;
		sizeResult = numDigits;
		return tempResultArray;
	}

	private static int[] splitArray(int[] array, int splitIndex, String str) {
		int[] subArray = new int[MAX_DIGITS];
		int digitCounter = 0;
		if (str.equals("low")) {
			for (int i = 0; i < splitIndex; i++) {
				subArray[i] = array[i];
				digitCounter++;
			}
		} else if (str.equals("high")) {
			for (int i = splitIndex; i < array[array.length - 1]; i++) {
				subArray[i - splitIndex] = array[i];
				digitCounter++;
			}
		}
		subArray[subArray.length - 1] = digitCounter;
		return subArray;
	}

	private static int[] addArrays(int[] array1, int[] array2) {
		int array1Length = array1[array1.length - 1];
		int array2Length = array2[array2.length - 1];
		int max = Math.max(array1Length, array2Length);
		int min = Math.min(array1Length, array2Length);

		int[] sumArray = new int[MAX_DIGITS * 2];
		int numDigits = 0;

		for (int i = 0; i < min; i++) {
			sumArray[i] = array1[i] + array2[i];
			numDigits++;
		}
		for (int i = min; i < max; i++) { // in case of uneven length of arrays
			if (array1Length > array2Length) {
				sumArray[i] = array1[i];
			} else {
				sumArray[i] = array2[i];
			}
			numDigits++;
		}
		for (int i = 0; i < sumArray.length; i++) {
			if (sumArray[i] >= 10) {
				sumArray[i] -= 10;
				sumArray[i + 1] += 1;
			}
		}
		if (sumArray[numDigits] > 0) {
			numDigits++;
		}
		sumArray[sumArray.length - 1] = numDigits;
		sizeResult = numDigits;
		return sumArray;
	}

	private static int[] subtractArrays(int[] subtractFromArray,
			int[] subtractThisArray) {

		int subFromArrayLength = subtractFromArray[subtractFromArray.length - 1];
		int subThisArrayLength = subtractThisArray[subtractThisArray.length - 1];
		int max = Math.max(subFromArrayLength, subThisArrayLength);
		int min = Math.min(subFromArrayLength, subThisArrayLength);

		int[] diffArray = new int[MAX_DIGITS * 2];
		int numDigits = 0;

		for (int i = 0; i < min; i++) {
			diffArray[i] = subtractFromArray[i] - subtractThisArray[i];
			numDigits++;
		}
		for (int i = min; i < max; i++) {
			if (subFromArrayLength > subThisArrayLength) {
				diffArray[i] = subtractFromArray[i];
			} else {
				diffArray[i] = subtractThisArray[i];
			}
			numDigits++;
		}
		for (int i = 0; i < max; i++) {
			if (diffArray[i] < 0) {
				diffArray[i] += 10;
				diffArray[i + 1] -= 1;
			}
		}
		if (diffArray[numDigits] > 0) {
			numDigits++;
		}
		diffArray[diffArray.length - 1] = numDigits;
		return diffArray;
	}

	private static int[] stringToArray(String inputString) {
		int numDigits = 0;
		int[] array = new int[MAX_DIGITS * 2];
		for (int i = inputString.length() - 1; i >= 0; i--) {
			array[numDigits] = parseDigit(inputString.charAt(i));
			numDigits++;
		}
		array[array.length - 1] = numDigits;
		return array;
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

	/*
	 * Return: A = A + B * base ^ offset. A[0], B[0] store the length of the
	 * numbers, digits are stored in reversed order. A[],B[] are arrays of
	 * constant max-size.
	 */
//	private static int[] addWithOffset(int[] A, int[] B, int base, int offset) {
//		int lenA = A[A.length - 1];
//		int lenB = B[B.length - 1];
//		int carry = 0;
//		int i = 0;
//		offset++;
//		int b;
//		int a;
//
//		while (i <= lenB || carry > 0) {
//			// if (offset > lenA) a = 0;
//			// else a = A[offset];
//
//			a = offset > lenA ? 0 : A[offset];
//			b = i > lenB ? 0 : B[i];
//			carry += a + b;
//
//			if (carry >= base) {
//				A[offset] = carry - base;
//				carry = 1;
//			} else {
//				A[offset] = carry;
//				carry = 0;
//			}
//
//			i++;
//			offset++;
//		}
//
//		offset--;
//		while (offset > 1 && A[offset] == 0) {
//			offset--;
//			if (offset > lenA) {
//				A[0] = offset;
//			}
//		}
//		return A;
//	}

}
