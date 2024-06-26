# 단방향 관계에서 delete cascade 사용하기

## 목차

- [단방향 관계에서 delete cascade 사용하기](#단방향-관계에서-delete-cascade-사용하기)
    - [목차](#목차)
    - [jpa 에서 cascade](#jpa-에서-cascade)
    - [on delete cascade](#on-delete-cascade)
    - [@OnDelete](#ondelete)
    - [결론](#결론)

## jpa 에서 cascade

- `cascade`는 위험하기 때문에 조심스럽게, 순차적으로 테이블들을 쫒아가며 파악하고 적용해야한다.
- jpa에서는 `cascade`를 프레임워크단에서 지원을 해주는데, 양방향관계에서만 적용가능하다.
- 필자는 양방향 관계를 선호하지 않는다. 복잡해지고, 관리하기 까다롭고, 무엇보다 필요없는 경우가 상당히 많다.
- 특히나 종속적인 api 개발보다, 종속적이지 않은 api 개발을 선호하는 경우에는 더더욱 양방향 관계는 필요가없다.
- 양방향보다는 단방향을 지향해야하는 이유에 대해서는 이 글을 읽는 독자가 직접 찾아보라.
- 그런데 프레임워크의 지원을 받기위해 굳이 양방향 관계를 설정해 `cascade`를 거는것은 그렇게 썩 좋아보이진 않는다.

## on delete cascade

- DB에는 `on delete cascade` 라는 옵션이 있다.
- 이는 fk에 추가할 수 있다. 아래처럼 말이다.

```
foreign key (member_id) references Member (id) on delete cascade
```

- 이는 member 데이터가 삭제되면 위의 옵션을 추가한, member_id 를 fk로 갖는 데이터가 삭제된다.
- 이를 hibernate에서 사용가능하도록 지원해준다.

## @OnDelete

- `on delete`는 다양한 옵션이 있다. `set null`등 다양한 옵션이 있다.
- 다 제쳐두고 `OnDelete`는 `cascade`와 달리 다음과 같은 특징이 있다.
- jpa의 `cascade`옵션은 삭제되어야 할 데이터가 10개라면 10개의 delete 쿼리가 나간다.
- 그러나 `OnDelete`는 그렇지 않다. 단 한개의 delete 쿼리가 나가면
- 프레임워크단이 아닌, DB에서 처리한다. 이것이 가장 큰 차이점이다.
- 이에 따라 삭제 쿼리를 프레임워크에서 확인할 수 없다. 즉 hibernate 쿼리 로그가 찍히지 않는다.
- 이에 따라서 어떤 테이블이 같이 삭제되었는지 로그를 통해서 파악할 순 없다.

## 결론

- `on delete cascade`를 사용하고 싶거나, 단방향 관계에서 `delete cascade`를 걸고 싶거나
- 여러 delete 쿼리가 나가는 것이 마음에 들지 않는다면 `@OnDelete`는 아주 좋은 대안이 된다.
- 그러나 jpa의 `cascade`와 마찬가지로 fk의 값을 변경하거나(set null), fk가 속해있는 데이터를 삭제하는 등의 행위는 상당히 위험하고 신중하게 결정하여 프로그래밍 해야한다.
- 이런 저런 이유로, 안전하게 코드로 처리하기 위해서 직접 delete 처리하는 벌크 쿼리를 만들어 날릴 수도 있겠으나(이전에는 필자가 그런 방식을 썻다.)
- 이번 프로젝트에서는 `OnDelete`를 사용해서 `delete cascade`를 처리하였다.
- 안전하게만 사용한다면, 또한 테이블을 삭제할 때, 다른 어떤 테이블이 같이 삭제되는지 인지하고 프로그래밍한다면 큰 문제가 없다.
