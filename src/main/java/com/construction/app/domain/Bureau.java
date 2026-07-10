package com.construction.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
public class Bureau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String bureauName;

    private String location;

    @OneToMany(mappedBy = "bureau")
    private Set<Employee> employees = new HashSet<>();

    @PreRemove
    private void preRemove() {
        for (Employee e : employees) {
            e.setBureau(null); // unassign emp
        }
    }

}
