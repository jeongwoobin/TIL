// �ŵ�����
#include <stdio.h>
#include <math.h>

int main() {
	int num;
	long double sum = 0, result;		// ���� �ʹ� Ŀ�� long double�� �ؾ���

	for (int i = 1; i <= 10; i++) {
		num = 2 * i - 1;
		result = pow(num, i);
		sum += result;
		printf("%d ** %d = %lf\n", num, i, result);
	}
	printf("%lf", sum);
}