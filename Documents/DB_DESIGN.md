# DB 설계

## ER-Diagram
![스크린샷(216)](https://github.com/liveforone/howru/assets/88976237/0994b695-bf15-4ca6-8fd2-a77f8642a541)

## 테이블 생성 및 제약조건 명시
### 회원 -> Member
```sql
create table member (
    id BINARY(16) not null,
    email varchar(255) not null,
    password varchar(100) not null,
    auth varchar(7) not null,
    nick_name VARCHAR(10) not null UNIQUE,
    member_lock varchar(3) not null,
    primary key (id)
);
CREATE INDEX id_auth_idx ON member (id, auth);
CREATE INDEX email_idx ON member (email);
CREATE INDEX email_auth_idx ON member (email, auth);
```
### refresh-token
* refresh-token은 member와 1:1관계이다.
* 단순한 구조이면서 1:1관계이므로 fk(uuid)를 pk로 매핑한다.
```sql
create table refresh_token (
    id BINARY(16) not null,
    refresh_token varchar(255),
    primary key (id)
);
```
### 신고상태 -> ReportState
* report_state와 member는 1:1 관계이다.
```sql
create table report_state (
     id bigint not null auto_increment,
     member_id binary(16),
     modified_state_date INT(8) not null,
     report_count integer not null,
     member_state VARCHAR(17) not null,
     primary key (id),
     foreign key (member_id) references on Member (id) on delete cascade
);
```
### 구독 -> Subscribe
* 복합키를 갖고 있다.
* 복합키는 팔로우 되는사람(followee), 팔로우 하는 사람(follower)로 구성된다.
```sql
create table subscribe (
    timestamp integer,
    followee_id binary(16) not null,
    follower_id binary(16) not null,
    primary key (followee_id, follower_id)
);
CREATE INDEX subscribe_timestamp_idx ON Subscribe (timestamp);
CREATE INDEX followee_id_timestamp_idx ON Subscribe (followee_id, timestamp);
CREATE INDEX follower_id_timestamp_idx ON Subscribe (follower_id, timestamp);
```
### 게시글 -> Post
* post는 member와 N:1관계를 맺는다.
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
### 좋아요 -> Likes
* 복합키를 가진다. 복합키는 회원의 외부식별자(uuid)와 게시글 id로 구성된다.
* 좋아요의 경우 member_uuid를 where에 사용할 경우 인덱스가 동작한다.
* 그러나 post_id만 where 절에 사용할 경우 인덱스가 동작하지 않는다. 복합키의 pk 인덱스는 순서에 따라 member_uuid, post_id 로 구성되어 있기 때문이다.
* 따라서 post_id로 조회하는 상황이 있기에 post_id에 대한 인덱스를 만들어 주었다. 복합키를 사용할때 인덱스는 순서에 민감하므로 추가적인 인덱스 구성이 필요한지 살펴보아야한다.
```sql
create table likes (
     timestamp integer,
     member_id binary(16) not null,
     post_id bigint not null,
     primary key (member_id, post_id)
);
CREATE INDEX likes_post_id_idx ON Likes (post_id);
CREATE INDEX likes_timestamp_idx ON Likes (timestamp);
```
### 댓글 -> Comments
* 댓글은 회원(member)테이블과 N:1 관계를 맺는다.
* 댓글은 게시글(post) 테이블과 N:1 관계를 맺는다. 
```sql
create table comments (
     id bigint not null auto_increment,
     post_id bigint,
     writer_id binary(16),
     content VARCHAR(100) not null,
     comments_state varchar(8) not null,
     created_date BIGINT(12) not null,
     primary key (id)
     foreign key (writer_id) references Member (id) on delete cascade
     foreign key (post_id) references Post (id) on delete cascade
);
```
### 대댓글 -> Reply
* 대댓글은 댓글(Comments)과 N:1 관계를 맺는다.
* 대댓글은 회원(Member)와 N:1 관계를 맺는다.
```sql
create table reply (
     id bigint not null auto_increment,
     comment_id bigint,
     writer_id binary(16),
     content VARCHAR(100) not null,
     reply_state varchar(8) not null,
     created_date BIGINT(12) not null,
     primary key (id)
     foreign key (comment_id) references comments (id) on delete cascade
     foreign key (writer_id) references member (id) on delete cascade
);
```
### 광고 -> Advertisement
```sql
create table advertisement (
     id bigint not null auto_increment,
     company varchar(255) not null,
     title varchar(255) not null,
     content VARCHAR(800) not null,
     created_date INT(8) not null,
     end_date INT(8) not null,
     primary key (id)
);
CREATE INDEX advertisement_company_idx ON Advertisement (company);
CREATE INDEX advertisement_end_date_idx ON Advertisement (end_date);
```

## no-offset 페이징
* 페이징 성능을 향상하기 위해 no-offset 방식으로 페이징 처리한다.
* 이에 따라 동적쿼리 구성이 필요하다.
* 아래는 jdsl v2로 구성한 no-offset 동적쿼리이다.
* 현재 정렬은 desc이기 때문에 asc를 사용한다면 lessThan을 greaterThan으로 변경한다.
* 정책은 lastId가 null 일경우 첫 페이지로 인식하고
* 그 이외에는 lastId보다 작은 id에 한해 조회한다.
```kotlin
private fun <T> SpringDataCriteriaQueryDsl<T>.ltLastUUID(lastId: Long?): PredicateSpec? {
        return lastId?.let { and(col(엔티티::id).lessThan(it)) }
}
```

## 복합키 주의 사항
* 복합키는 인덱스의 순서를 반드시 주의하여한다.
* 이는 쿼리를 짤때 큰 성능 차이를 유발할 수 있다.