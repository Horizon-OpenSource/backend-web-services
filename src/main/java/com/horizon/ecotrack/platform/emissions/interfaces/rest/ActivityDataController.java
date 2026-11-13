package com.horizon.ecotrack.platform.emissions.interfaces.rest;

import com.horizon.ecotrack.platform.emissions.application.ActivityDataService;
import com.horizon.ecotrack.platform.emissions.domain.model.ActivityOrigin;
import com.horizon.ecotrack.platform.emissions.interfaces.rest.resource.ActivityDataRequest;
import com.horizon.ecotrack.platform.emissions.interfaces.rest.resource.ActivityDataResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/activity-data")
public class ActivityDataController {
    private final ActivityDataService service;
    public ActivityDataController(ActivityDataService service) { this.service = service; }

    @GetMapping
    public List<ActivityDataResponse> list(@RequestParam(value = "emissionSourceId", required = false) String sourceId) {
        return service.list(sourceId).stream().map(ActivityDataResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ActivityDataResponse get(@PathVariable("id") String id) {
        return ActivityDataResponse.from(service.get(id));
    }

    @PostMapping
    public ResponseEntity<ActivityDataResponse> create(@Valid @RequestBody ActivityDataRequest request) {
        var activity = service.create(request.emissionSourceId(), request.period(), request.quantity(), request.unit(), ActivityOrigin.valueOf(request.origin()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(activity.getId()).toUri();
        return ResponseEntity.created(location).body(ActivityDataResponse.from(activity));
    }

    @PutMapping("/{id}")
    public ActivityDataResponse update(@PathVariable("id") String id, @Valid @RequestBody ActivityDataRequest request) {
        return ActivityDataResponse.from(service.update(id, request.emissionSourceId(), request.period(), request.quantity(), request.unit(), ActivityOrigin.valueOf(request.origin())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
