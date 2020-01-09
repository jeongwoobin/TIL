// 거듭제곱
#include <stdio.h>
#include <math.h>

int main() {
	int num;
	long double sum = 0, result;		// 수가 너무 커서 long double로 해야함

	for (int i = 1; i <= 10; i++) {
		num = 2 * i - 1;
		result = pow(num, i);
		sum += result;
		printf("%d ** %d = %lf\n", num, i, result);
	}
	printf("%lf", sum);
}