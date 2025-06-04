package com.nexaplatform.api.controllers.services.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationMethodDtoOut {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "client_secret_basic")
    private String method;
}
