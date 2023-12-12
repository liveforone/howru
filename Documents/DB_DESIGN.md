# DB 설계

## ER-Diagram
![image](https://github.com/liveforone/howru/assets/88976237/18e37e6f-5597-4300-828f-d61042353849)

## 테이블 생성 및 제약조건 명시
### 회원 -> Member
```sql
create table member (
    id bigint not null auto_increment,
    uuid BINARY(16) not null UNIQUE,
    email varchar(255) not null,
    password varchar(100) not null,
    auth varchar(7) not null,
    nick_name VARCHAR(10) not null UNIQUE,
    member_lock varchar(3) not null,
    primary key (id)
);
CREATE INDEX uuid_idx ON member (uuid);
CREATE INDEX email_idx ON member (email);
```
### refresh-token
```sql
create table refresh_token (
    uuid BINARY(16) not null UNIQUE,
    refresh_token varchar(255),
    primary key (uuid)
);
```
### 신고상태 -> ReportState
```sql
create table report_state (
     id bigint not null auto_increment,
     member_id bigint,
     modified_state_date INT(8) not null,
     report_count integer not null,
     member_state VARCHAR(17) not null,
     primary key (id),
     foreign key (member_id) references on Member (id) on delete cascade
);
```
### 구독 -> Subscribe
```sql
create table subscribe (
    timestamp integer,
    followee_uuid binary(16) not null,
    follower_uuid binary(16) not null,
    primary key (followee_uuid, follower_uuid)
);
CREATE INDEX subscribe_timestamp_idx ON Subscribe (timestamp);
```
### 게시글 -> Post
```sql
create table post (
    id bigint not null auto_increment,
    writer_id bigint,
    content VARCHAR(800) not null,
    post_state varchar(8) not null,
    createdDate BIGINT(12) not null,
    primary key (id),
    foreign key (writer_id) references Member (id) on delete cascade
);
CREATE INDEX post_content_idx ON Post (content);
```
### 좋아요 -> Likes
```sql
create table likes (
     timestamp integer,
     member_uuid binary(16) not null,
     post_id binary(16) not null,
     primary key (member_uuid, post_id)
);
CREATE INDEX likes_timestamp_idx ON Likes (timestamp);
```
### 댓글 -> Comments
```sql
create table comments (
     id bigint not null auto_increment,
     post_id bigint,
     writer_id bigint,
     content VARCHAR(100) not null,
     comments_state varchar(8) not null,
     created_date BIGINT(12) not null,
     primary key (id)
     foreign key (writer_id) references Member (id) on delete cascade
     foreign key (post_id) references Post (id) on delete cascade
);
```
### 대댓글 -> Reply
```sql
create table reply (
     id bigint not null auto_increment,
     comment_id bigint,
     writer_id bigint,
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
* 아래는 jdsl로 구성한 no-offset 동적쿼리이다.
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