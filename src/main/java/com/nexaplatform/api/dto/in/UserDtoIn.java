package com.nexaplatform.api.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoIn {

    @Schema(example = "John")
    private String fistName;

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
