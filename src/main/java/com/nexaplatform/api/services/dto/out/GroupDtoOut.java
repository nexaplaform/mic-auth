package com.nexaplatform.api.services.dto.out;

import com.nexaplatform.domain.models.Role;
import com.nexaplatform.domain.models.User;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDtoOut {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Admin")
    private String name;

    @Schema(example = "admin")
    private String uniqueName;

    @Schema(example = "This is the admin group.")
    private String description;

    @ArraySchema(schema = @Schema(implementation = Role.class))
    private List<RoleDtoOut> roles;

    @ArraySchema(schema = @Schema(implementation = User.class))
    private List<UserDtoOut> users;

    @Schema(example = "user")
    private String createdBy;

    @Schema(example = "2025-07-09 15:59:22.034 +0200")
    private Instant createdDate;

    @Schema(example = "admin")
    private String lastModifiedBy;

    @Schema(example = "2025-07-09 23:59:22.034 +0200")
    private Instant lastModifiedDate;
}
