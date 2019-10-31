package com.codegym.cms.model;
import org.springframework.cglib.core.Local;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String birthDate;
    private String address;
    private String image;
    private Double salary;
    @ManyToOne
    @JoinColumn(name="department_id")
    private Department department;

    public Employee(Long id, String name, String birthDate, String address, String image, Double salary, Department department) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
        this.image = image;
        this.salary = salary;
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee() {
    }

    public Employee(String name, String birthDate, String address, String image, Double salary,Department department) {
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
        this.image = image;
        this.salary = salary;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
