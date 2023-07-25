# How r U?

# 목차
1. [프로젝트 소개](#1-프로젝트-소개)
2. [프로젝트 설계](#2-프로젝트-설계)
3. [고민](#3-고민점)

# 1. 프로젝트 소개
## 소개
* 해당 서비스는 순수 텍스트 기반 소셜 네트워크 서비스 입니다.
* 복잡한 팔로잉/맞팔로잉 처리와 여기서 발생하는 exist, case when 쿼리의 성능 문제를 해결하였습니다.
* 또한 jpa에서 복합키를 사용하며 발생하는 고질적인 문제를 해결하였습니다. 그리고 복합키를 사용하는 테이블에서 timestamp를 이용해 no-offset 페이징을 간편하게 처리하는 방법을 고안했습니다.
* 팔로잉한 사람들의 게시글을 가져오는 대용량 in 쿼리에서 성능을 향상하는 방법을 어플리케이션 단과 DB단에서 모두 고민하고 해결하였습니다.
* 머신러닝을 이용하지 않고, 코드만으로 키워드를 분석하여 관련 데이터를 추천하는 방법을 찾아내기도 하였습니다.
* 성능의 문제는 대부분 쿼리에서 발생합니다.
* 다양한 쿼리 방법들을 시도해보며 성능이 좋은 쿼리를 찾기 위해 분석하였고, 이를 적용한 코드를 확인하실 수 있습니다.
* 특히나 구독(Subscribe) 도메인과 게시글(Post) 도메인에서 확인하실 수 있습니다.
## 기술 스택
* Framework : Spring Boot 3.1.2
* Lang : Kotlin 1.9.0, Jvm17 
* Data : Spring Data Jpa & Kotlin-JDSL & MySql 
* Security : Spring Security & Jwt 
* Test : Junit5

# 2. 프로젝트 설계
## 시스템 설계
* [전체 설계]()
* [DB 설계]()
## 엔티티 설계
* [회원 설계](https://github.com/liveforone/howru/blob/master/Documents/MEMBER_DESIGN.md)
* [팔로잉(구독) 설계](https://github.com/liveforone/howru/blob/master/Documents/SUBSCRIBE_DESIGN.md)
* [게시글 설계](https://github.com/liveforone/howru/blob/master/Documents/POST_DESIGN.md)
* [좋아요 설계](https://github.com/liveforone/howru/blob/master/Documents/LIKES_DESIGN.md)
* [댓글 설계](https://github.com/liveforone/howru/blob/master/Documents/COMMENTS_DESIGN.md)
* [대댓글 설계]()

# 3. 고민점
## 기술적 고민
* [JPA에서 복합키로 조회 쿼리 최적화](https://github.com/liveforone/howru/blob/master/Documents/COMPOSITE_KEY_IN_JPA.md)
* [복합키를 사용하는 테이블에서 timestamp 기반 no-offset 페이징](https://github.com/liveforone/howru/blob/master/Documents/NO_OFFSET_IN_COMPOSITE_KEY_TABLE.md)
* [단일 쿼리로 맞팔로우 확인 방법](https://github.com/liveforone/howru/blob/master/Documents/FOLLOW_EACH_CHECK_BY_QUERY.md)
* [존재여부 확인(exist/count) 쿼리 성능 향상](https://github.com/liveforone/howru/blob/master/Documents/EXIST_VS_COUNT_QUERY.md)
* [날짜형 타입의 성능 향상](https://github.com/liveforone/howru/blob/master/Documents/DATETIME_PERFORMANCE.md)
* [varchar vs text(LOB)](https://github.com/liveforone/howru/blob/master/Documents/VARCHAR_VS_TEXT.md)
* [대용량 IN 쿼리 성능 향상 및 주의점](https://github.com/liveforone/howru/blob/master/Documents/BULK_IN_QUERY_PERFORMANCE.md)
* [코드만으로 키워드 추출](https://github.com/liveforone/howru/blob/master/Documents/KEYWORD_EXTRACT_RECOMMEND.md)
## 비즈니스적 고민
* [클린한 커뮤니티 만들기](https://github.com/liveforone/howru/blob/master/Documents/CLEAN_COMMUNITY.md)