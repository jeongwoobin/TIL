//// 배열값이 3보다 크면 T 아니면 F를 가지는 배열 생성
//#include <stdio.h>
//
//int main() {
//	int arr[3][3] = { 1, 2, 3, 2, 3, 4, 3, 4, 5 };
//	char re[3][3] = { 0, };
//
//	for (int i = 0; i < 3; i++) {
//		for (int j = 0; j < 3; j++) {
//			if (arr[i][j] > 3) {
//				re[i][j] = 'T';
//			}
//			else {
//				re[i][j] = 'F';
//			}
//			printf("%c ", re[i][j]);
//		}
//		printf("\n");
//	}
//}