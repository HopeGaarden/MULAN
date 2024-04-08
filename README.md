<img src="https://capsule-render.vercel.app/api?type=waving&color=2A64A5&height=150&section=header" />

# 

# Blue Rose

![블루로즈-4_3-발표자료-001](https://github.com/HopeGaarden/BlueRose/assets/62228433/2362c634-4c25-4166-8fde-38356b341f7c)

<br>

### 프로젝트 제안서

![003](https://github.com/HopeGaarden/BlueRose/assets/62228433/e334a2b2-32e8-4bbe-8435-5b53f40a65d0)
![004](https://github.com/HopeGaarden/BlueRose/assets/62228433/fb7c132b-cde1-42d4-866b-ef8457aac844)

<br>

### 기술 스택 선정
<img width="340" alt="image" src="https://github.com/HopeGaarden/BlueRose/assets/62228433/2812150e-a9d4-49b1-8c6d-da452a2238ab">

MySQL: 객체 지향적으로 데이터를 다루기 위해 JPA를 사용할 예정입니다. MySQL이 JPA의 구현체(Hibernate)를 지원해주기 때문에 JPA와 결합해 데이터 처리 및 관리를 안전하고 편하게 할 수 있어서 선정했습니다.

Redis: 프로젝트 특성상 진단서 정보를 받기 때문에 보안에 민감한 정보들이 데이터베이스에 존재하게 됩니다. 따라서 보안상 토큰의 만료기간을 짧게 지정할 예정입니다. 토큰이 저장된 데이터의 유효성 판단 및 재발급을 위해 자주 접근해야 하므로
빠른 읽기 및 쓰기 속도를 자랑하는 인-메모리 데이터베이스인 Redis를 택했습니다.

<br>

### 프로젝트 일정
![006](https://github.com/HopeGaarden/BlueRose/assets/62228433/34be93e6-c437-44f3-af64-14f04f751bdd)


<br>

### 요구사항 명세서

![001](https://github.com/HopeGaarden/BlueRose/assets/62228433/07f42d43-22c7-4ef1-9401-14beaf8d8cc9)
![002](https://github.com/HopeGaarden/BlueRose/assets/62228433/e8426d22-48ba-40c6-8d62-3f34d35d4732)

<br>
<br>

# Project Architecture
이미지


<br><br>

<div align="center">
  <img src="https://img.shields.io/badge/Java17-000000?style=flat-square&logo=java&color=F40D12">
  <img src="https://img.shields.io/badge/Spring_Boot_3-0?style=flat-square&logo=spring-boot&logoColor=white&color=%236DB33F">
  <img src="https://img.shields.io/badge/MySQL_8-0?style=flat-square&logo=mysql&logoColor=white&color=4479A1">
  <img src="https://img.shields.io/badge/Nginx-0?style=flat-square&logo=nginx&logoColor=white&color=009639">
  <img src="https://img.shields.io/badge/Hibernate-0?style=flat-square&logo=hibernate&logoColor=white&color=%2359666C">
  <img src="https://img.shields.io/badge/Amazon_EC2-0?style=flat-square&logo=amazon-ec2&logoColor=white&color=%23FF9900">
  <br/>
  <img src="https://img.shields.io/badge/Amazon_CloudWatch-0?style=flat-square&logo=amazon-cloudwatch&logoColor=white&color=%23FF4F8B">
  <img src="https://img.shields.io/badge/OAuth2-0?style=flat-square&logo=oauth2&logoColor=white&color=%23000000">
  <img src="https://img.shields.io/badge/Gradle-0?style=flat-square&logo=gradle&logoColor=white&color=%2302303A">
  <img src="https://img.shields.io/badge/Swagger-0?style=flat-square&logo=Swagger&logoColor=white&color=%2385EA2D">
  <img src="https://img.shields.io/badge/GitHub%20Actions-0?style=flat-square&logo=GitHub%20Actions&logoColor=white&color=%232088FF">
  <img src="https://img.shields.io/badge/JUnit5-0?style=JUnit5-square&logo=junit5&logoColor=white&color=%2325A162">
</div>
<br/>


<img src="https://capsule-render.vercel.app/api?type=waving&color=2A64A5&height=150&section=footer" />
