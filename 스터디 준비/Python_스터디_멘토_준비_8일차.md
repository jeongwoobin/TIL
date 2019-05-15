---

layout: post
title: Python 스터디 멘토 준비 7일차
tags: Python 스터디 딕셔너리 튜플

---

### Dictionary
```
Dictionary이름 = {
	key1 : value1, 
    key2 : value2, 
    key3 : value3
}
```
* 여러값을 key와 value를 매칭시켜 저장해놓고 key로 value를 검색하여 사용하는 구조
* 리스트와 다르게 인덱스(순서)가 없음

### Dictionary 수정
```
dict = { 'one' : 1, 'two' : 2, 'three' : 3 }
# 추가
dict['four'] = 4
dict2 = { 'five' : 5 }
dict.update(dict2)
# 수정
dict['one'] = 10
# 삭제
del(dict['two'])
dict.pop('three')
# pop같은경우 삭제한 값을 return해준다
```
* 모두 key로 수정

### Dictionary & 반복문
```
dict = { 'one' : 1, 'two' : 2, 'three' : 3 }
# key 출력
for key in dict.keys():
	print(key)
# value 출력
for value in dict.values():
	print(value)
# 둘다 한번에 출력
for key, value in dict.items():
	print('{}는 {}'.format(key, value))
# items는 앞에서 배운 enumerate와 비슷함
```

### Tuple
* 정해진 순서가 있는 리스트
    ```
    # 선언방법
    tuple1 = (1, 2, 3, 4)
    tuple2 = 1, 2, 3, 4
    print(tuple1[0])
    >> 1
    tuple1[0] = 5
    >> ERROR
    # 한번 선언한 튜플은 값을 삭제할수도, 변경할수도 없다.

    # 선언된 리스트원소들을 튜플안에 넣기
    list = [1, 2, 3, 4]
    tuple3 = tuple(list)
	```
	* packing
	```
    x = 1
    y = 2
    z = x, y
    ```

	* unpacking
	```
    x = ( 1, 2 )
    y, z = x
    ```
	
    * x, y 값 변경
	```
    # 정석
    x = 3
    y = 6
    temp = x
    x = y
    y = temp

	# packing, unpacking 활용
    x = 3
    y = 6
    x, y = y, x
    ```

	* tuple을 이용한 함수 리턴값
	```
    # tuple list
    list = [ 1, 2, 3, 4 ]
    for a in enumerate(list):
    	print('{}번째 값: {}'.format(a[0], a[1]))
    for a in enumerate(list):
    	print('{}번째 값: {}'.format(*a))
    
    # tuple dictionary
    dict = { 'one' : 1, 'two' : 2, 'three' : 3 }
    for a in dict.items():
    	print('{}은 {}'.format(a[0], a[1]))
    for a in dict.items():
    	print('{}은 {}'.format(*a))
    ```
		* 여기서 *은 '쪼개다'라는 의미로 쓰인다.
		* 하나씩 쪼개서 값 전달