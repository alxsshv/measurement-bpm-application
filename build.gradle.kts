plugins {
    java
    jacoco
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.sonarqube)
}

group = "com.github.alxsshv"
version = "2.0.0"
description = "Demo project for Spring Boot"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/alxsshv/arshin-client-starter")
        credentials {
            // В CI будет использоваться PACKAGES_TOKEN
            // Локально будет попытка использовать GITHUB_TOKEN или токен из gradle.properties
            username = System.getenv("GITHUB_ACTOR") ?: "alxsshv"
            password = System.getenv("PACKAGES_TOKEN") ?: System.getenv("GITHUB_TOKEN") ?: findProperty("gpr.token") as String?
        }
    }
}

dependencies {
    implementation(libs.arshin.client.starter)
    implementation(libs.spring.boot.starter.thymeleaf)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.poiji)
    implementation(libs.jaxb.impl)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testImplementation(libs.spring.boot.starter.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}

// 1. Настройка тестов
tasks.test {
    useJUnitPlatform() // Для JUnit 5
    finalizedBy(tasks.jacocoTestReport) // После тестов сразу генерируем отчет
}

// 2. Настройка самого плагина JaCoCo
jacoco {
    toolVersion = libs.versions.jacoco.get() // Актуальная версия для Java 17+
}

// 3. Настройка генерации отчета
tasks.jacocoTestReport {
    dependsOn(tasks.test) // Обязательно запускаем тесты перед генерацией отчета

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    // Связываем проверку покрытия с генерацией отчета
    finalizedBy(tasks.jacocoTestCoverageVerification)
}

// 4. Настройка проверки покрытия (Coverage Verification)
tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                // Можно использовать COUNT = LINE (строки) или INSTRUCTION (байткод инструкции)
                // INSTRUCTION считается более точным стандартом
                counter = "INSTRUCTION"
                minimum = "0.80".toBigDecimal() // 80% покрытия обязательно!
            }
        }
    }
}

sonar {
    properties {
        // Замени на свой реальный ключ проекта, который ты создал в SonarQube Cloud
        property("sonar.projectKey", "alxsshv_measurement-bpm-application")
        property("sonar.projectName", "measurement-bpm-application")

        // Пути к исходному коду и тестам
        property("sonar.sources", "src/main/java")
        property("sonar.tests", "src/test/java")

        // Указываем путь к XML-отчету JaCoCo, который генерируется в шаге 2 пайплайна
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")

        // Опционально: Исключения из анализа (чтобы SonarQube ругался на DTO, сгенерированный код и т.д.)
        // property("sonar.exclusions", "**/dto/**, **/entity/**, **/config/**")
    }
}