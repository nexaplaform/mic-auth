package com.nexaplatform.api.controllers.services.dto.out;

import lombok.*;


@Data
@With
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RoleDtoOut {

    private Long id;
    private String name;
    private String description;
    private Boolean active;
}
