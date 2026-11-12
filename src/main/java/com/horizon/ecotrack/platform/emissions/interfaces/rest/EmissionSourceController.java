package com.horizon.ecotrack.platform.emissions.interfaces.rest;

import com.horizon.ecotrack.platform.emissions.application.EmissionSourceService;
import com.horizon.ecotrack.platform.emissions.domain.model.EmissionScope;
import com.horizon.ecotrack.platform.emissions.interfaces.rest.resource.EmissionSourceRequest;
import com.horizon.ecotrack.platform.emissions.interfaces.rest.resource.EmissionSourceResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/emission-sources")
public class EmissionSourceController {
    private final EmissionSourceService service;
    public EmissionSourceController(EmissionSourceService service) { this.service = service; }

    @GetMapping
    public List<EmissionSourceResponse> list(@RequestParam(value = "scope", required = false) EmissionScope scope) {
        return service.list(scope).stream().map(EmissionSourceResponse::from).toList();
    }

    @GetMapping("/{id}")
    public EmissionSourceResponse get(@PathVariable("id") String id) {
        return EmissionSourceResponse.from(service.get(id));
    }

    @PostMapping
    public ResponseEntity<EmissionSourceResponse> create(@Valid @RequestBody EmissionSourceRequest request) {
        var source = service.create(request.facilityId(), request.facilityName(), request.name(), request.type(), EmissionScope.valueOf(request.scope()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(source.getId()).toUri();
        return ResponseEntity.created(location).body(EmissionSourceResponse.from(source));
    }

    @PutMapping("/{id}")
    public EmissionSourceResponse update(@PathVariable("id") String id, @Valid @RequestBody EmissionSourceRequest request) {
        return EmissionSourceResponse.from(service.update(id, request.facilityId(), request.facilityName(), request.name(), request.type(), EmissionScope.valueOf(request.scope())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
