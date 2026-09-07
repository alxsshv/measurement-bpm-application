package com.github.alxsshv.measurementbpmapplication.reportscheduler.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.alxsshv.measurementbpmapplication.common.config.PathsConfig;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.entity.FsaReportTask;
import com.github.alxsshv.measurementbpmapplication.reportscheduler.exception.FsaReportTaskStorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("java:S5778")
class FsaReportTaskFileRepositoryTest {

    private static final String STORAGE_FILENAME = "fsa_report_tasks_table.json";

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @TempDir
    private static Path tempDir;

    private PathsConfig paths;

    private FsaReportTaskFileRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(tempDir.resolve(STORAGE_FILENAME));
        paths = new PathsConfig("", "", "", "", tempDir.toString(), "");
        repository = new FsaReportTaskFileRepository(paths, objectMapper);
    }

    @Nested
    class TestFindAllMethod {

        @Test
        @DisplayName("Создаёт файл хранения и возвращает пустой список, если файл отсутствует")
        void findAllReturnsEmptyListForMissingStorage() {
            assertTrue(repository.findAll().isEmpty());
            assertTrue(Files.exists(tempDir.resolve(STORAGE_FILENAME)));
        }

        @Test
        @DisplayName("Возвращает задачи из файла хранения")
        void findAllReturnsPersistedTasks() {
            FsaReportTask task = createTask("1");
            repository.save(task);

            assertEquals(1, repository.findAll().size());
            assertEquals("1", repository.findAll().get(0).getId());
        }

        @Test
        @DisplayName("Бросает исключение при повреждённом файле хранения")
        void findAllThrowsStorageExceptionOnCorruptedJson() throws IOException {
            Files.writeString(tempDir.resolve(STORAGE_FILENAME), "{ corrupted json");

            assertThrows(FsaReportTaskStorageException.class, repository::findAll);
        }
    }

    @Nested
    class TestFindByIdMethod {

        @Test
        @DisplayName("Возвращает задачу по идентификатору")
        void findByIdReturnsTask() {
            FsaReportTask task = createTask("1");
            repository.save(task);

            Optional<FsaReportTask> result = repository.findById("1");

            assertTrue(result.isPresent());
            assertEquals(task.getId(), result.get().getId());
            assertEquals(task.getTitle(), result.get().getTitle());
        }

        @Test
        @DisplayName("Возвращает пустой Optional для отсутствующей задачи")
        void findByIdReturnsEmptyOptionalForMissingTask() {
            assertTrue(repository.findById("missing").isEmpty());
        }
    }

    @Nested
    class TestSaveMethod {

        @Test
        @DisplayName("Сохраняет задачу в файл хранения")
        void savePersistsTask() {
            FsaReportTask task = createTask("1");

            FsaReportTask result = repository.save(task);
            assertEquals(1, repository.findAll().size());
            assertEquals(task.getId(), result.getId());
        }

        @Test
        @DisplayName("Бросает исключение, когда не удаётся записать файл хранения")
        void saveThrowsStorageExceptionWhenCannotWrite() throws IOException {
            Path blockedFile = Files.createFile(tempDir.resolve("blocked"));
            PathsConfig blockedPaths = new PathsConfig("", "", "", "", blockedFile.toString(), "");
            FsaReportTaskFileRepository blockedRepository = new FsaReportTaskFileRepository(blockedPaths, objectMapper);

            assertThrows(FsaReportTaskStorageException.class, () -> blockedRepository.save(createTask("1")));
        }
    }

    @Nested
    class TestDeleteMethod {

        @Test
        @DisplayName("Удаляет задачу из файла хранения")
        void deleteRemovesTask() {
            repository.save(createTask("1"));
            repository.save(createTask("2"));

            repository.delete("1");

            assertEquals(1, repository.findAll().size());
            assertEquals("2", repository.findAll().get(0).getId());
        }
    }

    @Nested
    class TestUpdateMethod {

        @Test
        @DisplayName("Обновляет существующую задачу")
        void updatePersistsChanges() {
            FsaReportTask task = createTask("1");
            task.setCreationTimestamp(LocalDateTime.of(2024, Month.DECEMBER, 25, 14, 30));
            repository.save(task);
            task.setStatus(FsaReportTask.Status.PROCESSED);

            repository.update(task);

            assertTrue(repository.findById("1").isPresent());
            assertEquals(FsaReportTask.Status.PROCESSED, repository.findById("1").get().getStatus());
        }

        @Test
        @DisplayName("Бросает исключение при обновлении отсутствующей задачи")
        void updateThrowsStorageExceptionForMissingTask() {
            assertThrows(FsaReportTaskStorageException.class, () -> repository.update(createTask("1")));
        }
    }

    @Nested
    class TestDeleteAllMethod {

        @Test
        @DisplayName("Очищает файл хранения")
        void deleteAllEmptiesStorage() {
            repository.save(createTask("1"));

            repository.deleteAll();

            assertTrue(repository.findAll().isEmpty());
        }
    }

    private FsaReportTask createTask(String id) {
        return FsaReportTask.builder()
                .id(id)
                .title("Отчет по файлу book.xlsx")
                .contentFilename("content.tmp")
                .status(FsaReportTask.Status.CREATED)
                .creationTimestamp(LocalDateTime.of(2024, Month.DECEMBER, 25, 14, 30))
                .build();
    }
}