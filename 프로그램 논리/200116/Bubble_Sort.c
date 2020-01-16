//// 버블정렬
//#include <stdio.h>
//
//int main() {
//	int arr[] = { 54, 35, 44, 12, 5 };
//	int temp = 0;
//
//	printf("원본 : ");
//	for (int i = 0; i < 5; i++) {
//		printf("%2d ", arr[i]);
//	}
//
//	for (int i = 0; i < 4; i++) {
//		for (int j = 0; j < 4 - i; j++) {
//			if (arr[j] > arr[j+1]) {
//				temp = arr[j];
//				arr[j] = arr[j+1];
//				arr[j+1] = temp;
//			}
//		}
//		printf("\n정렬 : ");
//		for (int i = 0; i < 5; i++) {
//			printf("%2d ", arr[i]);
//		}
//	}
//}