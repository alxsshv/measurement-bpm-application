# AGENTS.md

Spring Boot 3.5.11 (Java 17, Gradle) app for generating verification reports for ФГИС Аршин and ФСА. Persistence is JSON-file based, not a database.

## Build & test

- Build/test: `./gradlew test` (CI also runs `jacocoTestReport`, `jacocoTestCoverageVerification`, `sonar`)
- Single test class: `./gradlew test --tests "com.github.alxsshv.measurementbpmapplication.employee.repository.EmployeeStorageTest"`
- Run app: `./gradlew bootRun` (serves on port **9090**)
- JaCoCo coverage gate: **80% instruction minimum** (`build.gradle.kts`). `gradlew test` will fail if new code pushes coverage below this — write tests for anything you add.


## Architecture / conventions

- Domain packages under `src/main/java/com/github/alxsshv/...`: `employee`, `reports` (fsa/arshin report generation), `filestorage` (file upload/download + periodic cleanup scheduler), `reportscheduler` (scheduled report tasks), `common`.
- Persistence is file-based. Each repository is a `*FileStorageRepository` / `*FileRepository` writing JSON to disk via `ObjectMapper`. There is no JPA/DB layer.
- `reports/utils/xml/` contains hand-written JAXB tag classes and factories for building Аршин/ФСА XML; `reports/utils/excel/` parses uploaded Excel files (Poiji).
- Config (paths, organization, intervals) lives in `src/main/resources/application.yaml`. It hard-codes absolute paths under `/home/aleksei/TMP/...` and the prod profile — do not assume these are portable; tests inject their own temp paths via `PathsConfig`.

## Testing conventions

- Pure unit tests, no Spring context (`@TempDir` + manual class instantiation); see `EmployeeStorageTest`. Don't spin up `@SpringBootTest` — the full context depends on the external Arshin starter.

## Misc

- Logs are written to `src/logs/` (not committed in practice; `.gitignore` does not exclude them — avoid committing the `*.log*` files).
- CI (`.github/workflows/ci.yml`) triggers on PRs to `main` and `develop` with SonarQube quality-gate checks.
