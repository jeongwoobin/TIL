#include <stdio.h>

int main() {
	int select, temp, cel, fah;

	printf("1. ����, 2. ȭ�� : ");
	scanf_s("%d", &select);
	printf("�µ��� �Է��ϼ��� : ");
	scanf_s("%d", &temp);

	if (select == 1) {
		fah = (temp * 1.8) + 32;
		printf(" ���� %d�� ȭ���� %d�Դϴ�.\n", temp, fah);
	}
	else if (select == 2) {
		cel = (temp - 32) / 1.8;
		printf(" ȭ�� %d�� ������ %d�Դϴ�.\n", temp, cel);
	}
	else
		printf(" �߸� �Է��ϼ̽��ϴ�. \n");

}