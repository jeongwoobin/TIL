//// 교환정렬
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
//		for (int j = i+1; j < 5; j++) {
//			if (arr[i] > arr[j]) {
//				temp = arr[i];
//				arr[i] = arr[j];
//				arr[j] = temp;
//			}
//		}
//		printf("\n정렬 : ");
//		for (int i = 0; i < 5; i++) {
//			printf("%2d ", arr[i]);
//		}
//	}
//}