package javaexam.ch0;

import java.util.Scanner;

public class Conditional {
	void login() {
		Scanner scan = new Scanner(System.in);
		
		System.out.print("## ���̵� �Է��ϼ��� : ");
		String uname = scan.next();
		
		System.out.print("## ��й�ȣ�� �Է��ϼ��� : ");
		String pwd = scan.next();
		
		if(uname.contentEquals("hong") && pwd.contentEquals("1234")) {
			System.out.println("���� Ȯ��!! -> �α��� ����");
		}
		else {
			System.out.println("���̵� ��й�ȣ�� Ʋ�Ƚ��ϴ�.!!");
		}
	}
	
	void check() {
		int msgCnt = 10;
		String msg = msgCnt > 0 ? "���ο� ������ �ֽ��ϴ�." : "���ο� ������ �����ϴ�.";
		System.out.println(msg);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Conditional con = new Conditional();
		
		while(true) {
			System.out.print("## �޴��� �����ϼ��� (1: �α���, 2: ����Ȯ��, x: ����) : ");
			Scanner scanner = new Scanner(System.in);
			String menu = scanner.next();
			
			switch(menu) {
			case "1": con.login(); break;
			case "2": con.check(); break;
			case "x": System.exit(0);
			default : System.out.println("�߸��� �Է��Դϴ�!!");
			}
		}
	}
}
