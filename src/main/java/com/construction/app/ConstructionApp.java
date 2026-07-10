package com.construction.app;

import com.construction.app.domain.*;
import com.construction.app.repository.*;
import com.construction.app.gui.ConstructionCompanyGUI;
import com.construction.app.service.Construction;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.util.EnumSet;

@SpringBootApplication
public class ConstructionApp {

    public static void main(String[] args) {
        //AWT environment immediately initialized
        System.setProperty("java.awt.headless", "false");

        new SpringApplicationBuilder(ConstructionApp.class).headless(false).run(args);
    }

    @Bean
    public CommandLineRunner initData(ProjectRepository projectRepo, EmployeeRepository empRepo,
                                      Construction service, ConstructionCompanyGUI gui) {
        return args -> {


            if (projectRepo.count() == 0 && empRepo.count() == 0) {

                Bureau hq = new Bureau();
                hq.setBureauName("Main Construction Office");

                Customer defaultClient = new Customer();
                defaultClient.setCompanyOrPersonalName("City Government");

                Employee john = createEmployee(empRepo, "John", "Doe", "Senior Manager", "Electrical", EnumSet.of(EmployeeType.MANAGER, EmployeeType.ENGINEER));
                Employee alice = createEmployee(empRepo, "Alice", "Smith", "Junior Manager", null, EnumSet.of(EmployeeType.MANAGER, EmployeeType.LANDSCAPE_ARCHITECT));
                Employee bob = createEmployee(empRepo, "Bob", "Johnson", null, "Electrical", EnumSet.of(EmployeeType.ENGINEER));
                Employee clara = createEmployee(empRepo, "Clara", "Woods", null, null, EnumSet.of(EmployeeType.LANDSCAPE_ARCHITECT));
                Employee david = createEmployee(empRepo, "David", "Miller", "Junior Manager", null, EnumSet.of(EmployeeType.MANAGER));
                Employee eva = createEmployee(empRepo, "Eva", "Stone", null, "Mechanical", EnumSet.of(EmployeeType.ENGINEER));
                Employee frank = createEmployee(empRepo, "Frank", "Ocean", "Middle Manager", "Plumbing", EnumSet.of(EmployeeType.MANAGER, EmployeeType.ENGINEER));
                Employee grace = createEmployee(empRepo, "Grace", "Hopper", "Middle Manager", null, EnumSet.of(EmployeeType.MANAGER));
                Employee henry = createEmployee(empRepo,  "Henry", "Ford", null, "Mechanical", EnumSet.of(EmployeeType.ENGINEER));
                Employee ivy = createEmployee(empRepo,  "Ivy", "Green", null, null, EnumSet.of(EmployeeType.LANDSCAPE_ARCHITECT));
                Employee jack = createEmployee(empRepo, "Jack", "Ryan", "Senior Manager", null, EnumSet.of(EmployeeType.MANAGER));
                Employee karen = createEmployee(empRepo,  "Karen", "Hill", null, "Electrical", EnumSet.of(EmployeeType.ENGINEER));


                Project techCorp = createProject(projectRepo,  new Project(), "TechCorp Headquarters", ProjectCategory.BUSINESS);
                Project nextGen = createProject(projectRepo,  new Project(), "NextGen Logistics Center", ProjectCategory.BUSINESS);
                Project pineview = createProject(projectRepo,  new Project(), "Pineview Apartment Complex", ProjectCategory.RESIDENTIAL);
                Project sunset = createProject(projectRepo,  new Project(), "Sunset Valley Villas", ProjectCategory.RESIDENTIAL);

                LandscapeProject centralPark = (LandscapeProject) createProject(projectRepo,  new LandscapeProject(), "City Central Park Renovation", ProjectCategory.BUSINESS);
                LandscapeProject riverfront = (LandscapeProject) createProject(projectRepo,  new LandscapeProject(), "Riverfront Promenade", ProjectCategory.BUSINESS);

                InteriorProject cafe = (InteriorProject) createProject(projectRepo,  new InteriorProject(), "Downtown Cafe Redesign", ProjectCategory.BUSINESS);
                InteriorProject hotel = (InteriorProject) createProject(projectRepo,  new InteriorProject(), "Luxe Hotel Lobby", ProjectCategory.BUSINESS);


                service.assignEmployee(techCorp, john, "Senior Manager", "Lead Project Manager", LocalDate.parse("2024-01-15"), LocalDate.parse("2026-12-31"));
                service.assignEmployee(techCorp, john, "Engineer (Electrical)", "Lead Electrical Grid Designer", LocalDate.parse("2024-01-15"), LocalDate.parse("2025-06-30"));
                service.assignEmployee(techCorp, eva, "Engineer (Mechanical)", "HVAC Specialist", LocalDate.parse("2024-03-01"), LocalDate.parse("2025-10-15"));

                service.assignEmployee(nextGen, grace, "Middle Manager", "Site Supervisor", LocalDate.parse("2024-04-01"), LocalDate.parse("2025-12-01"));
                service.assignEmployee(nextGen, henry, "Engineer (Mechanical)", "Structural Integrity", LocalDate.parse("2024-05-15"), LocalDate.parse("2026-01-20"));

                service.assignEmployee(pineview, david, "Junior Manager", "Residential Coordinator", LocalDate.parse("2023-11-01"), LocalDate.parse("2025-04-30"));
                service.assignEmployee(pineview, frank, "Engineer (Plumbing)", "Plumbing Layout Lead", LocalDate.parse("2023-12-10"), LocalDate.parse("2024-11-30"));
                service.assignEmployee(pineview, karen, "Engineer (Electrical)", "Wiring Specialist", LocalDate.parse("2024-01-05"), LocalDate.parse("2024-12-15"));

                service.assignEmployee(sunset, jack, "Senior Manager", "Head of Construction", LocalDate.parse("2024-05-01"), LocalDate.parse("2026-05-01"));
                service.assignEmployee(sunset, frank, "Middle Manager", "Water Systems Lead", LocalDate.parse("2024-06-15"), LocalDate.parse("2025-09-28"));

                service.assignEmployee(centralPark, alice, "Landscape Architect", "Head Landscape Designer", LocalDate.parse("2024-04-01"), LocalDate.parse("2025-10-15"));
                service.assignEmployee(centralPark, alice, "Junior Manager", "On-Site Team Manager", LocalDate.parse("2024-06-01"), LocalDate.parse("2025-08-30"));
                service.assignEmployee(centralPark, clara, "Landscape Architect", "Junior Botanist", LocalDate.parse("2024-05-15"), LocalDate.parse("2025-08-30"));

                service.assignEmployee(riverfront, ivy, "Landscape Architect", "Urban Planner Integration", LocalDate.parse("2024-08-10"), LocalDate.parse("2025-09-30"));
                service.assignEmployee(riverfront, david, "Junior Manager", "Permit Specialist", LocalDate.parse("2024-07-01"), LocalDate.parse("2025-01-30"));

                service.assignEmployee(cafe, john, "Senior Manager", "Client Liaison", LocalDate.parse("2024-09-01"), LocalDate.parse("2024-12-31"));
                service.assignEmployee(cafe, karen, "Engineer (Electrical)", "Lighting Consultant", LocalDate.parse("2024-09-15"), LocalDate.parse("2024-11-15"));
                service.assignEmployee(cafe, bob, "Engineer (Electrical)", "Power Grid Lead", LocalDate.parse("2024-09-15"), LocalDate.parse("2024-12-01"));

                service.assignEmployee(hotel, jack, "Senior Manager", "Interior Procurement Lead", LocalDate.parse("2024-07-01"), LocalDate.parse("2025-12-31"));
                service.assignEmployee(hotel, eva, "Engineer (Mechanical)", "Ventilation Architect", LocalDate.parse("2024-08-01"), LocalDate.parse("2025-06-30"));
            }else{System.out.println("skip injection.");}

            SwingUtilities.invokeLater(() -> {
                gui.loadMemoryData();
                gui.revalidate();
                gui.setVisible(true);
            });
        };
    }


    private Employee createEmployee(EmployeeRepository repo, String name, String surname, String tier, String specialty, EnumSet<EmployeeType> types) {
        Employee emp = new Employee();
        Bureau bureau = new Bureau();
        bureau.setBureauName("Standard Office");
        emp.setBureau(bureau);
        emp.setName(name);
        emp.setSurname(surname);
        emp.setEmployeeTypes(types);
        emp.setManagementTier(tier);
        emp.setSpecialty(specialty);
        return repo.save(emp);
    }

    private Project createProject(ProjectRepository repo, Project project, String name, ProjectCategory category) {
        Customer customer = new Customer();
        customer.setCompanyOrPersonalName("Standard GUI Client");
        project.setCustomer(customer);
        project.setName(name);
        project.setProjectCategory(category);
        project.setBudget(100000.0);
        return repo.save(project);
    }
}
