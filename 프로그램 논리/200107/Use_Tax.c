//// 사용량에 따른 총 사용액
//#include <stdio.h>
//
//int main() {
//	int userNum, grade, use, usePay;
//	float tax, allUse;
//
//	printf("사용자번호, 등급, 사용량 입력 : ");
//	scanf_s("%d %d %d", &userNum, &grade, &use);
//
//	if ( grade == 1 ) {
//		usePay = use * 450;
//	}
//	else if (grade == 2) {
//		usePay = use * 300;
//	}
//	else {
//		usePay = use * 200;
//	}
//	allUse = usePay * 1.1;
//	tax = usePay * 0.1;
//
//	printf("사용자번호 : %d\n", userNum);
//	printf("세금 : %f\n", tax);
//	printf("총사용액 : %f\n", allUse);
//}