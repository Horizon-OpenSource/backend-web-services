package com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest;

import com.horizon.ecotrack.platform.carbonfootprint.application.CalculateFootprintCommand;
import com.horizon.ecotrack.platform.carbonfootprint.application.FootprintCalculationService;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.resource.CalculateFootprintRequest;
import com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.resource.FootprintCalculationResponse;
import com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.resource.FootprintResultsResponse;
import com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.resource.HistoryEntryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/footprint-calculations")
@Tag(name = "Footprint calculations", description = "Carbon footprint calculation, results by scope and history")
public class FootprintCalculationController {
    private final FootprintCalculationService service;
    public FootprintCalculationController(FootprintCalculationService service) { this.service = service; }

    @PostMapping
    @Operation(summary = "Calculate the footprint of a period",
            description = "Replaces the previous calculation of the same organization and period.")
    public ResponseEntity<FootprintCalculationResponse> calculate(@Valid @RequestBody CalculateFootprintRequest request) {
        var items = request.items().stream()
                .map(item -> new CalculateFootprintCommand.Item(item.activityDataId(), item.sourceName(),
                        FootprintScope.valueOf(item.scope()), item.quantity(), item.unit(), item.emissionFactorId()))
                .toList();
        var calculation = service.calculate(new CalculateFootprintCommand(request.organizationId(), request.period(), items));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(calculation.getId()).toUri();
        return ResponseEntity.created(location).body(FootprintCalculationResponse.from(calculation));
    }

    @GetMapping
    @Operation(summary = "List the calculations of an organization, newest first, optionally for one period")
    public List<FootprintCalculationResponse> list(@RequestParam("organizationId") String organizationId,
                                                   @RequestParam(value = "period", required = false) String period) {
        return service.list(organizationId, period).stream().map(FootprintCalculationResponse::from).toList();
    }

    @GetMapping("/results")
    @Operation(summary = "Get the total and the results by scope of a period")
    public FootprintResultsResponse results(@RequestParam("organizationId") String organizationId,
                                            @RequestParam("period") String period) {
        return FootprintResultsResponse.from(service.resultsOf(organizationId, period));
    }

    @GetMapping("/history")
    @Operation(summary = "Get the history of calculations of an organization")
    public List<HistoryEntryResponse> history(@RequestParam("organizationId") String organizationId) {
        return service.history(organizationId).stream().map(HistoryEntryResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a calculation with its items")
    public FootprintCalculationResponse get(@PathVariable("id") String id) {
        return FootprintCalculationResponse.from(service.get(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a calculation")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}