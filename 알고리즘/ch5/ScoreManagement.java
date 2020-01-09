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
		Student s = new Student("20179916", "김길동", "컴공", Arrays.asList(90, 85, 92, 99, 91));
		data.put("20179916", s);
		s = new Student("20179917", "홍길동", "경영", Arrays.asList(70, 65, 82, 96, 86));
		data.put("20179917", s);
		s = new Student("20179918", "박길동", "교류", Arrays.asList(98, 89, 79, 95, 77));
		data.put("20179918", s);
	}
	
	void findStudent() {
		System.out.print("성적조회 - 학번입력 : ");
		String id = scanner.next();
		
		//학번으로 조회하여 이름, 학과, 총점, 평균 출력
		Student s = data.get(id);
		
		System.out.printf("학번 : %s, 이름 : %s, 학과 : %s\n", s.getId(), s.getName(), s.getDept());
		int total = 0;
		for( int sj : s.getSungjuk() ) {
			total += sj;
		}
		int avg = total / s.getSungjuk().size();
		System.out.printf("총점 : %d, 평균 : %d\n", total, avg);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ScoreManagement app = new ScoreManagement();
		app.genStudent();
		app.findStudent();
	}

}
