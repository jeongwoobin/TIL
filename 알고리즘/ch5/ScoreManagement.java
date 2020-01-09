package javaexam.ch5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ScoreManagement {
	Map<String, Student> data = new HashMap<>();
	Scanner scanner = new Scanner(System.in); 
	
	void genStudent() {
		Student s = new Student("20179916", "��浿", "�İ�", Arrays.asList(90, 85, 92, 99, 91));
		data.put("20179916", s);
		s = new Student("20179917", "ȫ�浿", "�濵", Arrays.asList(70, 65, 82, 96, 86));
		data.put("20179917", s);
		s = new Student("20179918", "�ڱ浿", "����", Arrays.asList(98, 89, 79, 95, 77));
		data.put("20179918", s);
	}
	
	void findStudent() {
		System.out.print("������ȸ - �й��Է� : ");
		String id = scanner.next();
		
		//�й����� ��ȸ�Ͽ� �̸�, �а�, ����, ��� ���
		Student s = data.get(id);
		
		System.out.printf("�й� : %s, �̸� : %s, �а� : %s\n", s.getId(), s.getName(), s.getDept());
		int total = 0;
		for( int sj : s.getSungjuk() ) {
			total += sj;
		}
		int avg = total / s.getSungjuk().size();
		System.out.printf("���� : %d, ��� : %d\n", total, avg);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ScoreManagement app = new ScoreManagement();
		app.genStudent();
		app.findStudent();
	}

}
