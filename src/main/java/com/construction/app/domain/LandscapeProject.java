package com.construction.app.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter @Setter
public class LandscapeProject extends Project {
    private Double landAreaSquareMeters;

    public LandscapeProject() {}

    public Double getLandAreaSquareMeters() { return landAreaSquareMeters; }
    public void setLandAreaSquareMeters(Double landAreaSquareMeters) { this.landAreaSquareMeters = landAreaSquareMeters; }
}
