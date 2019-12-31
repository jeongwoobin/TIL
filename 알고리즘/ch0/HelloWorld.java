package javaexam.ch0;

public class HelloWorld {
	String msg = "Hello World!";
	
	void prtMsg() {
		System.out.println(msg);
	}
	
	void prtMsg(String smile) {
		System.out.println(smile+" "+msg);
	}
	
	String getMsg(String smile) {
		return smile+" hi";
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloWorld hello = new HelloWorld();
		hello.prtMsg();
		hello.prtMsg("hahaha");
		System.out.println(hello.getMsg("haha"))	;
	}
}