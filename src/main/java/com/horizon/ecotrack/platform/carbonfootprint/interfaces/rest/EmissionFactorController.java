package com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest;

import com.horizon.ecotrack.platform.carbonfootprint.application.EmissionFactorService;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.resource.EmissionFactorRequest;
import com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.resource.EmissionFactorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/emission-factors")
@Tag(name = "Emission factors", description = "Factors that convert activity data into kgCO2e")
public class EmissionFactorController {
    private final EmissionFactorService service;
    public EmissionFactorController(EmissionFactorService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "List emission factors, optionally filtered by scope")
    public List<EmissionFactorResponse> list(@RequestParam(value = "scope", required = false) FootprintScope scope) {
        return service.list(scope).stream().map(EmissionFactorResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an emission factor")
    public EmissionFactorResponse get(@PathVariable("id") String id) {
        return EmissionFactorResponse.from(service.get(id));
    }

    @PostMapping
    @Operation(summary = "Create an emission factor")
    public ResponseEntity<EmissionFactorResponse> create(@Valid @RequestBody EmissionFactorRequest request) {
        var factor = service.create(request.name(), FootprintScope.valueOf(request.scope()), request.unit(),
                request.value(), request.source());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(factor.getId()).toUri();
        return ResponseEntity.created(location).body(EmissionFactorResponse.from(factor));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace the editable fields of an emission factor")
    public EmissionFactorResponse update(@PathVariable("id") String id, @Valid @RequestBody EmissionFactorRequest request) {
        return EmissionFactorResponse.from(service.update(id, request.name(), FootprintScope.valueOf(request.scope()),
                request.unit(), request.value(), request.source()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an emission factor")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}