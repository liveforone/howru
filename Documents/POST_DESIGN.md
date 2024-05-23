# 게시글 설계

## 상세 설계
* 게시글은 **상세조회**, **내가 작성한 게시글**, **모든게시글(최신순)**,
* **다른 회원의 게시글**, **팔로워가 팔로잉 하고 있는 사람들의 게시글**, **최다키워드를 분석하여 3개의 유사 게시글 추천**,
* **랜덤 게시글** **작성자가 작성한 게시글의 수** 등에 대한 조회가 가능하며,
* 잠금된 계정의 회원의 경우 해당 회원의 게시글을 조회하려는 회원이, 해당 회원을 팔로잉하고 있는지 확인해야한다.
* 또한 게시글은 수정이 가능하며, 삭제 또한 가능하다.
* 게시글은 최대 800자까지만 가능하다. 이는 정책이다.
* 게시글은 상태를 가지며, 수정시에는 수정됨으로 표시된다. 따라서 다른 사용자가 게시글이 수정된것을 확인할 수 있다.
* 회원이 탈퇴되면 해당 회원에 속한 게시글은 삭제된다.
* 게시글은 좋아요를 누를 수 있도록 설계되었다. 좋아요 중복과 관리를 위해 따로 도메인을 떼어내어 관리하도록 하였다.
* 게시글의 좋아요 수는 likes 도메인에서 api를 호출하여 가져온다.
* 다른 사람의 활동을 조회할때, 잠금된 계정의 관해서는 다음과 같다.
* 게시글은 잠금된 계정의 게시글이어도 팔로잉을 한다면 조회가 가능하다.
* 그러나 댓글(의견)의 경우 맞팔로우한 상태여야만 열람이 가능하다.
* 게시글과 관련되어 고민한 점이나, 상세한 설계는 따로 기술하였으니 아래 링크를 참조바란다.
1. [날짜형 타입의 성능 향상](https://github.com/liveforone/howru/blob/master/Documents/DATETIME_PERFORMANCE.md)
2. [varchar vs text(LOB)](https://github.com/liveforone/howru/blob/master/Documents/VARCHAR_VS_TEXT.md)
3. [대용량 IN 쿼리 성능 향상 및 주의점](https://github.com/liveforone/howru/blob/master/Documents/BULK_IN_QUERY_PERFORMANCE.md)
4. [코드만으로 키워드 추출](https://github.com/liveforone/howru/blob/master/Documents/KEYWORD_EXTRACT_RECOMMEND.md)

## API 설계
```
[GET] /posts
[GET] /posts/{id}
[GET] /posts/{memberId}/my : 나의 글 조회
[GET] /posts/{memberId}/other : 다른 사람의 글 조회
[GET] /posts/{memberId}/followee : 내가 팔로우하는 사람들의 글 조회
[GET] /posts/recommend
[GET] /posts/random
[GET] /posts/{memberId}/count
[POST] /posts
[PATCH] /posts/{id}
[DELETE] /posts/{id}
```

## Json Body 설계
```json
[CreatePost]
{
  "writerUUID": "2753c128-9efc-4262-8196-ce62747d3287",
  "content": "post content."
}

[UpdatePostContent]
{
  "writerUUID": "1d390047-ac36-433b-b73a-8c9ec00405b9",
  "content": "update content."
}

[DeletePost]
{
  "writerUUID": "a784848d-b1ce-4b1a-946d-7b343ebdd373"
}
```

## DB 설계
```sql
create table post (
    id bigint not null auto_increment,
    writer_id binary(16),
    content VARCHAR(800) not null,
    post_state varchar(8) not null,
    createdDate BIGINT(12) not null,
    primary key (id),
    foreign key (writer_id) references Member (id) on delete cascade
);
CREATE INDEX post_content_idx ON Post (content);
```

## findPostsOfSomeone : 다른 회원의 게시글 조회 매커니즘
* 다른 회원의 게시글을 조회하는 매커니즘은 다음과 같다.
* 회원은 잠금이 가능하고, 이렇게 잠금된 계정의 경우 함부로 회원이 작성한 게시글이나 댓글(토론)을 열람할 수 없다.
* 아래와 같은 절차를 걸친다.
1. 회원이 잠금된 계정인 확인한다.
2. 잠금된 계정일 경우 해당 회원을 조회하려는 회원과 팔로우 여부를 확인한다.
3. 팔로우된 상태라면 게시글을 조회하여 리턴한다.
4. 팔로우되지 않은 상태라면 게시글 조회 불가 예외를 터뜨린다.
5. 잠금되지 않은 상태라면 게시글을 조회하여 리턴한다.

## 키워드 추출 매커니즘
* 키워드 추출한 데이터는 최신 100개의 데이터중 5개를 뽑는 방식으로 한다.

## 작성자 확인
* 해당 프로젝트에서 회원의 경우 uuid를 외부 식별자로 사용한다.
* 이는 보안의 측면에서 우수하기 때문이다. 게시글을 삭제하거나 수정할때 회원의 uuid를 사용하면 아주 안전하게 수정/삭제를 할 수 있다.
* 수정/삭제시에 클라이언트로부터 게시글의 id와 회원의 uuid를 받아온다.
* 그리고 조인을 통해 게시글 id와 회원의 uuid를 and 쿼리를 통해서 날린다.
* 이렇게 쿼리를 통해서 검증하게 되면 어플리케이션 단에서 추가적인 검증작업 없이 수정/삭제를 진행할 수 있다.
```kotlin
override fun findOneByUUIDAndWriter(id: Long, writerUUID: UUID): Post {
        return try {
            queryFactory.singleQuery {
                select(entity(Post::class))
                from(Post::class)
                fetch(Post::writer)
                join(Post::writer)
                where(col(Post::id).equal(id).and(col(Member::uuid).equal(writerUUID)))
            }
        } catch (e: NoResultException) {
            throw PostException(PostExceptionMessage.POST_IS_NULL)
        }
}
```

## 랜덤 게시글 쿼리
* 두 가지 방법을 생각해볼 수 있다.
* 첫번째는 count쿼리를 날려서 튜플의 갯수를 체크하고 어플리케이션 내부에서 1부터 튜플 총 갯수 까지 랜덤하게 돌려서 수를 뽑아 해당 id의 데이터를 조회하는 방법이고,
* 이 방법의 경우 여러값일 경우 in 쿼리로 조회하면된다.
* 두번째 방법은 실제 사용한 방법인데, RAND() 함수를 이용하는 것이다.
* 이렇게 하면 DB에서 자동으로 랜덤한 값을 리턴한다.
* jdsl에서는 rand()함수를 사용할 수 없다. 따라서 이는 jpql로 정적쿼리를 만들어 날려주어야한다.
```sql
SELECT * FROM Table ORDER BY RAND();
```
