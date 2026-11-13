package com.horizon.ecotrack.platform.organization.interfaces.rest;

import com.horizon.ecotrack.platform.organization.application.commandservices.OrganizationCommandService;
import com.horizon.ecotrack.platform.organization.application.queryservices.OrganizationQueryService;
import com.horizon.ecotrack.platform.organization.domain.model.queries.GetAllOrganizationsQuery;
import com.horizon.ecotrack.platform.organization.domain.model.queries.GetOrganizationByIdQuery;
import com.horizon.ecotrack.platform.organization.interfaces.rest.resources.*;
import com.horizon.ecotrack.platform.organization.interfaces.rest.transform.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizations")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Organization Management")
public class OrganizationsController {

    private final OrganizationCommandService commandService;
    private final OrganizationQueryService queryService;

    public OrganizationsController(
            OrganizationCommandService commandService,
            OrganizationQueryService queryService
    ) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    @Operation(summary = "Register organization")
    public ResponseEntity<OrganizationResource> create(
            @Valid @RequestBody CreateOrganizationResource request
    ) {
        var command = CreateOrganizationCommandFromResourceAssembler
                .toCommandFromResource(request);

        var saved = commandService.handle(command);

        var resource = OrganizationResourceFromEntityAssembler
                .toResourceFromEntity(saved);

        return ResponseEntity
                .created(URI.create("/api/v1/organizations/" + saved.getId()))
                .body(resource);
    }

    @GetMapping
    @Operation(summary = "List organizations")
    public List<OrganizationResource> list() {
        return queryService
                .handle(new GetAllOrganizationsQuery())
                .stream()
                .map(OrganizationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }

    @GetMapping("/{organizationId}")
    @Operation(summary = "Get organization by ID")
    public ResponseEntity<OrganizationResource> getById(
            @PathVariable Long organizationId
    ) {
        return queryService
                .handle(new GetOrganizationByIdQuery(organizationId))
                .map(OrganizationResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{organizationId}")
    @Operation(summary = "Update organization profile")
    public OrganizationResource update(
            @PathVariable Long organizationId,
            @Valid @RequestBody UpdateOrganizationResource request
    ) {
        var current = queryService
                .handle(new GetOrganizationByIdQuery(organizationId))
                .orElseThrow(() -> new NoSuchElementException(
                        "Organization not found: " + organizationId
                ));

        var command = UpdateOrganizationCommandFromResourceAssembler
                .toCommandFromResource(organizationId, request, current);

        return OrganizationResourceFromEntityAssembler
                .toResourceFromEntity(commandService.handle(command));
    }
}
