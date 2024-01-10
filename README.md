# How r U?

# 목차
1. [프로젝트 소개](#1-프로젝트-소개)
2. [프로젝트 설계](#2-프로젝트-설계)
3. [고민](#3-고민점)

# 1. 프로젝트 소개
## 소개
* 해당 서비스는 순수 텍스트 기반 소셜 네트워크 서비스 입니다.
* 보안이 중요한 회원테이블의 내/외부 식별자를 구분하여 보안성을 높였습니다.
* 복잡한 팔로잉/맞팔로잉 처리와 여기서 발생하는 exist, case when 쿼리의 성능 문제를 해결하였습니다.
* 또한 jpa에서 복합키를 사용하며 발생하는 고질적인 문제를 해결하였습니다. 그리고 복합키를 사용하는 테이블에서 timestamp를 이용해 no-offset 페이징을 간편하게 처리하는 방법을 고안했습니다.
* 팔로잉한 사람들의 게시글을 가져오는 대용량 in 쿼리에서 성능을 향상하는 방법을 어플리케이션 단과 DB단에서 모두 고민하고 해결하였습니다.
* 머신러닝을 이용하지 않고, 코드만으로 키워드를 분석하여 관련 데이터를 추천하는 방법을 찾아내기도 하였습니다.
* 성능의 문제는 대부분 쿼리에서 발생합니다.
* 다양한 쿼리 방법들을 시도해보며 성능이 좋은 쿼리를 찾기 위해 분석하였고, 이를 적용한 코드를 확인하실 수 있습니다.
* 특히나 구독(Subscribe) 도메인과 게시글(Post) 도메인에서 확인하실 수 있습니다.
* 모든 문서는 Documents 디렉토리에 존재합니다. 코드 최상단에 매핑되어있는 문서 id를 통해 문서를 쉽게 검색하고 유지보수 할 수 있습니다.
## 기술 스택
* Framework : Spring Boot 3.2.1
* Lang : Kotlin 1.9.20, Jvm17 
* Data : Spring Data Jpa | Kotlin-JDSL-2.2.1.RELEASE & 3.2.0 | MySql 8.X 
* Security : Spring Security | Jwt(jjwt) 0.11.5 
* Test : Junit5

# 2. 프로젝트 설계
## 시스템 설계
* [1 : 전체 설계](https://github.com/liveforone/howru/blob/master/Documents/DESIGN.md)
* [2 : DB 설계](https://github.com/liveforone/howru/blob/master/Documents/DB_DESIGN.md)
* [3 : rest-ful한 api 설계](https://github.com/liveforone/howru/blob/master/Documents/REST_FUL_API_DESIGN.md)
## 엔티티 설계
* [4 : 회원 & JWT 설계](https://github.com/liveforone/howru/blob/master/Documents/MEMBER_DESIGN.md)
* [신고상태 설계](https://github.com/liveforone/howru/blob/master/Documents/REPORT_STATE_DESIGN.md)
* [팔로잉(구독) 설계](https://github.com/liveforone/howru/blob/master/Documents/SUBSCRIBE_DESIGN.md)
* [게시글 설계](https://github.com/liveforone/howru/blob/master/Documents/POST_DESIGN.md)
* [좋아요 설계](https://github.com/liveforone/howru/blob/master/Documents/LIKES_DESIGN.md)
* [댓글 설계](https://github.com/liveforone/howru/blob/master/Documents/COMMENTS_DESIGN.md)
* [대댓글 설계](https://github.com/liveforone/howru/blob/master/Documents/REPLY_DESIGN.md)
* [광고 설계](https://github.com/liveforone/howru/blob/master/Documents/AD_DESIGN.md)

# 3. 고민점
## 기술적 고민
* [내부 식별자와 외부 식별자 구분](https://github.com/liveforone/howru/blob/master/Documents/INTERNAL_EXTERNAL_PK.md)
* [단방향 관계에서 delete cascade 사용하기](https://github.com/liveforone/howru/blob/master/Documents/ONE_WAY_CASCADE.md)
* [JPA에서 복합키로 조회 쿼리 최적화](https://github.com/liveforone/howru/blob/master/Documents/COMPOSITE_KEY_IN_JPA.md)
* [복합키를 사용하는 테이블에서 timestamp 기반 no-offset 페이징](https://github.com/liveforone/howru/blob/master/Documents/NO_OFFSET_IN_COMPOSITE_KEY_TABLE.md)
* [단일 쿼리로 맞팔로우 확인 방법](https://github.com/liveforone/howru/blob/master/Documents/FOLLOW_EACH_CHECK_BY_QUERY.md)
* [존재여부 확인(case-exist/count) 쿼리 성능 향상](https://github.com/liveforone/howru/blob/master/Documents/EXIST_VS_COUNT_QUERY.md)
* [날짜형 타입의 성능 향상](https://github.com/liveforone/howru/blob/master/Documents/DATETIME_PERFORMANCE.md)
* [varchar vs text(LOB)](https://github.com/liveforone/howru/blob/master/Documents/VARCHAR_VS_TEXT.md)
* [대용량 IN 쿼리 성능 향상 및 주의점](https://github.com/liveforone/howru/blob/master/Documents/BULK_IN_QUERY_PERFORMANCE.md)
* [코드만으로 키워드 추출](https://github.com/liveforone/howru/blob/master/Documents/KEYWORD_EXTRACT_RECOMMEND.md)
* [DB 데이터 사이즈 맞추기](https://github.com/liveforone/howru/blob/master/Documents/DB_DATA_STURCTURE_SIZE.md)
* [어플리케이션 캐시를 활용해 성능향상하기](https://github.com/liveforone/howru/blob/master/Documents/CACHE_FOR_PERFORMANCE.md)
* [정지 기간이 있는 계정 정지 방법](https://github.com/liveforone/howru/blob/master/Documents/HOW_TO_SUSPEND_USER.md)
* [추적가능한 예외처리](https://github.com/liveforone/howru/blob/master/Documents/TRACEABLE_EXCEPTION.md)
* [controller advice와 똑같은 예외처리 in 필터](https://github.com/liveforone/howru/blob/master/Documents/FILTER_ERR_HANDLING.md)
* [refresh token을 이용한 jwt 토큰 재발급 매커니즘](https://github.com/liveforone/howru/blob/master/Documents/JWT_TOKEN_REISSUE.md)
* [적절한 로그를 기록하는 법](https://github.com/liveforone/howru/blob/master/Documents/HOW_TO_RECORD_PROPER_LOG.md)
## 비즈니스적 고민
* [클린한 커뮤니티 만들기](https://github.com/liveforone/howru/blob/master/Documents/CLEAN_COMMUNITY.md)