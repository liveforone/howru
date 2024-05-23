# 광고 설계

## 상세 설계
* 광고에 대한 생성/수정/삭제 작업은 ADMIN만 가능하다.
* 광고는 전체 조회가 거의 일어나지 않으므로 페이징을 구현하진 않았다.
* 회사명으로 검색이 가능하고, 만료된 광고의 조회가 가능하다.
* 광고는 만료일이 3개월이 지난 경우 스케줄링으로 23시 59분에 자동 삭제된다.
* 이는 추가 갱신 여부가 있을수도 있기 때문에 3개월의 유예시간을 주었다.
* 6개월, 1년단위로 계약이 가능하며, 연장 가능하다.

## 캐싱을 적용하지 않은 이유
* 광고는 단건 조회가 자주 일어남에도 캐싱을 적용하지 않았다.
* 광고에서 가장 많이 호출되는 api는 광고 랜덤 api인데, 랜덤으로 뽑는 방법은 두가지 경우를 생각해볼 수 있다.
* 첫번째는 광고 데이터 갯수를 파악하는 쿼리를 날린 후, 데이터 갯수에서 나올 수 있는 랜덤 id를 뽑고 이를 가지고 조회쿼리를 다시 날려서 조회한다.
* 두번째는 랜덤으로 한 개의 광고를 조회하는 쿼리를 날린다.
* 첫번째 경우에는 캐시를 사용할 수 있다. 랜덤 id를 가지고 먼저 캐시에서 찾고 없을 경우 조회 쿼리를 날리면 되기 때문이다.
* 그러나 두번째 경우는 db 자체에서 돌리는 랜덤 쿼리이기 때문에 캐싱을 사용할 수 없다.(DB단에서 동작하고 어플리케이션 단과는 연관이 없는 쿼리이다)
* 그러나 성능 적 효율을 따졋을때, 캐시에 없을경우 첫번째 케이스는 두번의 쿼리가 날라간다. 이러한 이유로 한 번의 간단한 쿼리만 날리는 두번째 방법을 채택하게 되었다.
* 또한 단건 조회는 성능에 큰 영향을 미치지 않으므로, 이를 선택했다.

## API 설계
```
[GET] /advertisement/{id}
[GET] /advertisement
[GET] /advertisement/search-company
[GET] /advertisement/expired
[GET] /advertisement/random
[POST] /advertisement/create/half
[POST] /advertisement/create/year
[PATCH] /advertisement/{id}/edit
[DELETE] /advertisement/{id}
```

## Json Body 예시
```json
[CreateAdvertisement]
{
  "company": "korea apple",
  "title": "buy korea apple!! - 6month ad",
  "content": "korea apple is the best apple ever in korea"
}

[UpdateAdvertisement]
{
  "title": "update_ad_title",
  "content": "update_ad_content",
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
