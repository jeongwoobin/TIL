//// ��������
//#include <stdio.h>
//
//int main() {
//	int arr[] = { 54, 35, 44, 12, 5 };
//	int minIndex = 0, temp = 0;
//
//	printf("���� : ");
//	for (int i = 0; i < 5; i++) {
//		printf("%2d ", arr[i]);
//	}
//
//	for (int i = 0; i < 4; i++) {
//		for (int j = i + 1; j < 5; j++) {
//			if (arr[i] > arr[j]) {
//				minIndex = j;
//			}
//		}
//		temp = arr[i];
//		arr[i] = arr[minIndex];
//		arr[minIndex] = temp;
//		printf("\n���� : ");
//		for (int i = 0; i < 5; i++) {
//			printf("%2d ", arr[i]);
//		}
//	}
//}