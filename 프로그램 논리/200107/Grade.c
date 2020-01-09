//// 학점 부여
//#include <stdio.h>
//
//int main() {
//	int studentnum, midgrade, finalgrade, avg;
//	char grade;
//
//	printf(" 학번, 중간, 기말을 입력하세요 : ");
//	scanf_s("%d %d %d", &studentnum, &midgrade, &finalgrade);
//
//	avg = (midgrade + finalgrade) / 2;
//
//	if (avg >= 90) {
//		grade = 'a';
//	}
//	else if (avg >= 60) {
//		grade = 'b';
//	}
//	else {
//		grade = 'f';
//	}
//
//	printf("학번 : %d\n", studentnum);
//	printf("평균 : %d\n", avg);
//	printf("학점 : %c\n", grade);
//}