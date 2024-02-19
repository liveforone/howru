# 자바와 gradle 버전 변경

## intro
* 이 문서의 경우 자바 17에서 자바 21로 변경할때 작성되었다.
* 버전 변경시 문법 변경이나 deprecated 되는 것들 때문에 에러가 발생할 수 있다.
* 그에 대한 처리는 알아서 진행해야하며, 아래 프로세스들은 자바를 미리 다운로드 받고 환경변수를 설정한 후 진행하는 것을 전제로 한다.
* 또한 ide는 intellij를 기준으로 한다.

## 자바 버전 변경
1. `SHIFT+CTRL+ALT+S` 단축키로 설정에 진입한다.
2. sdk에 들어가서 새로 받은 자바를 sdk에 추가한다.
3. project란에 들어가서 sdk를 변경한다.
4. 일반 설정의 gradle 설정에 들어간다.
5. gradle jvm을 변경한다.
6. 컴파일러 설정에 들어간다
7. project byte code 버전을 변경한다.
8. build.gradle의 java 버전을 변경한다.
9. jvm target을 변경한다.
10. 만약 Unsupported Java 에러발생 시 아래의 gradle 버전 변경을 따라 한다.

## gradle 버전 변경
1. 터미널을 열고 `./gradlew -v` 명령어로 버전을 확인한다.
2. 해당 자바 버전에 맞는 gradle 버전을 찾는다.
3. `./gradlew wrapper --gradle-version 버전(ex : 9.0)` 단축키로 변경한다.