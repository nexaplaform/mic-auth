package com.nexaplatform.api.services.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoIn {

    @NotNull
    @Schema(example = "John")
    private String firstName;

    @NotNull
    @Schema(example = "Doe")
    private String lastName;

    @Schema(example = "5 5555 5555")
    private String phoneNumber;

    @Schema(example = "John Doe")
    private String email;

    @Schema(example = "123456789")
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String password;
}
