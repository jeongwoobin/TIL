package javaexam.ch5;

import java.util.List;

public class Student {
	private String id;
	private String name;
	private String dept;
	private List<Integer> sungjuk;
	
	
	public Student(String id, String name, String dept, List<Integer> sungjuk) {
		super();
		this.id = id;
		this.name = name;
		this.dept = dept;
		this.sungjuk = sungjuk;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public List<Integer> getSungjuk() {
		return sungjuk;
	}
	public void setSungjuk(List<Integer> sungjuk) {
		this.sungjuk = sungjuk;
	}	
}
