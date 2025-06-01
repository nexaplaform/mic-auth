package com.nexaplatform.api.controllers.services.dto.out;

import com.nexaplatform.domain.models.UserStatus;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoOut {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "John")
    private String firstName;

    @Schema(example = "Doe")
    private String lastName;

    @Schema(example = "John Doe")
    private String fullName;

    @Schema(example = "5 5555 5555")
    private String phoneNumber;

    @Schema(example = "John Doe")
    private String email;

    @Schema(example = "ACTIVE")
    private UserStatus status;

    @ArraySchema(schema = @Schema(implementation = RoleDtoOut.class))
    private List<RoleDtoOut> roles;
}
