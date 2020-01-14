#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main() {
	srand((unsigned)time(NULL));
	int sum[3][3] = { 0, };
	int num[3][3] = { 0, };
	int a, b, grade;
	float avg;

	// 성적 입력

	while (1) {
		printf("학년 , 반 입력 : ");
		scanf_s("%d %d", &a, &b);
		if (a < 0 || b < 0) break;
		if (a > 3 || b > 3) {
			printf("잘못입력하셨습니다.");
			continue;
		}
		grade = (rand() * 5) % 100;

		sum[a-1][b-1] += grade;
		num[a-1][b-1] ++;
	}

	for (int i = 0; i < 3; i++) {
		for (int j = 0; j < 3; j++) { 
			if (num[i][j] == 0) {
				continue;
			}
			avg = sum[i][j] / num[i][j];
			printf("\n%d학년 %d반의 평균은 %f\n", i+1, j+1, avg);
		}
	}
}