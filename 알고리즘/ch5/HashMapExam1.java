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
		System.out.print("검색할 상품 코드 : ");
		String code = scanner.next();
		System.out.printf("%s : %s\n", code, map.get(code));
		
		// 등록 순서대로나오진 않음
		// 전체 해쉬맵 출력방법 1 - key : value
		System.out.println("-----------------------------");
		for(String s : map.keySet()) {
			System.out.printf("%s : %s\n", s, map.get(s));
		}
		
		// 전체 해쉬맵 출력방법 2 - value
		System.out.println("-----------------------------");
		for(String s : map.values()) {
			System.out.printf("%s\n", s);
		}

		// 전체 해쉬맵 출력방법 3 - key : value
		System.out.println("-----------------------------");
		for(Map.Entry<String, String> entry : map.entrySet()) {
			System.out.printf("%s : %s\n", entry.getKey(), entry.getValue());
		}
		
	}
}
