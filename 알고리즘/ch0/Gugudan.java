package javaexam.ch0;

public class Gugudan {

	// 2단 출력, 2 * 1 = 2
	void go() {
		for(int i = 1; i < 10; i++) {
			System.out.printf("%d * %d = %2d\n", 2, i, 2*i);
		}
	}
	
	// n단 출력
	void go(int num) {
		for(int i = 1; i < 10; i++) {
			System.out.printf("%d * %d = %2d\n", num, i, num*i);
		}
	}
	
	// n~m단 출력
	void go(int start, int end) {
		for(int i = start; i <= end; i++) {
			for(int j = 1; j < 10; j++) {
				System.out.printf("%d * %d = %2d\n", i, j, i*j);
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Gugudan gugu = new Gugudan();
		// gugu.go();
		// gugu.go(3);
		gugu.go(2, 9);
	}
}
