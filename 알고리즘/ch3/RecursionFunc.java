package javaexam.ch3;

import java.util.Scanner;

public class RecursionFunc {
	int Func(int num) {				
		if( num == 1 ) {
			return 1;
		}
		else {
			return num + Func(num-1);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		RecursionFunc RecFunc = new RecursionFunc();
		
		System.out.println("1���� ���� ���� ���� >> ");
		int num = scanner.nextInt();
		
		System.out.println("1���� " + num + "���� ���� " + RecFunc.Func(num));	
	}
}
