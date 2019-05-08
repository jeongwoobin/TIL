---

layout: post
title: Python 스터디 멘토 준비 4일차
tags: Python 스터디 format 문자열 정수와실수

---

### format

```

year = 2019
month = 5
day = 9

print('오늘은 ', year, '년 ', month, '월 ', day, '일 입니다.')

base = '오늘은 {}년 {}월 {}일 입니다.'
today = base.format(year, month, day)

print(base)
print(today)

print('오늘은 {}년 {}월 {}일 입니다.'.format(year, month, day))

```
* 항상 대괄호 갯수와 format의 매개변수의 수는 같아야한다.
* 만약 다르다면 오류가 남

### 문자열
```
# 한줄 문자열
string1 = 'this is string'
string2 = "이것도 문자열"
string3 = '{}도 {}도 지금 이것도 문자열'.format(string1, string2)

print(string1, string2, string3)

#여러줄 문자열
long_string1 = '''이렇게 하면
여러줄도 가능!'''
long_string2 = """이렇게 쌍따옴표도
가능!"""

print(long_string1)
print(long_string2)
```

### 정수와 실수
* 정수
	* Integer 줄여서 int 라고표현
	* 연산후에 정확한 값이 나옴
* 실수
	* 부동소수점을 이용하여 표현하기 때문에 floating point 줄여서 float 로표현
	* 연산에 있어 완벽한 정확성은 없음

```
num1 = 3
num2 = 3.0
num3 = 3.000000000

print(num1)
print(num2)
print(num3)
# num2와 num3은 같지만 num1과는 같지 않다.
```

```
num4 = 5 * 1
num5 = 5 * 1.0
# 정수끼리의 연산 결과값은 정수이고
# 연산중 실수가 들어가있다면 결과값은 실수가 나온다.

div1 = 6 / 5
div2 = 6 // 5
# 하지만 나눗셈같은 경우 정수와의 연산에서도 결과값이 실수가 나오기 때문에
# 몫을 정수로만 받고싶다면 //를 쓰면 된다.

a = 7
b = 3
print(a == b * (a // b) + (a % b))
# 이것이 의미하는 바는?

# 실수의 연산은 완벽히 정확하지 않음
print(0.1 + 0.1 == 0.2)			#TRUE
print(0.1 + 0.1 + 0.1 == 0.3)	#FALSE
```