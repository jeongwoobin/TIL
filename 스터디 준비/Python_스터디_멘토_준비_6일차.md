---

layout: post
title: Python 스터디 멘토 준비 6일차
tags: Python 스터디 반복문 for while

---

### 반복문
* for in list - 반복할 리스트가 있을 때
	```python
	list = [ 'a', 'b', 'c' ]
	for n in list:
		print(n)
	>> a
	b
	c
	```
	* in 뒤에있는 모든 값을 in 앞에있는 변수에 한번씩 넣어가며 리스트의 길이만큼 블럭을 실행
	* in 앞에있는 변수는 따로 선언을 해주지 않아도 바로 사용 가능

* for in range - 반복할 횟수가 정해져있을 때
	```python
	for i in range(5):
		print(i)
	>> 0
	1
	2
	3
	4
	```
	```python
	# 리스트에 순서를 출력하고싶을 때
	fruit = [ 'banana', 'apple', 'pear', 'peach' ]
	for i in range(4):
		fruitname = fruit[i]
	    print('{}번: {}'.format( i+1, fruitname ))
	>>1번: banana
	2번: apple
	3번: pear
	4번: peach
	```
	* 만약 fruit리스트에 과일이 더 추가될것이라면 for in문의 range(4)보다는 range(len(fruit))가 더 효율적이다
	* 항상 변경될 수 있는 값을 넣기보다는 길이를 넣어주는 것이 좋다.(중요!!!)

* for in enumerate - 리스트가 있는 경우 순서와 리스트의 값 둘다 전달해야할 때
	```python
	fruit = [ 'banana', 'apple', 'pear', 'peach' ]
	for i, fruitname in enumerate(fruit):
		print('{}번: {}'.format(i+1, fruitname))
	>>1번: banana
	2번: apple
	3번: pear
	4번: peach
	```
	* enumerate는 인덱스와 리스트값 2개의 값을 반환한다. 처음나온 변수에 인덱스, 그다음 변수에는 리스트값을 반환

<br/><br/>
* while - 조건이 맞다면 계속 반복
	```python
	selected = None		# 초기화
	while selected not in ['가위', '바위', '보']:
		selected = input('가위, 바위, 보 중에 선택하세요>> ')
	print('선택된 값은: {}'.format(selected))
	# 실행해보세요
	```

	또는
	```python
	patterns = ['가위', '바위', '보']
	i = 0		# 초기화
	while i<len(patterns):
		print(patterns[i])
	    i += 1
	>> 가위
	바위
	보
	```

* for문과 while문은 서로 자유롭게 바꿔서 쓸 수 있다.
	```python
	patterns = ['가위', '바위', '보']
	for i in range(len(patterns)):
	```
	* 위의 while문 예제를 for문으로 바꾼 것이다.

* break, continue
	* break는 반복문을 종료시킨다.
		```python
		list = [1,2,3,5,7,2, 5,237,55]
		for n in list:
			print(n)
		>> 3
		237
		# break 사용
		for n in list:
			print(n)
		    break
		>> 3
		```
	* continue는 반복문의 나머지 부분을 보지 않고 처음으로 돌아간다.
		```python
		for i in rnage(10):
			if i % 2 == 0:
				print(i)
			print(i)
		>> 1
		1
		3
		3
		5
		5
		7
		7
		9
		9
		```

		```python
		for i in range(10):
			if i % 2 == 0:
			continue
			print(i)
			print(i)
		>> 1
		1
		3
		3
		5
		5
		7
		7
		9
		9
		```
		* 위와 아래의 결과는 같지만 메인이되는 블럭이 눈에 더 잘 보이게 하기 위해 continue를 씀
