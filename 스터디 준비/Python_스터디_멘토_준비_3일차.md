---

layout: post
title: Python 스터디 멘토 준비 3일차
tags: Python 스터디 함수

---

### 함수
* 형식
	```
    def 함수이름() :
    	print('여기는 함수!')
    ```
    * 함수는 한번 정의해놓고 계속 불러서 쓸 수 있다.<br/>
    * 복잡한 계산식 등을 한번 정의해놓고 불러서 쓰면 편하다.<br/>
    * print() 도 함수이다.<br/><br/>

* 매개변수
	```
    def plus(x, y):
    	print('{}와 {}의 합은 {}입니다.'.format(x, y, x+y))
	a = 1
    b = 3
    plus(a,b)
    ```
    * 위의 코드에서 plus함수 안에 있는(함수를 정의할 때) x, y는 매개변수 라고 하고,<br/>
    * 함수를 호출할 때 쓰는 a,b를 실행 인자라고 한다.<br/>
    * 실행인자는 순서대로 매개변수에 하나씩 매칭되어 들어간다.<br/><br/>

* 리턴값
	```
    def plus_Result(x,y):
    	return x+y
	a = 1
	b = 3
    result = plus_Result(a, b)
    print(result)
    ```
	* return을 이용해 함수의 실행결과를 값으로 돌려줄 수 있다.<br/>
	* 하나의 return에 여러개의 결과를 돌려줄 수 있다.(쉼표로 구분)<br/>
    * retrun이 실행 된 후에는 즉시 함수가 종료된다.<br/>
    * return을 여러개 쓸 수 있지만 그 경우에는 먼저 선언된 return이 실행되고 함수가 종료된다.<br/>
    * 만약 경우에 따라 return값을 다르게 출력하고 싶다면 if문을 이용하면 된다.