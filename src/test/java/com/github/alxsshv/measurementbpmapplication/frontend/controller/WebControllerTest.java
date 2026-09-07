package com.github.alxsshv.measurementbpmapplication.frontend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


class WebControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new WebController()).build();
    }

    @Nested
    class TestGetFirstViewMethod {

        @Test
        @DisplayName("Возвращает представление index")
        void testGetFirstView() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"));
        }
    }

    @Nested
    class TestGetEmployeeFormViewMethod {

        @Test
        @DisplayName("Возвращает представление employee/form")
        void testGetEmployeeFormView() throws Exception {
            mockMvc.perform(get("/employees/form"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("employee/form"));
        }
    }

    @Nested
    class TestGetEmployeeListViewMethod {

        @Test
        @DisplayName("Возвращает представление employee/list")
        void testGetEmployeeListView() throws Exception {
            mockMvc.perform(get("/employees"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("employee/list"));
        }
    }

    @Nested
    class TestGetEmployeeViewMethod {

        @Test
        @DisplayName("Возвращает представление employee/card с идентификатором поверителя")
        void testGetEmployeeView() throws Exception {
            mockMvc.perform(get("/employees/1").param("id", "42"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("employee/card"))
                    .andExpect(model().attribute("id", "42"));
        }
    }

    @Nested
    class TestGetEditEmployeeFormMethod {

        @Test
        @DisplayName("Возвращает представление employee/edit с идентификатором поверителя")
        void testGetEditEmployeeForm() throws Exception {
            mockMvc.perform(get("/employees/form/1").param("id", "42"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("employee/edit"))
                    .andExpect(model().attribute("id", "42"));
        }
    }

    @Nested
    class TestGetFsaReportTasksListViewMethod {

        @Test
        @DisplayName("Возвращает представление tasks/fsa/list")
        void testGetFsaReportTasksListView() throws Exception {
            mockMvc.perform(get("/tasks/fsa"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("tasks/fsa/list"));
        }
    }

    @Nested
    class TestGetFsaReportTasksFormViewMethod {

        @Test
        @DisplayName("Возвращает представление tasks/fsa/form")
        void testGetFsaReportTasksFormView() throws Exception {
            mockMvc.perform(get("/tasks/fsa/form"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("tasks/fsa/form"));
        }
    }

    @Nested
    class TestGetFsaConvertFormMethod {

        @Test
        @DisplayName("Возвращает представление fsa/form")
        void testGetFsaConvertForm() throws Exception {
            mockMvc.perform(get("/fsa-converters"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("fsa/form"));
        }
    }

    @Nested
    class TestGetArshinConvertFormMethod {

        @Test
        @DisplayName("Возвращает представление arshin/form")
        void testGetArshinConvertForm() throws Exception {
            mockMvc.perform(get("/arshin-converters"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("arshin/form"));
        }
    }
}