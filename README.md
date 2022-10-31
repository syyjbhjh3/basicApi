### API 구현

swagger : http://localhost:8080/swagger-ui/index.html

JPA를 사용해보고자 기본적인 Api구성, Member를 통한 회원가입, 로그인시 jwt토큰 발급, Api 호출기능

사용기술 : Java11, Spring Boot2.6.7, JPA, H2, Gradle


#### 1. 회원가입 (signup)

###### parameter
```
{
    "userId": "",
    "password": "",
    "name": "",
    "regNo": ""
}
```

###### response
```
{
  "status": "OK",
  "message": "회원가입",
  "data": ""
}
```

 - 가입가능 유저만 가입되며 비밀번호는 단방향, 주민등록번호는 양방향 암호화 처리 


#### 2. 로그인 (login)

###### parameter
```
{
    "userId": "",
    "password": ""
}
```

###### response
```
{
  "status": "OK",
  "message": "로그인",
  "data": {
    "type": "BEARER",
    "token": ""
  }
}
```
 - 가입된 유저의 한해 token 발급 


#### 3. 내 정보 보기 (me)

###### parameter

###### response
```
{
  "status": "OK",
  "message": "내정보",
  "data": {
    "userNo": ,
    "userId": "",
    "name": "",
    "regNo": ""
  }
}
```
 - token 검증 후 token의 회원정보 조회 및 전송
