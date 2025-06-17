package com.nexaplatform.api.controllers.services.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@With
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RoleDtoIn {

    @Schema(example = "ROLE_USER")
    private String name;
    @Schema(example = "User admin")
    private String description;
    @Schema(example = "true")
    private Boolean active;

}
