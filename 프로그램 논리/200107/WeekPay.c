//// �ִ�޿� ���
//#include <stdio.h>
//
//int main() {
//	int empNum, grade, time, tax, hourPay, weekPay, pay;
//
//	printf("�����ȣ, ���, �ٹ��ð��� �Է��ϼ��� : ");
//	scanf_s("%d %d %d", &empNum, &grade, &time);
//
//	// ��޴� �ð��� �ݾ�
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
//	// �ʰ��ð��ٹ��� �ݾ�
//	if (time >= 40) {
//		weekPay = (time * hourPay) + (time - 40) * (hourPay / 2);
//	}
//	else {
//		weekPay = (time * hourPay);
//	}
//	
//	// �ֱ޿� ���� ���� ����
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
//	printf("�����ȣ : %d\n", empNum);
//	printf("��  �޿� : %d\n", weekPay);
//	printf("��    �� : %d\n", tax);
//	printf("�����޾� : %d\n", pay);
//}