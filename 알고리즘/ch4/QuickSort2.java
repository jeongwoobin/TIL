package javaexam.ch4;

import java.util.Arrays;

public class QuickSort2 {
	public static void main(String[] args) {
		int[] data = { 55, 1, 99, 23, 10, 5};
		//int[] data = { 5, 3, 8, 4, 9, 1, 6, 2, 7,10 };
		//int[] data = { 6,23,1,65,78,3,21,10,9 };
		
		QuickSort2 app = new QuickSort2();
		System.out.println("원본: "+Arrays.toString(data));
		app.sort(data, 0, data.length - 1);
		System.out.println("정렬: " + Arrays.toString(data));
	}

	private void sort(int[] data, int left, int right) {
        int lowIdx = left + 1;
        int highIdx = right;
        int pivot = data[left];
		if(left < right) {
	        while (lowIdx <= highIdx) {
	            while (lowIdx <= right && data[lowIdx] <= pivot) {
	                lowIdx++;
	            }
	 
	            while (left + 1 <= highIdx && pivot <= data[highIdx]) {
	                highIdx--;
	            }
	 
	            if (lowIdx <= highIdx) {
	                int temp = data[lowIdx];
	                data[lowIdx] = data[highIdx];
	                data[highIdx] = temp;
	            } else {
	                data[left] = data[highIdx]; // pivot이 자신의 자리를 찾아가는 과정
	                data[highIdx] = pivot;
	            }
	        }
			System.out.println("과정: " + Arrays.toString(data));

	        sort(data, left, highIdx - 1);
	        sort(data, highIdx + 1, right);
		}
	}
}
