package com.nexaplatform.api.services.dto.in;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDtoIn {

    @NotBlank
    @Schema(example = "Admin")
    private String name;

    @NotBlank
    @Schema(example = "admin")
    private String uniqueName;

    @Schema(example = "true")
    private Boolean enabled;

    @Schema(example = "This is the admin group.")
    private String description;

    @ArraySchema(schema = @Schema(implementation = Long.class))
    private List<Long> rolesIds;

    @ArraySchema(schema = @Schema(implementation = Long.class))
    private List<Long> usersIds;
}
