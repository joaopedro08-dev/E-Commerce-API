package com.crud.ecommerce.infrastructure.entity.client;

import com.crud.ecommerce.infrastructure.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_clients_profile")
@Getter
@Setter
public class ClientProfile extends BaseEntity {

    @Column(nullable = false, name = "cpf", length = 14, unique = true)
    private String cpf;

    @Column(nullable = false, name = "rg", length = 20)
    private String rg;

    @Embedded
    private Address address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "client_id")
    private Client client;
}