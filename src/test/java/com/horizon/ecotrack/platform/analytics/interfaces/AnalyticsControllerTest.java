package com.horizon.ecotrack.platform.analytics.interfaces;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.horizon.ecotrack.platform.analytics.application.AnalyticsQueryService;
import com.horizon.ecotrack.platform.analytics.application.InMemoryAnalyticsRepositories;
import com.horizon.ecotrack.platform.analytics.application.ReportCommandService;
import com.horizon.ecotrack.platform.analytics.interfaces.rest.AnalyticsController;
import com.horizon.ecotrack.platform.analytics.interfaces.rest.AnalyticsExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AnalyticsControllerTest {
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        var indicators = InMemoryAnalyticsRepositories.Indicators.withDemoData();
        var reports = new InMemoryAnalyticsRepositories.Reports();
        var controller = new AnalyticsController(new AnalyticsQueryService(indicators, reports),
                new ReportCommandService(indicators, reports, event -> { }));
        mvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new AnalyticsExceptionHandler()).build();
    }

    private String generatePeriodReport(String period) throws Exception {
        return mvc.perform(post("/api/v1/analytics/reports/period").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"organizationId\":1,\"period\":\"" + period + "\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
    }

    @Test
    void dashboardListsTheIndicators() throws Exception {
        mvc.perform(get("/api/v1/analytics/dashboard").param("organizationId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(8))
                .andExpect(jsonPath("$[0].type").value("SCOPE_1"))
                .andExpect(jsonPath("$[0].period").value("2026-01"))
                .andExpect(jsonPath("$[0].value").value(42.5));
    }

    @Test
    void summaryListsEveryPeriodAndMarksMissingScopes() throws Exception {
        mvc.perform(get("/api/v1/analytics/summary").param("organizationId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].hasData").value(true))
                .andExpect(jsonPath("$[0].total").value(72.5))
                .andExpect(jsonPath("$[0].scopes[2].scope").value("SCOPE_3"))
                .andExpect(jsonPath("$[0].scopes[2].tonnes").value(nullValue()));
    }

    @Test
    void summaryOfAPeriodWithoutResultsReportsNoData() throws Exception {
        mvc.perform(get("/api/v1/analytics/summary").param("organizationId", "1").param("period", "2026-04"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].hasData").value(false))
                .andExpect(jsonPath("$[0].total").value(nullValue()));
    }

    @Test
    void summaryRejectsAMissingOrganizationAndAnInvalidPeriod() throws Exception {
        mvc.perform(get("/api/v1/analytics/summary")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/v1/analytics/summary").param("organizationId", "1").param("period", "2026-13"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/v1/analytics/summary").param("organizationId", "abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void comparisonReturnsTheVariation() throws Exception {
        mvc.perform(get("/api/v1/analytics/comparison").param("organizationId", "1")
                        .param("from", "2026-02").param("to", "2026-03"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from.total").value(83.5))
                .andExpect(jsonPath("$.to.total").value(89.6))
                .andExpect(jsonPath("$.variationTonnes").value(6.1))
                .andExpect(jsonPath("$.variationPercent").value(7.31));
    }

    @Test
    void comparisonOfAPeriodWithoutResultsIsUnprocessable() throws Exception {
        mvc.perform(get("/api/v1/analytics/comparison").param("organizationId", "1")
                        .param("from", "2026-02").param("to", "2026-04"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value(containsString("not enough data")));
    }

    @Test
    void generatesAReportForAPeriod() throws Exception {
        mvc.perform(post("/api/v1/analytics/reports/period").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"organizationId\":1,\"period\":\"2026-02\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/analytics/reports/1"))
                .andExpect(jsonPath("$.type").value("PERIOD"))
                .andExpect(jsonPath("$.period").value("2026-02"))
                .andExpect(jsonPath("$.totalTonnes").value(83.5))
                .andExpect(jsonPath("$.status").value("GENERATED"));
    }

    @Test
    void reportForAPeriodWithoutResultsIsUnprocessable() throws Exception {
        mvc.perform(post("/api/v1/analytics/reports/period").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"organizationId\":1,\"period\":\"2026-04\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void reportRequestsAreValidated() throws Exception {
        mvc.perform(post("/api/v1/analytics/reports/period").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"organizationId\":1,\"period\":\"2026-13\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/v1/analytics/reports/period").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"period\":\"2026-02\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/v1/analytics/reports/consolidated").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"organizationId\":-1}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/v1/analytics/reports/consolidated").contentType(MediaType.APPLICATION_JSON)
                        .content("not json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generatesAConsolidatedReport() throws Exception {
        mvc.perform(post("/api/v1/analytics/reports/consolidated").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"organizationId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("CONSOLIDATED"))
                .andExpect(jsonPath("$.period").value(nullValue()))
                .andExpect(jsonPath("$.totalTonnes").value(245.6));
        mvc.perform(post("/api/v1/analytics/reports/consolidated").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"organizationId\":99}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void historyCanBeFilteredByPeriod() throws Exception {
        generatePeriodReport("2026-01");
        generatePeriodReport("2026-02");

        mvc.perform(get("/api/v1/analytics/reports").param("organizationId", "1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
        mvc.perform(get("/api/v1/analytics/reports").param("organizationId", "1").param("period", "2026-02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].period").value("2026-02"));
        mvc.perform(get("/api/v1/analytics/reports").param("organizationId", "1").param("period", "2026-04"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void downloadsAReportAsCsvAndMarksItAsDownloaded() throws Exception {
        generatePeriodReport("2026-02");

        mvc.perform(get("/api/v1/analytics/reports/1/download"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("sustainability-report-1.csv")))
                .andExpect(content().contentTypeCompatibleWith("text/csv"))
                .andExpect(content().string(containsString("2026-02,SCOPE_1,40.00")));
        mvc.perform(get("/api/v1/analytics/reports").param("organizationId", "1"))
                .andExpect(jsonPath("$[0].status").value("DOWNLOADED"));
    }

    @Test
    void downloadOfAnUnknownReportIsNotFound() throws Exception {
        mvc.perform(get("/api/v1/analytics/reports/999/download")).andExpect(status().isNotFound());
    }
}
