package com.nexaplatform.api.controllers.services.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Data
@With
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RoleDtoOut {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "ROLE_USER")
    private String name;
    @Schema(example = "Role admin")
    private String description;
    @Schema(example = "true")
    private Boolean active;
}
