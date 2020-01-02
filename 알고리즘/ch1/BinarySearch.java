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
			return mid+"번 인덱스에 있습니다.";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		BinarySearch bsearch = new BinarySearch();
		
		System.out.print("최솟값을 입력하세요 : ");
		int min = scanner.nextInt();
		System.out.print("최댓값을 입력하세요 : ");
		int max = scanner.nextInt();
		System.out.print("범위의 숫자 중 찾을 숫자를 입력하세요 : ");
		int num = scanner.nextInt();
		
		if( (max < min) || (max == min) ) {
			System.out.println("범위를 잘못 입력하셨습니다.");
		}
		else if( (num < min) || (max < num) ) {
			System.out.println("범위안에 숫자가 없습니다.");
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
