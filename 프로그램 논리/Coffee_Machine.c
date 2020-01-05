#include <stdio.h>

int main() {
	int money, select;

	printf("커피 1잔에 200원입니다\n");
	printf("돈을 넣으세요 >> ");
	scanf_s("%d", &money);

	while (1) {
		if (money >= 200) {
			printf("\n1. 커피, 2. 반환 >> ");
			scanf_s("%d", &select);

			if (select == 1) {
				printf("커피나오는중...\n");
				printf("커피나왔습니다.\n");
				money -= 200;
			}

			else if (select == 2) {
				printf("돈을 반환합니다...\n");
				break;
			}

			else {
				printf("잘못 입력하셨습니다.\n");
			}
		}

		else {
			printf("\n돈이 부족합니다.\n돈을 반환합니다\n");
			break;
		}
	}

	int fiveHD = money / 500;
	int oneHD = (money % 500) / 100;
	int fiveTN = (money % 100) / 50;
	int oneTN = (money % 50) / 10;
	printf("\n잔돈 >> \n");
	printf("   500원 : %4d개\n", fiveHD);
	printf("   100원 : %4d개\n", oneHD);
	printf("    50원 : %4d개\n", fiveTN);
	printf("    10원 : %4d개\n", oneTN);
	printf(" 총 잔돈 : %4d원\n", money);
}