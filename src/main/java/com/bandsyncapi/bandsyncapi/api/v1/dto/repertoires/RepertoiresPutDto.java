package com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires;

/**
 * This Dto represents the update request for RepertoiresModel
 */
public record RepertoiresPutDto(String name, String description, String link, Boolean status) {
}
