package com.nexaplatform.api.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoOut {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "John")
    private String fistName;

    @Schema(example = "Doe")
    private String lastName;

    @Schema(example = "John Doe")
    private String fullName;

    @Schema(example = "5 5555 5555")
    private String phoneNumber;

    @Schema(example = "John Doe")
    private String email;
}
