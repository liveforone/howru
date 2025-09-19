# 날짜형 타입의 성능 향상

## 목차
* [날짜 타입은 성능에 영향을 미칠까?](#날짜-타입은-성능에-영향을-미칠까)
* [숫자타입과 성능 비교](#숫자타입과-성능-비교)
* [date와 datetime의 경우 타입에 주의하라](#date와-datetime의-경우-타입에-주의하라)
* [날짜를 숫자타입으로 파싱하는 코드 : datetime기준](#날짜를-숫자타입으로-파싱하는-코드--datetime기준)
* [날짜를 숫자타입으로 파싱하는 코드 : date기준](#날짜를-숫자타입으로-파싱하는-코드--date기준)
* [숫자타입의 날짜를 LocalDate 형식으로 변경](#숫자타입의-날짜를-localdate-형식으로-변경)

## 날짜 타입은 성능에 영향을 미칠까?
* 날짜타입은 아주 유용하게 사용된다.
* 정말 날짜를 표시하는 용도로도 사용이 되고, 정렬에도 사용할 수 있다.(물론 pk가 더 정렬하기 좋지만)
* 또한 조회시 조건절로도 사용되기도 한다. 거래내역같은 데이터의 경우 날짜는 아주 좋은 조건절 파라미터가 될것이다.
* 그런데 이렇게 흔히 사용하는 날짜타입이 성능에 영향을 미칠까?

## 숫자타입과 성능 비교
* datetime의 경우 단건조회일때는 인덱스가 들어가지만, 기간으로 잡아서 조회를 하게 되면 인덱스가 먹지 않는다.
* 또한 between, where, in 등 다양한 쿼리를 날려도 int/bigint형이 훨씬 빠른것을 테스트를 통해 확인하였다.
* 100만건이 넘어가면 varchar가 숫자타입보다 빠르다는 둥 다양한 사례가 있지만, 일단 datetime과 숫자타입을 비교할때
* 인덱스의 여부때문에 숫자타입이 훨씬 좋은 성능을 내는 것같다.
* 따라서 조건절에 날짜를 사용하게된다면 숫자타입(int/bigint)를 사용하면 좋을 것같다.

## date와 datetime의 경우 타입에 주의하라
* date의 경우 yyyy-mm--dd 만 넣기 때문에 int로 타입을 선언해도 무방하지만,
* datetime의 경우 초를 뺸다고 하더라도, yyyy--mm--dd-hh--mm를 넣게 되어 int(12) 처럼 int 타입이 불가능해진다.
* 따라서 날짜와 시간을 표시하는 datetime의 경우 bigint 를 사용하도록한다.

## 날짜를 숫자타입으로 파싱하는 코드 : datetime기준
* 한자리 숫자의 경우 앞에 0을 삽입해주었다.
* datetime을 기준으로 작성했기 때문에 Long 타입을 사용하였다.
* DB에서도 정의할때 bigint로 정의해주어야한다.
```kotlin
fun datetimeConvertToDigit(): Long {
    val now = LocalDateTime.now()
    val year = now.year.toString()
    val month = checkSingleDigit(now.monthValue)
    val day = checkSingleDigit(now.dayOfMonth)
    val hour = checkSingleDigit(now.hour)
    val minute = checkSingleDigit(now.minute)

    return return "$year$month$day$hour$minute".toLong()
}

private fun checkSingleDigit(digit: Int): String {
    return if (digit in 1..9) {
        "0${digit}"
    } else {
        digit.toString()
    }
}
```

## 날짜를 숫자타입으로 파싱하는 코드 : date기준
* yyyymmdd 의 형태로 저장된다.
```kotlin
fun getDateDigit(date: LocalDate): Int {
    val year = date.year.toString()
    val month = checkSingleDigit(date.monthValue)
    val day = checkSingleDigit(date.dayOfMonth)

    return "$year$month$day".toInt()
}
```

## 숫자타입의 날짜를 LocalDate 형식으로 변경
* 숫자타입의 날짜를 date의 경우 localDate
* datetime의 경우 localDateTime의 형태로 변경하는 함수도 필요할 수 있다. 이는 아래와 같이 정의할 수 있다.
* date 타입의 경우만 작성하였다. datetime 또한 같은 로직으로 작성하면된다.
```kotlin
fun convertDateToLocalDate(date: Int): LocalDate {
    val stringDate = date.toString()
    val year = stringDate.substring(0, 4).toInt()
    val month = stringDate.substring(4, 6).toInt()
    val day = stringDate.substring(6).toInt()

    return LocalDate.of(year, month, day)
}
```
