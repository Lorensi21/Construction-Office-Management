package com.construction.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @Min(0)
    private Double budget;

    @Enumerated(EnumType.STRING)
    private ProjectState state = ProjectState.DRAFTING;

    @Enumerated(EnumType.STRING)
    private ProjectCategory projectCategory;

    private String nameOfCompany;
    private Integer residentCapacity;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectRole> projectRoles = new ArrayList<>();

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST  )
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Override
    public String toString() {
        if (this instanceof LandscapeProject) {
            return "[LANDSCAPE] " + name;
        } else if (this instanceof InteriorProject) {
            return "[INTERIOR] " + name;
        }
        return "[" + projectCategory + "] " + name;
    }
}
