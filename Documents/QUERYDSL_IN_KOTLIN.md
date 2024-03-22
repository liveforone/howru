# 코틀린-스프링 환경에서 QueryDsl 사용하기

## QueryDsl 대체제
* 필자는 자바-스프링을 사용할 때에는 QueryDsl을 사용하였다.
* 꽤 오랜 시간 자바-스프링을 사용하였기 때문에 QueryDsl에 익숙하여, 코틀린-스프링에도 QueryDsl을 사용하려고 하였으나, 잘 되지 않았다.
* 이런 저런 설정이 까다로웠기 때문에 라인에서 만든 Kotlin-Jdsl을 사용하였다.
* JDSL V2부터 V3까지 사용하였으나, JDSL은 쿼리 유틸 함수 등을 커스터마이징 하는 것이 까다롭고
* 버전이 업그레이드 될 수록 커스터마이징 기능들이 많이 생략되어갔다.
* 다음 글에서는 코틀린-스프링(3.2.X) 환경에서 QueryDsl을 사용하는 방법을 설명한다.
* 깊은 내용에 대해서는 읽는 독자들이 따로 검색하여 찾아보기 바란다.
* 이 글은 빠르게 QueryDsl을 사용할 수 있도록 한다.

## build.gradle setting
* 플러그인에 `kotlin("kapt") version "1.9.22"`와 같이 kapt를 추가해야한다.
* 이 `kapt`가 있어야 QueryDsl이 코틀린 환경에서도 동작한다.
* 코틀린은 어노테이션 프로세서와 궁합이 아주 않좋다..
* 또한 아래와 같이 QueryDsl을 implementation하고 kapt설정을 해준다.
```gradle.kts
//query-dsl config
implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
kapt("jakarta.annotation:jakarta.annotation-api")
kapt("jakarta.persistence:jakarta.persistence-api")
```
* 이렇게 설정을 마치고 빌드를 하면 `build/kotlin/generated/source/kapt/main`에 QClass가 생성된다.
* 아래는 `build.gradle.kts` 전문이다.
```gradle.kts
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
	kotlin("plugin.jpa") version "1.9.22"
	kotlin("kapt") version "1.9.22"
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

noArg {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

group = "howru"
version = "2.3.0"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

tasks.jar {
	enabled = false
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	//query-dsl config
	implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
	kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
	kapt("jakarta.annotation:jakarta.annotation-api")
	kapt("jakarta.persistence:jakarta.persistence-api")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "21"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

kapt {
	generateStubs = true
} 
```

## JpaQueryFactory setting
* 이제 `JpaQueryFactory`를 설정해주어야한다.
* 자바 코드와 유사하다.
```kotlin
package howru.howru.globalConfig.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDslConfig (
    @PersistenceContext
    val entityManager: EntityManager
) {
    @Bean
    fun jpaQueryFactory(): JPAQueryFactory = JPAQueryFactory(entityManager)
}
```
* 이렇게 설정을 마치면 코틀린에서 QueryDsl을 정상적으로 사용할 수 있다.