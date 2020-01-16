// 삽입정렬
#include <stdio.h>
#define ARR_SIZE 5

int main() {
	int arr[ARR_SIZE] = { 54, 35, 44, 12, 5 };
	int index = 0, temp = 0;

	printf("원본 : ");
	for (int i = 0; i < ARR_SIZE; i++) {
		printf("%2d ", arr[i]);
	}

	for (int i = 0; i < ARR_SIZE; i++) {
		index = i;
		for (int j = i + 1; j < ARR_SIZE; j++) {
			if (arr[j] < arr[index]) {
				index = j;
			}
		}
		temp = arr[i];
		arr[i] = arr[index];
		arr[index] = temp;

		printf("\n정렬 : ");
		for (int i = 0; i < ARR_SIZE; i++) {
			printf("%2d ", arr[i]);
		}
	}
}