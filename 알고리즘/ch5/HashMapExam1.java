package javaexam.ch5;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HashMapExam1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<>();
		map.put("11082917", "Apple Iphone 11");
		map.put("12082912", "Samsung Galaxy Note 19");
		map.put("11062919", "LG G9");
		map.put("21082923", "Apple iPad pro");
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("�˻��� ��ǰ �ڵ� : ");
		String code = scanner.next();
		System.out.printf("%s : %s\n", code, map.get(code));
		
		// ��� ������γ����� ����
		// ��ü �ؽ��� ��¹�� 1 - key : value
		System.out.println("-----------------------------");
		for(String s : map.keySet()) {
			System.out.printf("%s : %s\n", s, map.get(s));
		}
		
		// ��ü �ؽ��� ��¹�� 2 - value
		System.out.println("-----------------------------");
		for(String s : map.values()) {
			System.out.printf("%s\n", s);
		}

		// ��ü �ؽ��� ��¹�� 3 - key : value
		System.out.println("-----------------------------");
		for(Map.Entry<String, String> entry : map.entrySet()) {
			System.out.printf("%s : %s\n", entry.getKey(), entry.getValue());
		}
		
	}
}
