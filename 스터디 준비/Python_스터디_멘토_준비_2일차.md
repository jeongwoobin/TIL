---

layout: post
title: Python 스터디 멘토 준비 2일차
tags: Python 스터디 조건문

---

### 조건문
* if문<br/>
	조건문은 조건이 참일 때 그 블럭을 실행
	거짓이라면 건너뛰어 else문을 실행<br/>
	```
	# 예제) 돈이 만원이상 있으면 택시를 타고 없으면 걸어가라
	money = 5000
	if money >= 10000:
		print('택시타자!')
	else:
		print('걸어가자')
	# money값을 조건문이 참이되도록 바꿔서도 실행 해 볼것
	```
	<br/><br/>

### 조건식
* 대소비교<br/>
	```
	com = 3
	user = 5

	if com <= user:
		print('참!')
	else:
		print('거짓!')

	if com >= user:
		print('참!')
	else:
		print('거짓!'
		)
	if com == user:
		print('참!')
	else:
		print('거짓!')

	if com != user:
		print('참!')
	else:
		print('거짓!')

	# if com = user:
	#	print('오류!')
	# 이 연산은 오류! 등호 하나는 '대입'을 뜻한다.
	```
	항상 등호(=)는 부호 뒤에!
	<br/>
* boolean 연산<br/>

	AND|**True**|**False**
	----|----|----
	**True**|True|False
	**False**|False|False

	OR|**True**|**False**
	----|----|----
	**True**|True|True
	**False**|Ture|False

	```
	>>NOT True
	False
	>>NOT False
	True
	```  
	<br/><br/>
### 블럭

```
if True:			  # 1. 여기서부터
    print('블럭1')
    if True:			  # 1-. 여기서부터
	print('블럭1-1')
    else:
	print('블럭1-2')	  # 1-. 여기까지가 한 블럭
else:
    print('블럭1 끝')		  #1. 여기까지가 한 블럭

#여기는 블럭 밖
print('블럭 끝')

#블럭2시작
if False:
    print('블럭2')	
    if True:
	print('블럭2-1')
    else:
	print('블럭2 끝')
else:
    print('블럭 끝')
```
<br/>
블럭1에서 처음 if문이 시작되고 들여쓰기가 끝나는지점까지가 한 블럭
블럭2에서는 블럭 2-1 조건식이 참이라도 블럭2의 조건식이 거짓이므로 if문 안으로 들어가지 못함 -> 출력되지 않음<br/><br/>

### elif

```
apple = 3
banana = 5

if apple == banana:
	print('갯수 동일!')
elif apple < banana:
	print('바나나가 더 많음!')
else
	print('사과가 더 많음!')
```
<br/>
elif는 else와 if를 합친것으로 조건이 맍지 않는 경우 다른 경우를 검사하기 위해 사용
if문이 거짓일 때 elif문 조건식을 검사
가독성을 위해 if else를 여러개 쓰는것보다 if elif else를 쓰는게 좋음<br/><br/>

### pass
조건문이 참, 거짓일 때 아무것도 안하고 싶다면<br/>

```
print('조건문 시작')

if True:
	pass
else:
	print('False')

print('조건문 끝')

조건문 시작
조건문 끝
```
<br/>
처음 pirnt 실행되고 if문이 참이므로 if문이 실행되지만 pass가 있기 때문에 아무것도 하지 않고 빠져나옴,<br/>
마지막 print 실행<br/><br/>

### 조건부 표현식
형식은
: 조건문이_참인_경우 if 조건문 else 조건문이_거짓인_경우<br/>

```
user = 5
>> user = 10 if True else user = 0
>> print(user)
10
```

조건부 표현식은 가독성에 유리하고 한 라인으로 작성할 수 있어 활용성이 좋음<br/>

1
