package javaexam.ch3;

import java.util.Scanner;

public class Num_Sum {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int sum=0;
		
		System.out.print("num >> ");
		int num = scanner.nextInt();
		
		for(int i = 1; i <= num; i++) {
			sum +=i;
		}
		System.out.println(sum);
	}
}
