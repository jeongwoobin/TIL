package javaexam.ch3;

import java.util.Scanner;
import java.util.stream.LongStream;

public class RecursionFunc {
	// Recursion
	int Func(int num) {				
		if( num == 1 ) {
			return 1;
		}
		else {
			return num + Func(num-1);		// 함수 호출 수 연산
		}
	}
	
	// TilRecursion	@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	int recFuncTail( int n, int acc) {		//acc = 누적값
		if( n == 1 ) {
			return acc;
		}
		else {
			return recFuncTail ( n - 1, acc + n );		// 호출 후 연산 필요없음
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		RecursionFunc RecFunc = new RecursionFunc();
		
		System.out.println("1부터 합을 구할 숫자 >> ");
		int num = scanner.nextInt();
		
		// System.out.println("1부터 " + num + "까지 합은 " + RecFunc.Func(num));
		//System.out.println(RecFunc.recFuncTail(num, 1));
		
		// 코딩테스트에 유용 -> 재귀함수 따로 쓰지않고 누적연산 가능
		long result = LongStream.rangeClosed(1, num).reduce((long x, long y) -> x + y).getAsLong();		// 1부터 5까지 누적연산(reduce)인데 (n, n+1)을 +연산 하겠다 그 후 getAsLong. long타입으로 결과를 받겠다
		System.out.println(result);
		
	}
}
