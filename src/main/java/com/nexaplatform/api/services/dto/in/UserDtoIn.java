package com.nexaplatform.api.services.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

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

    @Schema(example = "5-5555-5555")
    private String phoneNumber;

    @Schema(example = "JohnDoe@example.com")
    private String email;

    @Schema(example = "123456789")
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String password;

    @Schema(description = "Lista de roles del usuario", example = "[\"1\", \"2\"]")
    private List<Long> roles;
}
