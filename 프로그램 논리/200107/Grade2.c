// ���� �ο�
#include <stdio.h>

int main() {
	int algGrade, alg, cGrade, c, engGrade, eng, gradeSum, avg;
	char grade;

	printf("�˰��� ������ ������ �Է��ϼ��� : ");
	scanf_s("%d %d", &algGrade, &alg);
	printf("c��� ������ ������ �Է��ϼ��� : ");
	scanf_s("%d %d", &cGrade, &c);
	printf("���� ������ ������ �Է��ϼ��� : ");
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

	printf("��� : %d\n", avg);
	printf("���� : %c\n", grade);
}