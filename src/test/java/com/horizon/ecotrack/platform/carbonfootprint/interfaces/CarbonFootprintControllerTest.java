package com.horizon.ecotrack.platform.carbonfootprint.interfaces;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.horizon.ecotrack.platform.carbonfootprint.application.EmissionFactorService;
import com.horizon.ecotrack.platform.carbonfootprint.application.FootprintCalculationService;
import com.horizon.ecotrack.platform.carbonfootprint.application.InMemoryRepositories;
import com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.CarbonFootprintExceptionHandler;
import com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.EmissionFactorController;
import com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.FootprintCalculationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class CarbonFootprintControllerTest {
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        var factors = new InMemoryRepositories.Factors();
        var factorService = new EmissionFactorService(factors);
        var calculationService = new FootprintCalculationService(new InMemoryRepositories.Calculations(), factors);
        mvc = MockMvcBuilders.standaloneSetup(new EmissionFactorController(factorService),
                        new FootprintCalculationController(calculationService))
                .setControllerAdvice(new CarbonFootprintExceptionHandler()).build();
    }

    private String createGasFactor() throws Exception {
        String body = mvc.perform(post("/api/v1/emission-factors").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Natural gas\",\"scope\":\"SCOPE_1\",\"unit\":\"m3\",\"value\":2.0,\"source\":\"Demo\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return body.replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    @Test
    void factorCrudAndValidation() throws Exception {
        String id = createGasFactor();
        mvc.perform(get("/api/v1/emission-factors/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$.unit").value("m3"));
        mvc.perform(get("/api/v1/emission-factors").param("scope", "SCOPE_2")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
        mvc.perform(post("/api/v1/emission-factors").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"scope\":\"SCOPE_9\",\"unit\":\"m3\",\"value\":-1,\"source\":\"x\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(delete("/api/v1/emission-factors/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/v1/emission-factors/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void calculateThenReadResultsAndHistory() throws Exception {
        String factorId = createGasFactor();
        String request = "{\"organizationId\":\"1\",\"period\":\"2026-10\",\"items\":[{\"activityDataId\":\"a1\","
                + "\"sourceName\":\"Boiler\",\"scope\":\"SCOPE_1\",\"quantity\":100,\"unit\":\"m3\",\"emissionFactorId\":\"" + factorId + "\"}]}";
        mvc.perform(post("/api/v1/footprint-calculations").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.total").value(200.0));
        mvc.perform(get("/api/v1/footprint-calculations/results").param("organizationId", "1").param("period", "2026-10"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.scopes.length()").value(3))
                .andExpect(jsonPath("$.scopes[0].records").value(1));
        mvc.perform(get("/api/v1/footprint-calculations/history").param("organizationId", "1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(get("/api/v1/footprint-calculations/results").param("organizationId", "1").param("period", "2026-01"))
                .andExpect(status().isNotFound());
        mvc.perform(get("/api/v1/footprint-calculations/results").param("organizationId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidCalculationRequestsAreRejected() throws Exception {
        mvc.perform(post("/api/v1/footprint-calculations").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"organizationId\":\"1\",\"period\":\"2026-99\",\"items\":[]}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/v1/footprint-calculations").contentType(MediaType.APPLICATION_JSON).content("not json"))
                .andExpect(status().isBadRequest());
    }
}
