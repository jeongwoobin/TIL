---

layout: post
title: Python 스터디 멘토 준비 7일차
tags: Python 스터디 모듈 random

---

### 모듈
```
# ex)
import math

# 원넓이
r = 10
circle = math.pi * r * r

# 올림
math.ceil(1.3)
# 내림
math.floor(2.5)
# 반올림
math.round(2.5)
```
* 가시성 - 연관된 함수나 변수를 모아놓음
* 효율성 - 미리 만들어놓고 필요할 때 찾아서 쓰면 됨

### 모듈 만들어서 쓰기
```
# my_module.py
def random_rsp():
    import random
    return random.choice(['가위', '바위', '보'])
s = '가위'
r = '바위'
p = '보'

# use_module.py
import my_module
selected = my_module.random_rsp()
print(selected)
print('가위?', mymodule.s == selected)
```
* 모듈을 만들어서 쓸 때에는 모듈파일과 실행코드파일이 같은 폴더에 있어야 한다.

### random
```
import random

rand1 = random.random()
print(rand1)
>> 0이상 1미만의 랜덤 수(난수)

rand2 = random.randrange(1,10)
print(rand2)
>> 1이상 10미만의 난수

arr = ['a', 'b', 'c']
rand3 = random.choice(arr);
print(rand3)
>> arr에서 랜덤으로 뽑은 원소

arr = ['a', 'b', 'c']
rand4 = random.shuffle(arr)
print(arr)
>> 랜덤으로 섞인 arr

arr = ['a', 'b', 'c']
rand5 = random.choice(arr)
>> 랜덤으로 원소하나 뽑아서 출력
```
* 랜덤은 많이쓰이기 때문에 잘 연습해두는게 좋음
<br/><br/>
### 실습)
* 1~100 사이의 난수를 맞출 때 까지 반복하는 up&down 게임을 만들어보자
    * 조건1 게임코드는 모듈로 만들고 실행코드만 메인에 작성
    * 조건2 정답은 랜덤으로
    * 조건3 오답은 up, down으로만 대답
    * 조건4 정답을 맞췄을 경우 정답과 도전횟수 표시