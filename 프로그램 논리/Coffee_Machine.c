#include <stdio.h>

int main() {
	int money, select;

	printf("Ŀ�� 1�ܿ� 200���Դϴ�\n");
	printf("���� �������� >> ");
	scanf_s("%d", &money);

	while (1) {
		if (money >= 200) {
			printf("\n1. Ŀ��, 2. ��ȯ >> ");
			scanf_s("%d", &select);

			if (select == 1) {
				printf("Ŀ�ǳ�������...\n");
				printf("Ŀ�ǳ��Խ��ϴ�.\n");
				money -= 200;
			}

			else if (select == 2) {
				printf("���� ��ȯ�մϴ�...\n");
				break;
			}

			else {
				printf("�߸� �Է��ϼ̽��ϴ�.\n");
			}
		}

		else {
			printf("\n���� �����մϴ�.\n���� ��ȯ�մϴ�\n");
			break;
		}
	}

	int fiveHD = money / 500;
	int oneHD = (money % 500) / 100;
	int fiveTN = (money % 100) / 50;
	int oneTN = (money % 50) / 10;
	printf("\n�ܵ� >> \n");
	printf("   500�� : %4d��\n", fiveHD);
	printf("   100�� : %4d��\n", oneHD);
	printf("    50�� : %4d��\n", fiveTN);
	printf("    10�� : %4d��\n", oneTN);
	printf(" �� �ܵ� : %4d��\n", money);
}