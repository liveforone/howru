# KTLint 적용

## 코드 포맷팅(컨벤션)
* 협업을 할때 코드 스타일이 뒤죽박죽이면 좋지 않다.
* 개인 프로젝트라면 모를까 협업에 이는 도움이 되지 않는다.
* 따라서 컨벤션을 맞추어서 포맷팅을 하면 모든 코드 스타일을 통일할 수 있다.
* 필자는 `kotlin`프로젝트에 `ktlint`를 적용했다.

## 플러그인
* 가장 유명한 코틀린 린트로는 `pinterest`에서 만든 린트가 제일 유명하다.
* 이를 보다 편하게 사용하기 위한 `wrapper`플러그인인 `org.jlleitschuh.gradle.ktlint`을 적용하면 아주 편하게 사용할 수 있다.
* 또한 `report`를 `json`형식으로 보는 것이 `txt`로 보는 것보다 편하다면 아래 `ktlint`설정을 걸어주면 된다.
```kts
plugins {
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

ktlint {
    reporters {
        reporter(ReporterType.JSON)
    }
}
```

## .editorconfig
* `.editorconfig`파일은 많은 에디터와 `ide`에서 적용을 지원하는 코드 포맷팅 파일이다.
* 이를 아래와 같이 기술하겠다.
* 필자는 들여쓰기와 탭을 공백 4자로 처리하게 하였다.
* 또한 맨 마지막 줄을 띄어쓰게 하였고, 함수 정의와 호출의 마지막 매개변수에 콤마를 붙이는 설정을 false로 두었다.
* 또한 ktlint에서 가장 맘에 들지 않는, wild-card import를 허용하지 않는 것을 false로 두었다.
```.editorconfig
root = true

[*]
charset = utf-8
end_of_line = lf
indent_size = 4
indent_style = space
trim_trailing_whitespace = true
insert_final_newline = true
max_line_length = 120
tab_width = 4

[*.{kt,kts}]
ij_kotlin_allow_trailing_comma_on_call_site=false
ij_kotlin_allow_trailing_comma=false
ktlint_standard_import-ordering=disabled
ktlint_standard_no-wildcard-imports=disabled
```

## 적용
* 이후 아래의 과정을 거쳐서 포맷팅 -> 빌드를 해준다.
* `./gradlew clean ktlintFormat`
* ide에서 빌드 실행
* 빌드는 gradle탭에 `tasks->build->build`를 실행하면 된다.
