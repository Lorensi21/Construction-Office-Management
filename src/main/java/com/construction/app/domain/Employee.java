package com.construction.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Entity
@Getter @Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<EmployeeType> employeeTypes;

    @ManyToOne(optional = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "bureau_id")
    private Bureau bureau;

    private String managementTier;
    private String specialty;

    @Override
    public String toString() {
        return name + " " + surname;
    }
}
