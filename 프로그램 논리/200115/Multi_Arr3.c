//// ㄹ형태로 값 증가
//#include <stdio.h>
//
//int main() {
//	int arr[4][5] = { 0, };
//	int num = 2;
//
//	for (int i = 0; i < 4; i++) {
//		for (int j = 0; j < 5; j++) {
//			if (i % 2 == 0) {
//				arr[i][j] = num;
//				num += 2;
//			}
//			else {
//				arr[i][5 - j - 1] = num;
//				num += 2;
//			}
//		}
//	}
//
//	for (int i = 0; i < 4; i++) {
//		for (int j = 0; j < 5; j++) {
//			printf("%2d ", arr[i][j]);
//		}
//		printf("\n");
//	}
//}