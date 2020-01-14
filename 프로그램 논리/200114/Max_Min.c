//#include <stdio.h>
//#define ARR_SIZE 10
//
//int main() {
//	int arr[ARR_SIZE] = { 0, };
//	int maxIndex = 0, minIndex = 0, maxCnt = 0, minCnt = 0;
//
//	for (int i = 0; i < ARR_SIZE; i++) {
//		printf("성적을 입력하세요 : ");
//		scanf_s("%d", &arr[i]);
//
//		if (arr[maxIndex] < arr[i]) {
//			maxIndex = i;
//		}
//		if (arr[minIndex] > arr[i]) {
//			minIndex = i;
//		}
//	}
//
//	for (int i = 0; i < ARR_SIZE; i++) {
//		if (arr[maxIndex] == arr[i]) {
//			maxCnt++;
//		}
//		if (arr[minIndex] == arr[i]) {
//			minCnt++;
//		}
//	}
//
//	printf("max : %d, min : %d\n", maxCnt, minCnt);
//
//}