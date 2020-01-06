#include <stdio.h>

int main() {
	int select, temp, cel, fah;

	printf("1. ¼·¾¾, 2. È­¾¾ : ");
	scanf_s("%d", &select);
	printf("¿Âµµ¸¦ ÀÔ·ÂÇÏ¼¼¿ä : ");
	scanf_s("%d", &temp);

	if (select == 1) {
		fah = (temp * 1.8) + 32;
		printf(" ¼·¾¾ %d´Â È­¾¾·Î %dÀÔ´Ï´Ù.\n", temp, fah);
	}
	else if (select == 2) {
		cel = (temp - 32) / 1.8;
		printf(" È­¾¾ %d´Â ¼·¾¾·Î %dÀÔ´Ï´Ù.\n", temp, cel);
	}
	else
		printf(" Àß¸ø ÀÔ·ÂÇÏ¼Ì½À´Ï´Ù. \n");

}