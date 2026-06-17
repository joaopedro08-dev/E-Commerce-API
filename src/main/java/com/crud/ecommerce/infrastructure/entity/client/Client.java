package com.crud.ecommerce.infrastructure.entity.client;

import com.crud.ecommerce.infrastructure.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_clients")
@Getter
@Setter
public class Client extends BaseEntity {

    @Column(nullable = false, name = "full_name", length = 100)
    private String fullName;

    @Column(nullable = false, name = "email", length = 150)
    private String email;

    @Column(nullable = false, name = "phone", length = 20)
    private String phone;

    @Column(nullable = false, name = "status_client")
    private Boolean statusClient = true;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private ClientProfile profile;
}
