package com.construction.app.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class InteriorProject extends Project {
    private String interiorDesignStyle;

    public InteriorProject() {}

    public String getInteriorDesignStyle() { return interiorDesignStyle; }
    public void setInteriorDesignStyle(String interiorDesignStyle) { this.interiorDesignStyle = interiorDesignStyle; }
}
