# 광고 설계

## 상세 설계
* 광고에 대한 생성/수정/삭제 작업은 ADMIN만 가능하다.
* 광고는 전체 조회가 거의 일어나지 않으므로 페이징을 구현하진 않았다. 
* 그러나 페이징이 없어도 성능 개선을 위해 캐시를 사용하였다.
* 다만, 어드민의 빈번한 광고 조회가 일어난다면 페이징을 추가할 필요는 있다.
* 회사명으로 검색이 가능하고, 만료된 광고의 조회가 가능하다.
* 광고는 만료일이 3개월이 지난 경우 스케줄링으로 23시 59분에 자동 삭제된다. 
* 이는 추가 갱신 여부가 있을수도 있기 때문에 3개월의 유예시간을 주었다.
* 6개월, 1년단위로 계약이 가능하며, 연장 가능하다.

## API 설계
```
[GET] /advertisement/{id}
[GET] /advertisement
[GET] /advertisement/search-company
[GET] /advertisement/expired
[GET] /advertisement/random
[POST] /advertisement/create/half
[POST] /advertisement/create/year
[PATCH] /advertisement/{id}/edit/title
[PATCH] /advertisement/{id}/edit/content
[DELETE] /advertisement/{id}/remove
```

## Json Body 예시
```json
[CreateAdvertisement]
{
  "company": "korea apple",
  "title": "buy korea apple!! - 6month ad",
  "content": "korea apple is the best apple ever in korea"
}

[UpdateAdTitle]
{
  "title": "update_ad_title"
}

[UdpateAdContent]
{
  "content": "update_ad_content"
}
```

## DB 설계
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