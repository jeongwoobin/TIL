#include <stdio.h>

int main() {
	int A[] = { 1, 2, 3, 4, 5 };
	int B[] = { 11, 12, 13, 14, 15 };
	int code, num, flag = 0;;

	printf("�����ڵ�� ������ �Է��ϼ��� : ");
	scanf_s("%d %d", &code, &num);

	if (code == 1) {
		for (int i = 0; i < 5; i++) {
			if (A[i] == num) {
				printf("%d\n", B[i]);
				flag = 1;
				break;
			}
		}
	}
	
	else {
		for (int i = 0; i < 5; i++) {
			if (B[i] == num) {
				printf("%d\n", A[i]);
				flag = 1;
				break;
			}
		}
	}

	if (flag == 0) {
		printf("�������");
	}
}