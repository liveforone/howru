# 단방향 관계에서 delete cascade 사용하기

## jpa 에서 cascade
* cascade는 위험하기 때문에 조심스럽게, 순차적으로 테이블들을 쫒아가며 파악하고 적용해야한다.
* jpa에서는 cascade를 프레임워크단에서 지원을 해주는데, 양방향관계에서만 적용가능하다.
* 필자는 양방향 관계를 선호하지 않는다. 복잡해지고, 관리하기 까다롭고, 무엇보다 필요없는 경우가 상당히 많다.
* 특히나 종속적인 api 개발보다, 종속적이지 않은 api 개발을 선호하는 경우에는 더더욱 양방향 관계는 필요가없다.
* 그런데 프레임워크의 지원을 받기위해 굳이 양방향 관계를 설정해 cascade를 거는것은 그렇게 썩 좋아보이진 않는다.

## on delete cascade
* DB에는 on delete cascade 라는 옵션이 있다.
* 이는 fk에 추가할 수 있다. 아래처럼 말이다.
```
foreign key (member_id) references Member (id) on delete cascade
```
* 이는 member 데이터가 삭제되면 위의 옵션을 추가한, member_id 를 fk로 갖는 데이터가 삭제된다.
* 이를 hibernate에서 사용가능하도록 지원해준다.

## @OnDelete
* on delete는 다양한 옵션이 있다. 실제 DB에 거는 제약처럼 set null도 있고 다양하다.
* 다 제쳐두고 OnDelete는 cascade와 달리 다음과 같은 특징이 있다.
* jpa의 cascade옵션은 삭제되야할 데이터가 10개라면 10개의 delete 쿼리가 나간다.
* 그러나 OnDelete는 그렇지 않다. 단 한개의 delete 쿼리가 나가면
* 프레임워크단이 아닌, DB에서 처리한다. 이것이 가장 큰 차이점이다.
* 이에 따라 삭제쿼리를 프레임워크에서 확인할 수 없다. 즉 hibernate 쿼리 로그가 찍히지 않는다.

## 결론
* on delete cascade 를 사용하고 싶거나, 단방향 관계에서 delete cascade를 걸고 싶거나
* 여러 delete 쿼리가 나가는 것이 마음에 들지 않는다면 @OnDelete는 아주 좋은 대안이 된다.
* 그러나 jpa의 cascade와 마찬가지로 fk의 값을 변경하거나(set null), fk가 속해있는 데이터를 삭제하는 등의 행위는
* 상당히 위험하고 신중하게 결정하여 프로그래밍 해야한다.
* 이런 저런 이유로, 안전하게 코드로 처리하기 위해서 직접 delete 처리하는 벌크 쿼리를 만들어 날릴 수도 있겠으나(이전에는 필자가 그런 방식을 썻다.)
* 이번 프로젝트에서는 OnDelete를 사용해서 delete cascade를 처리하였다.