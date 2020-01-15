//// 상품코드, 수량 입력 -> 금액
//#include <stdio.h>
//
//int main() {
//	int arr[4][2] = { 1, 1000, 2, 1200, 3, 800, 4, 1750 };
//	int num, code;
//
//	while (1) {
//		printf("상품코드, 수량 입력 : ");
//		scanf_s("%d %d", &code, &num);
//
//		for (int i = 0; i < 4; i++) {
//			if (arr[i][0] == code) {
//				printf("금액 : %d\n", arr[i][1] * num);
//			}
//		}
//	}
//}