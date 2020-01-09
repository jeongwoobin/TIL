package javaexam.ch4;

import java.util.Arrays;

public class QuickSort {
	public void sort(int[] data, int l ,int r) {
		int left = l;
		int right = r;
		int pivot = data[(l + r) / 2];
		
		do {
			while( data[left] < pivot )
				left ++;
			while( data[right] > pivot )
				right --;
			
			if(left<=right) {
				int temp = data[left];
				data[left] = data[right];
				data[right] = temp;
				left++;
				right--;
			}
		} while (left <= right);
		if(l < right)
			sort(data, l, right);
		if(r > left)
			sort(data, left, r);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] data = { 55, 1, 99, 23, 10, 5 };
		
		System.out.println("원본: "+Arrays.toString(data));
		QuickSort app = new QuickSort();

		app.sort(data, 0, data.length - 1);
		System.out.println("정렬: " + Arrays.toString(data));		
	}

}
