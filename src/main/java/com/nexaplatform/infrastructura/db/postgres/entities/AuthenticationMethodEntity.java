package com.nexaplatform.infrastructura.db.postgres.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@With
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authentication_methods")
@EqualsAndHashCode(callSuper = true)
public class AuthenticationMethodEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "method")
    private String method;
}
