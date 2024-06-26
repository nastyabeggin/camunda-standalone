package com.itmo.simaland.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name = "privilege")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Privilege implements GrantedAuthority {
    @Id
    private Long id;

    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
