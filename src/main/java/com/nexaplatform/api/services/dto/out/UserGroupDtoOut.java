package com.nexaplatform.api.services.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupDtoOut {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Admin")
    private String name;

    @Schema(example = "admin")
    private String uniqueName;

    @Schema(example = "This is the admin group.")
    private String description;

    @Schema(example = "true")
    private Boolean active;
}
