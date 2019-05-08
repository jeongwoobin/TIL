---

layout: post
title: Python 스터디 멘토 준비 5일차
tags: Python 스터디 리스트

---

### 리스트(자료형)
* 변수를 저장할 수 있는 변수
```python
list1 = [ 1, 2, 3, 4, 5 ]
print(list1)
>> [ 1, 2, 3, 4, 5 ]
```

* 리스트 원소는 인덱스로 접근가능
```python
list2 = [ 'a', 'b', 'c', 'd', 'e' ]
print(list2[0])
>> a
print(list2[-1])
>> e
print(list2[5])
>> ?
print(list2[-6])
>> ?
```
이때 리스트의 첫번째 원소의 인덱스가 0인것에 주의!!!
인덱스가 음수일 경우 뒤에서 몇번째 원소를 가리키는지
만약 인덱스가 리스트의 크기보다 크거나 작은값이라면 오류!!!

* 인덱스로 원소의 값 변경 가능
```python
list3 = [ 1, 2, 3 ]
list3[0] = 5
print(list3)
>>[ 5, 2, 3 ]
```

* 리스트에 새로운 원소 추가(3가지)
```python
# append 메소드 사용
list4 = [ 1, 2, 3, 4, 5 ]
list4.append(6)
print(list4)
>> [ 1, 2, 3, 4, 5, 6 ]
# 새로운 리스트에 추가하기
list5 = list4 + [7]
print(list5)
>> [ 1, 2, 3, 4, 5, 6, 7 ]
# 리스트끼리 더하는것도 가능
list6 = list4 + list5
print(list6)
>> [ 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 7 ]
```

* 리스트에 있는 원소 삭제(2가지)
```python
# del 이용
list7 = [ 1, 2, 3, 4, 5 ]
del list7[2]
print(list7)
>> [ 1, 2, 4, 5 ]
# remove 메소드 사용
list7.remove(2)
print(list7)
>> [ 1, 4, 5 ]
```
	* del은 인덱스의 위치 삭제
	* remove는 해당하는 원소 삭제
		* 만약 해당 원소의 갯수가 여러개라면 가장 처음 나온 원소 삭제

* 리스트에 원소가 있는지 확인
```python
list8 = [ 1, 2, 3, 4, 5 ]
n = 3
exist = n in list8
print(exist)
>> true
```
  * in 연산은 결과값이 boolean으로 나오기 때문에 조건문에서 유용함
