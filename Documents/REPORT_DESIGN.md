# 신고 설계

## 상세 설계
* 신고는 어드민과 일반 회원 모두 가능합니다.
* 클라이언트에서는 각 게시물, 댓글, 대댓글에 api를 배치하면됩니다.
* 모든 게시글, 댓글, 대댓글에는 작성자의 uuid가 포함되기 때문에 이를 이용해 신고처리합니다.

## API 설계
```
[POST] /report/member
```

## Json body 예시
```json
[ReportMember]
{
  "memberUUID": "f79e091c-2069-4153-ae07-12d8bd27d2de"
}
```