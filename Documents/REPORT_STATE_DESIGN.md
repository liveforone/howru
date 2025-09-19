# 신고상태 설계

## 상세 설계
* 신고 상태는, 기존에 회원에서 관리하던 신고 상태를 정규화하여 분리해낸 테이블입니다.
* 정지 기간이 있는 회원의 신고를 다루기 위해서는 최소 3개의 컬럼이 필요하기에 분리하였습니다.
* 회원 가입시 신고상태 데이터는 자동으로 생성됩니다.
* 신고는 어드민과 일반 회원 모두 신고를 요청할 수 있습니다.
* 클라이언트에서는 각 게시물, 댓글, 대댓글에 신고 api를 배치하면됩니다.
* 모든 게시글, 댓글, 대댓글에는 작성자의 uuid가 포함되기 때문에 이를 이용해 신고처리합니다.
* 신고는 3건 이상마다 한 달 정지 -> 6달 정지 -> 영구 정지 순으로 이어집니다.
* 누적 신고횟수는 삭제되지 않으며, 지속적인 신고가 이루어질 경우 영구 정지까지 가능합니다.
* 신고 기능에 대한 설명은 [기능 문서](https://github.com/liveforone/howru/blob/master/Documents/HOW_TO_SUSPEND_USER.md)를 따로 만들었으니, 해당 문서에서 확인 바랍니다.

## API 설계
```
[GET] /reports?member-id={memberId}
[POST] /reports
```

## Json body 예시
```json
[ReportMember]
{
  "memberId": "f79e091c-2069-4153-ae07-12d8bd27d2de"
}
```

## DB 설계
```sql
create table report_state (
     id bigint not null auto_increment,
     member_id UUID,
     modified_state_date integer not null,
     report_count integer not null,
     member_state VARCHAR(17) not null,
     primary key (id),
     foreign key (member_id) references on Member (id) on delete cascade
);
```
