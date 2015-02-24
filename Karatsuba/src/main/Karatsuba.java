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
	public static int[] resultArray;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		PrintWriter printWriter = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(System.out)));

		int numTestCases;
		int[] arrVelocity, arrMass;
		arrVelocity = new int[MAX_DIGITS + 1];
		arrMass = new int[MAX_DIGITS + 1];
		String velocityStr, massStr;

		numTestCases = scanner.nextInt();

		for (int i = 1; i <= numTestCases; ++i) {
			scanner.nextInt(); // BASE already defined as 10
			scanner.nextLine();
			velocityStr = scanner.nextLine();
			massStr = scanner.nextLine();

			int fpV = scanArray(velocityStr, arrVelocity);
			int fpM = scanArray(massStr, arrMass);

			resultArray = new int[MAX_DIGITS * 2];
			int resultLength = multArrays(arrVelocity, arrMass);
			String output = resultToString(resultArray, resultLength, fpV + fpM);

			printWriter.write(trimZeros(output));
			printWriter.write("\n");
		}
		printWriter.close(); // do not forget to use this
	}
	
	private static int karatsubaMultiply(int[] arrVel, int[] arrMass) {
		//base case:
		if (arrVel.length<CUT_OFF && arrMass.length<CUT_OFF) {
			return multArrays(arrVel, arrMass); // quadratic long multiply
		}
			
		//splitting by halves:
//		R = max(length of X, length of Y) / 2;
//		HighX, LowX = split X at R;
//		HighY, LowY = split Y at R;

		//recursive calls: 3 times: 
//		Z0 = karatsubaMultiply(LowX, LowY);
//		Z2 = karatsubaMultiply(HighX, HighY);
//		Z1 = karatsubaMultiply(LowX+HIghX, LowY+HighY);

		//adding and subtracting:
//		Res = Z2 * B^(2*R) + (Z1-Z2-Z0)*B^R + Z0

		//return:
		return 0;
//		return arrResult;
	}

	private static int multArrays(int[] arrVelocity, int[] arrMass) {
		int sizeVelocity = arrVelocity[arrVelocity.length-1];
		int sizeMass = arrMass[arrMass.length-1];
		int d = 0;
		int i = 0;
		for (d = 0; d < sizeMass; d++) {
			int carry = 0;
			for (i = 0; i < sizeVelocity; i++) {
				resultArray[i + d] += arrVelocity[i] * arrMass[d] + carry;
				carry = resultArray[i + d] / BASE;
				resultArray[i + d] %= BASE;
			}
			resultArray[d + i] += carry;
		}
		return d + i;
	}

	private static String resultToString(int[] array, int size, int fp) {
		StringBuilder sb = new StringBuilder(size + 1);
		fp -= 1;
		for (int i = size - 1; i >= 0; --i) {
			if (i == fp) {
				sb.append('.');
			}
			sb.append(toDigit(array[i]));
		}

		return sb.toString();
	}

	private static int scanArray(String s, int[] arr) {
		int index = 0;
		int fixedPoint = 0;
		for (int i = s.length() - 1; i >= 0; --i) {
			if (s.charAt(i) == '.') {
				fixedPoint = index;
			} else {
				arr[index] = parseDigit(s.charAt(i));
				index++;
			}
		}

		arr[arr.length - 1] = index; // Store size.
		return fixedPoint;
	}

	private static String trimZeros(String input) {
		int left = 0;
		int right = input.length() - 1;
		int fp = input.indexOf('.');
		if (fp == -1) {
			fp = input.length();
		}

		while (left < fp - 1) {
			if (input.charAt(left) != '0')
				break;
			left++;
		}

		while (right >= fp) {
			if (input.charAt(right) != '0') {
				if (input.charAt(right) == '.')
					right--;
				break;
			}
			right--;
		}

		if (left >= fp)
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
