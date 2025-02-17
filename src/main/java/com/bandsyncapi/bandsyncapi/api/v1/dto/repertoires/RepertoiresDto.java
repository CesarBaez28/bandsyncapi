package com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires;

import java.util.UUID;

/**
 * This Dto represents the data to be sent to the client
 */
public record RepertoiresDto(UUID id, String name, String description, String link, Boolean status) {
}
