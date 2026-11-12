# Carbon Footprint Calculation (backend)

Core bounded context of EcoTrack. Calculates the carbon footprint of an organization for a monthly period
(`YYYY-MM`) from activity records and emission factors, and exposes the results by Scope 1, 2 and 3 plus the history.

## Layers (`com.horizon.ecotrack.platform.carbonfootprint`)
| Layer | Content |
|---|---|
| `domain.model` | `EmissionFactor`, `FootprintCalculation` (aggregate) with `CalculationItem`; value objects `ReportingPeriod`, `ActivityQuantity`, `CarbonEmissions`, `FootprintScope` |
| `domain.repository` | Ports `EmissionFactorRepository`, `FootprintCalculationRepository` |
| `application` | `EmissionFactorService`, `FootprintCalculationService`, `CalculateFootprintCommand` |
| `infrastructure` | Spring Data JPA adapters and an optional demo-data loader |
| `interfaces.rest` | Controllers, request/response resources, exception handler |

## Business rules
- `emissions = quantity x factor value` (kgCO2e, 6 decimals, HALF_UP).
- The factor must belong to the same scope as the activity and use the same unit.
- An activity record cannot be counted twice in one calculation.
- Calculating an organization + period again replaces the previous result; other periods are kept as history.
- Each item stores a snapshot of the factor, so editing a factor later does not change past results.

## Endpoints (Swagger UI: `/swagger-ui/index.html`, OpenAPI: `/v3/api-docs`)
| Method | Path | Description |
|---|---|---|
| GET/POST | `/api/v1/emission-factors` | List (optional `?scope=SCOPE_1`) / create |
| GET/PUT/DELETE | `/api/v1/emission-factors/{id}` | Read / update / delete |
| POST | `/api/v1/footprint-calculations` | Calculate a period |
| GET | `/api/v1/footprint-calculations?organizationId=&period=` | List calculations (newest first) |
| GET | `/api/v1/footprint-calculations/results?organizationId=&period=` | Total and results by scope |
| GET | `/api/v1/footprint-calculations/history?organizationId=` | History of periods |
| GET/DELETE | `/api/v1/footprint-calculations/{id}` | Read / delete |

Errors use `{ "error": "...", "message": "..." }` with 400 (validation), 404 (not found).

Example calculation request:
```json
{ "organizationId": "1", "period": "2026-10",
  "items": [ { "activityDataId": "a1", "sourceName": "Boiler", "scope": "SCOPE_1",
               "quantity": 100, "unit": "m3", "emissionFactorId": "<factor id>" } ] }
```

## Run
The datasource is MySQL. Provide the usual Spring properties (environment variables work), for example:
```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ecotrack
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=...
SPRING_JPA_HIBERNATE_DDL_AUTO=update
ECOTRACK_CARBON_FOOTPRINT_DEMO_DATA=true   # loads 5 illustrative emission factors if the table is empty
```
Then `./mvnw spring-boot:run` (Windows: `.\mvnw.cmd spring-boot:run`).

## Test data
With `ECOTRACK_CARBON_FOOTPRINT_DEMO_DATA=true` the factors natural gas (2.0 kgCO2e/m3), diesel (2.68/L),
grid electricity (0.25/kWh), air travel (0.15/km) and road freight (0.10/t-km) are created. The values are
illustrative for development only, not official factors.

## Tests
`./mvnw test` runs domain unit tests, service tests and REST tests (standalone MockMvc with in-memory repositories).
