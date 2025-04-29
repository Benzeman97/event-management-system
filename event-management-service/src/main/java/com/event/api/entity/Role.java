package com.event.api.entity;

import com.event.api.enums.ERole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name="role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private int id;

    @Enumerated(EnumType.STRING)
    private ERole name;
}
