package javaexam.ch0;

import java.util.Scanner;

public class BinarySearch {
	
	String run(int arr[], int min, int max, int num) {
		int mid = (max+min)/2;
		
		if( arr[mid] < num ) {
			min = mid+1;
			return run(arr, min, max, num);
		}
		else if( arr[mid] > num ) {
			max = mid-1;
			return run(arr, min, max, num);
		}
		else  {
			return mid+"�� �ε����� �ֽ��ϴ�.";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		BinarySearch bsearch = new BinarySearch();
		
		System.out.print("�ּڰ��� �Է��ϼ��� : ");
		int min = scanner.nextInt();
		System.out.print("�ִ��� �Է��ϼ��� : ");
		int max = scanner.nextInt();
		System.out.print("������ ���� �� ã�� ���ڸ� �Է��ϼ��� : ");
		int num = scanner.nextInt();
		
		if( (max < min) || (max == min) ) {
			System.out.println("������ �߸� �Է��ϼ̽��ϴ�.");
		}
		else if( (num < min) || (max < num) ) {
			System.out.println("�����ȿ� ���ڰ� �����ϴ�.");
		}
		else {
			int []arr = new int[max-min+1];
			for(int i = 0; i<arr.length; i++) {
				arr[i] = i+min;
			}
			
			String result = bsearch.run(arr, min, max, num);
			System.out.println(result);
		}
	}
}
