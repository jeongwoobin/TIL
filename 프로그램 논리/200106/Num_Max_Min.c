//#include <stdio.h>
//
//int main() {
//	int a, b, c, max, min, mid;
//
//	printf(" 3개의 수를 입력하세요 : ");
//	scanf_s("%d %d %d", &a, &b, &c);
//
//	if (a >= b) {
//		if (a >= c) {
//			max = a;
//			if (b >= c) {
//				min = c;
//				mid = b;
//			}
//			else {
//				min = b;
//				mid = c;
//			}
//		}
//		else {
//			max = c;
//			min = b;
//			mid = a;
//		}
//	}
//
//	else {
//		if (b >= c) {
//			max = b;
//			if (a >= c) {
//				min = c;
//				mid = a;
//			}
//			else {
//				min = a;
//				mid = c;
//			}
//		}
//		else {
//			max = c;
//			min = a;
//			mid = b;
//		}
//	}
//
//	printf(" max : %d, min : %d, mid : %d", max, min, mid);
//}