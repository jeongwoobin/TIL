package javaexam.ch2;

public class ChooseSort {
	
	void sort (int[] arr) {
		int i, j, n, min, temp;
		
		for(i = 0; i < (arr.length)-1; i++) {
			min = i;
			for(j = i+1; j < arr.length; j++) {
				if( arr[j] < arr[min] ) {
					min = j;
				}
			}
			temp = arr[min];				// 안쪽 for문에 int j라고 선언하면 for문 빠져나온 후 쓸 수 없음
			arr[min] = arr[i];				// 최솟값 인덱스min의 원소와 대조한 원소를 바꿈
			arr[i] = temp;
			
			System.out.printf("%2d번째>>", i+1);
			for(n = 0; n < arr.length; n++) {
				System.out.printf("%3d", arr[n]);
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChooseSort chooseSort = new ChooseSort();
		
		int[] arr = { 34, 97, 1, 54, 71, 43, 55, 69, 21, 91 };
		//System.out.println(arr.length);
		System.out.print("arr[]= ");
		for(int i = 0; i < arr.length; i++) {
			System.out.printf("%3d", arr[i]);
		}
		System.out.println("\n");
		
		chooseSort.sort(arr);
	}
}
