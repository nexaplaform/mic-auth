package com.nexaplatform.api.services.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationMethodDtoIn {

    @Schema(example = "client_secret_basic")
    private String method;
}
