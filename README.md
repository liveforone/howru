# How r U?

# 목차

# 1. 프로젝트 소개
## 소개
* 해당 서비스는 텍스트 기반 소셜 네트워크 서비스 입니다.
* 복잡한 팔로잉/맞팔로잉 처리와 여기서 발생하는 exist, case when 쿼리의 성능 문제를 해결하였습니다.
* 또한 jpa에서 복합키를 사용하며 발생하는 고질적인 문제를 해결하였습니다.
* 팔로잉한 사람들의 게시글을 가져오는 대용량 in 쿼리에서 성능을 향상하는 방법을 어플리케이션 단과 DB단에서 모두 고민하고 해결하였습니다.
## 기술 스택
* Framework : Spring Boot 3.1.1
* Lang : Kotlin 1.9.0, Jvm17 
* Data : Spring Data Jpa & Kotlin-Jdsl & MySql 
* Security : Spring Security & Jwt 
* Test : Junit5

# 2. 프로젝트 설계
## 시스템 설계
* [전체 설계]()
* [DB 설계]()
## 엔티티 설계
* [회원 설계](https://github.com/liveforone/howru/blob/master/Documents/MEMBER_DESIGN.md)
* [팔로잉(구독) 설계](https://github.com/liveforone/howru/blob/master/Documents/SUBSCRIBE_DESIGN.md)

# 3. 고민점
* [JPA에서 복합키로 조회 쿼리 최적화](https://github.com/liveforone/howru/blob/master/Documents/COMPOSITE_KEY_IN_JPA.md)
* [단일 쿼리로 맞팔로우 확인 방법](https://github.com/liveforone/howru/blob/master/Documents/FOLLOW_EACH_CHECK_BY_QUERY.md)
* [존재여부 확인(exist/count) 쿼리 성능 향상](https://github.com/liveforone/howru/blob/master/Documents/EXIST_VS_COUNT_QUERY.md)
* [varchar vs text(LOB)]()
* [팔로잉한 사람이 등록한 게시글 조회방법]()
* [대용량 IN 쿼리 성능 향상 및 주의점]()