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
			return num + Func(num-1);		// �Լ� ȣ�� �� ����
		}
	}
	
	// TilRecursion	@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	int recFuncTail( int n, int acc) {		//acc = ������
		if( n == 1 ) {
			return acc;
		}
		else {
			return recFuncTail ( n - 1, acc + n );		// ȣ�� �� ���� �ʿ����
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		RecursionFunc RecFunc = new RecursionFunc();
		
		System.out.println("1���� ���� ���� ���� >> ");
		int num = scanner.nextInt();
		
		// System.out.println("1���� " + num + "���� ���� " + RecFunc.Func(num));
		//System.out.println(RecFunc.recFuncTail(num, 1));
		
		// �ڵ��׽�Ʈ�� ���� -> ����Լ� ���� �����ʰ� �������� ����
		long result = LongStream.rangeClosed(1, num).reduce((long x, long y) -> x + y).getAsLong();		// 1���� 5���� ��������(reduce)�ε� (n, n+1)�� +���� �ϰڴ� �� �� getAsLong. longŸ������ ����� �ްڴ�
		System.out.println(result);
		
	}
}
