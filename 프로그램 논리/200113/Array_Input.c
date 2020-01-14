#include <stdio.h>

int main() {
	int arr[10] = { 0, };
	int sum = 0;
	int max = 0 , min = 0;

	for(int i = 0; i < 10; i++) {
		printf("배열에 넣을 원소 입력 : ");
		scanf_s("%d", &arr[i]);

		sum += arr[i];
	}

	max = arr[0];
	min = arr[0];
	for (int i = 0; i < 10; i++) {
		if (max < arr[i])
			max = arr[i];
		if (min > arr[i])
			min = arr[i];
	}

	printf("sum = %d, max = %d, min = %d\n", sum, max, min);
}