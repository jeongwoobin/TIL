// 학점 부여
#include <stdio.h>

int main() {
	int algGrade, alg, cGrade, c, engGrade, eng, gradeSum, avg;
	char grade;

	printf("알고리즘 점수와 학점을 입력하세요 : ");
	scanf_s("%d %d", &algGrade, &alg);
	printf("c언어 점수와 학점을 입력하세요 : ");
	scanf_s("%d %d", &cGrade, &c);
	printf("영어 점수와 학점을 입력하세요 : ");
	scanf_s("%d %d", &engGrade, &eng);

	gradeSum = (algGrade * alg) + (cGrade * c) + (engGrade * eng);
	avg = gradeSum / (alg + c + eng);

	if (avg >= 90) {
		grade = 'A';
	}
	else if (avg >= 80) {
		grade = 'B';
	}
	else if (avg >= 70) {
		grade = 'C';
	}
	else {
		grade = 'F';
	}

	printf("평균 : %d\n", avg);
	printf("학점 : %c\n", grade);
}