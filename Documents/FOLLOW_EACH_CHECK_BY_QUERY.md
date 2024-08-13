# 단일 쿼리로 맞팔로우 확인 방법

## 목차
* [테이블 간단설명](#테이블-간단설명)
* [맞팔로우 조건](#맞팔로우-조건)
* [맞팔로우 사용범위](#맞팔로우-사용범위)
* [간단한 맞팔로우 확인 쿼리](#간단한-맞팔로우-확인-쿼리)
* [최적화한 맞팔로우 확인 쿼리](#최적화한-맞팔로우-확인-쿼리)
* [case와 exist를 사용하자.](#case와-exist를-사용하자)
* [case when 쿼리 주의점](#case-when-쿼리-주의점)
* [결론](#결론)

## 테이블 간단설명
* 이 프로젝트에서 팔로잉 테이블은 followeeUUID와 followerUUID가 복합키로 되어있는 구조를 가진다.
* 연관관계는 존재하지 않고, 복합키로 존재하기 때문에 중복발생확률은 없다.

## 맞팔로우 조건
* 그렇다면 이러한 테이블 구조에서 맞팔로우는 어떻게 찾을 수 있을까?
* a 튜플에 followeeUUID로 1이, followerUUID로 2가 있다고 가정하자.
* b 튜플에 followeeUUID로 2가, followerUUID로 1이 있다면
* 회원 1과 회원2는 맞팔로우 상태이다.

## 맞팔로우 사용범위
* 맞팔로우가 되어있는지 아닌지는 언제 사용할까?
* 해당 프로젝트에서는 프로필 잠금이 가능하다.
* 다른 사용자의 게시글을 확인할때 해당 사용자가 프로필 잠금을 걸어놓았다면
* 맞팔로우를 확인하여 프로필 접근에 대한 권한을 부여할때 사용된다.

## 간단한 맞팔로우 확인 쿼리
* 그렇다면 맞팔로우 쿼리는 어떻게 구성할 수 있을까?
* 제일 쉬운 방법은 쿼리를 두번날려서 `a.followeeUUID = b.followerUUID`이면서
* `a.followerUUID = b.followeeUUID`이면 맞팔로우로 처리하는것이다.
* 그러나 이러한 방식은 성능이 떨어질것이다.

## 최적화한 맞팔로우 확인 쿼리
* 단건의 쿼리로 처리하여 성능을 높이고,
* 필요하지 않은 속성들을 가져오지 않게 쿼리를 짠다면 필자가 원하는 최적화된 쿼리가 완성될것이다.

## case와 exist를 사용하자.
* `exist`를 사용하면 튜플이 존재하는지 존재하지 않는지 알 수 있다.
* 불필요한 속성을 가져오지 않기 때문에 필자의 요구사항에 딱 맞다.
* 단건으로 처리하기 위해서는 `case when` 쿼리를 사용해 조건을 맞추면 된다.

## case when 쿼리 주의점
* `case when` 쿼리는 절대로 where절에 사용하면 안된다.
* where절에 사용하게 되면 성능이 어마어마하게 떨어진다.
* DB는 같은 쿼리의 경우 실행계획을 세우고 재활용한다.
* 그러나 where절에 case를 사용하게 되면 매번 조회할 때마다 실행계획을 세운다. 따라서 성능이 떨어진다.

## 결론
* 결과적으로 exist 서브쿼리로 해결하였다.
* select 1을 이용해 상수를 반환하도록하여 처리하였다.
* jdsl로는 이러한 쿼리를 짜는것이 불가능하여 jpql을 이용해 정적쿼리를 직접 짜서 사용했다.
```sql
SELECT CASE WHEN EXISTS (
    SELECT 1 FROM Subscribe s1
    WHERE s1.followeeUUID = :followeeUUID
    AND s1.followerUUID = :followerUUID
) AND EXISTS (
    SELECT 1 FROM Subscribe s2
    WHERE s2.followeeUUID = :followerUUID
    AND s2.followerUUID = :followeeUUID
) THEN true ELSE false END
```
