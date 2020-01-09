//// 주당급여 계산
//#include <stdio.h>
//
//int main() {
//	int empNum, grade, time, tax, hourPay, weekPay, pay;
//
//	printf("사원번호, 등급, 근무시간을 입력하세요 : ");
//	scanf_s("%d %d %d", &empNum, &grade, &time);
//
//	// 등급당 시간당 금액
//	if (grade == 1) {
//		hourPay = 10000;
//	}
//	else if (grade == 2) {
//		hourPay = 7000;
//	}
//	else {
//		hourPay = 5000;
//	}
//
//	// 초과시간근무시 금액
//	if (time >= 40) {
//		weekPay = (time * hourPay) + (time - 40) * (hourPay / 2);
//	}
//	else {
//		weekPay = (time * hourPay);
//	}
//	
//	// 주급에 대한 세금 산출
//	if (weekPay >= 300000) {
//		tax = weekPay * 0.13;
//	}
//	else if (weekPay >= 200000) {
//		tax = weekPay * 0.11;
//	}
//	else {
//		tax = weekPay * 0.08;
//	}
//	
//	pay = weekPay - tax;
//
//	printf("사원번호 : %d\n", empNum);
//	printf("총  급여 : %d\n", weekPay);
//	printf("세    금 : %d\n", tax);
//	printf("실지급액 : %d\n", pay);
//}