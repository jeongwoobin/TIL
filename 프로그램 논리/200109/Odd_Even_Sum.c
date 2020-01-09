//// 두 수 사이의 홀수들의 합과 짝수들의 합
//#include <stdio.h>
//
//int main() {
//	int first, second, temp, i;
//	int oddSum=0, evenSum=0;
//
//	printf(" 두 수를 입력하세요 : ");
//	scanf_s("%d %d", &first, &second);
//
//	if (first >= second) {		// 무조건 first가 작은 수
//		temp = first;
//		first = second;
//		second = temp;
//	}
//
//	for (int i = first; i <= second; i++) {
//		if (i % 2 == 0) {
//			evenSum += i;
//		}
//		else {
//			oddSum += i;
//		}
//	}
//	printf(" oddSum : %d, evenSum : %d\n", oddSum, evenSum);
//}