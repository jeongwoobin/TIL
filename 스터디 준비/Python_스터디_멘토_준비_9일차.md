---

layout: post
title: Python 스터디 멘토 준비 9일차
tags: Python 스터디 클래스 인스턴스 상속 오버라이드 super()

---

### 클래스
* 연관된 변수와 함수들의 집합
* 객체를 만들기 위한 틀
* 클래스내의 함수는 메소드
* 클래스내의 변수는 필드
* 클래스 이름의 첫글자는 대문자!

### 인스턴스
* 클래스에 의해 생성된 객체

```
#클래스
class Human:
    #특수한 메소드 -> 초기화 함수
    def __init__(self, name, weight):
    	self.name = name
        self.weight = weight
    #특수한 메소드 -> 문자열화 함수
    def __str__(self):
    	return "{}(몸무게 {}kg)".format(self.name, self.weight)
    #사용자 메소드
    def eat(self):
    	self.weight += 0.1
        print("{}가 먹어서 {}kg이 되었습니다".format(self.name, self.weight))
    def walk(self):
    	self.weight -= 0.1
        print("{}가 걸어서 {}kg이 되었습니다".format(self.name, self.weight))

#인스턴스
person1 = Human("사과", 60)
person2 = Human("바나나", 30)
person1.eat()
person2.eat()
person1.walk()
person2.walk()
```

### 상속
```
class Human():
    def walk(self):
    	print("걷는다")
    def eat(self):
    	print("먹는다")
    def wave(self):
    	print("손을 흔든다")
class Dog():
    def walk(self):
    	print("걷는다")
    def eat(self):
    	print("먹는다")
    def wag(self):
    	print("꼬리를 흔든다")
```

* 위의 메소드들을 보면 겹치는게 있다.
* 이러한 겹치는 메소드들을 하나의 클래스로 만들어 그 클래스의 내용을 가져다 쓸 수 있게 하는것을 상속이라고한다.

```
#부모 클래스
class Animal():
    def walk(self):
    	print("걷는다")
    def eat(self):
    	print("먹는다")
#자식 클래스
class Human(Animal):
    def wave(self):
    	print("손을 흔든다")
#자식 클래스
class Dog(Animal):
    def wag(self):
    	print("꼬리를 흔든다")
```

### 오버라이드
```
class Animal():
    def greet(self):
    	print("인사한다")
class Human(Animal):
    def greet(self):
    	print("손을 흔든다")
class Dog(Animal):
    def greet(self):
    	print("꼬리를 흔든다")
```

* 상속을 받지만 재정의함으로써 같은 이름을 가진 메소드를 덮어씀

### super()
* 자식클래스에서 부모클래스의 내용을 사용하고 싶은 경우
```
class Animal():
    def __init__(self, name):
    	self.name = name
class Human(Animal):
    def __init__(self, name, hand):
    	super().__init__(name) #부모클래스의 __init__메소드 호출
        self.hand = hand

person = Human("사람", "오른손")
```
