# 대댓글 설계

## 상세 설계
* 대댓글은 수정, 삭제가 가능합니다. 사실상 댓글과 거의 대부분의 기능이 유사합니다.
* 대댓글 또한 상태가 존재합니다. 수정이 일어나게되면 수정됨으로 상태가 변경되어, 다른 사용자가 수정된 대댓글이라는 것을 알 수 있습니다.
* 대댓글은 15개씩 페이징 됩니다.
* 다른 사람의 프로필을 통해 대댓글을 조회할 순 없습니다. 댓글(의견)의 경우에만 가능합니다.

## API 설계
```
[GET] /reply/detail/{uuid} : 대댓글 상세
[GET] /reply/belong/writer/{writerUUID} : 사용자가 작성한 대댓글, 작성자만 접근 가능합니다.(프론트에서 제어)
[GET] /reply/belong/comment/{commentUUID} : 댓글에 속한 대댓글
[POST] /reply/create
[PUT] /reply/edit
[DELETE] /reply/delete
```

## Json Body 예시
```json
[CreateReply]
{
  "writerUUID": "b05a66cd-0a55-45b8-918f-a74820d96d64",
  "commentUUID": "b2bd94bd-dfcf-4303-82af-763572826284",
  "content": "test_reply"
}

[UpdateReplyContent]
{
  "uuid": "5b6dbc59-4892-4fb2-9e72-78d136bcee49",
  "writerUUID": "b05a66cd-0a55-45b8-918f-a74820d96d64",
  "content": "updated reply"
}

[DeleteReply]
{
  "uuid": "7e93d552-ce25-4c4d-9147-db12affad9f3",
  "writerUUID": "70033e13-275a-426d-b8bf-50b264a3e689"
}
```

## DB 설계
```sql
create table reply (
     id bigint not null auto_increment,
     uuid BINARY(16) not null UNIQUE,
     comment_id bigint,
     writer_id bigint,
     content VARCHAR(100) not null,
     reply_state varchar(255) not null,
     created_date BIGINT(12) not null,
     primary key (id)
     foreign key (comment_id) references comments (id) on delete cascade
     foreign key (writer_id) references member (id) on delete cascade
);
CREATE INDEX uuid_idx ON reply (uuid);
```