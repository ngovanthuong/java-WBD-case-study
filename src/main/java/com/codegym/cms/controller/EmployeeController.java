package com.codegym.cms.controller;

import com.codegym.cms.model.Department;
import com.codegym.cms.model.Employee;
import com.codegym.cms.model.EmployeeForm;
import com.codegym.cms.service.DepartmentService;
import com.codegym.cms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@PropertySource("classpath:global_config_app.properties")
public class EmployeeController {

    @Autowired
    Environment env;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @ModelAttribute("department")
    public Iterable<Department> departments(){
        return departmentService.findAll();
    }

    @GetMapping("/create-employee")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("/employee/create");
        modelAndView.addObject("employeeForm", new EmployeeForm());
        return modelAndView;
    }

    @PostMapping("/create-employee")
    public ModelAndView saveEmployee(@Validated @ModelAttribute("employeeForm") EmployeeForm employeeForm, BindingResult result){
        // thong bao neu xay ra loi
        if(result.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("/employee/create");
            modelAndView.addObject("employee",new EmployeeForm());
            return modelAndView;
        }
        //lay ten file
        MultipartFile multipartFile = employeeForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("file_upload").toString();

        //luu file len server
        try {
            //multipartFile.transferTo(imageFile);
            FileCopyUtils.copy(employeeForm.getImage().getBytes(),new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // tao doi tuong luu vao db

        Employee employeeObject = new Employee(employeeForm.getName(),
                employeeForm.getBirthDate(),
                employeeForm.getAddress(),
                fileName,employeeForm.getSalary(),employeeForm.getDepartment());

        //luu vao db
        //employeeService.save(employee)

        employeeService.save(employeeObject);

        ModelAndView modelAndView = new ModelAndView("/employee/create");
        modelAndView.addObject("employee",new EmployeeForm());
        modelAndView.addObject("message","success");
        return modelAndView;

//        employeeService.save(employee);
//        ModelAndView modelAndView = new ModelAndView("/employee/create");
//        modelAndView.addObject("employee", new Employee());
//        modelAndView.addObject("message", "New customer created successfully");
//        return modelAndView;

    }

    @GetMapping("/employee")
    public ModelAndView listEmployee(@RequestParam("s") Optional<String> s, @PageableDefault(2) Pageable pageable){
        Page<Employee> employees ;
        if(s.isPresent()){
            employees = employeeService.findAllByNameContaining(s.get(), pageable);
        } else {
            employees = employeeService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/employee/list");
        modelAndView.addObject("employees",employees);
        return modelAndView;

    }

    @GetMapping("/edit-employee/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Employee employee = employeeService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/employee/edit");
        modelAndView.addObject("employee",employee);
        return modelAndView;
    }

    @PostMapping("/edit-employee")
    public ModelAndView updateEmployee(@ModelAttribute("employee") EmployeeForm employeeForm){
        Long id = employeeForm.getId();
        String name = employeeForm.getName();
        String birthDate= employeeForm.getBirthDate();
        String address= employeeForm.getAddress();
        String filename = employeeForm.getImage().getOriginalFilename();
        Double salary= employeeForm.getSalary();
        Department department = employeeForm.getDepartment();
        Employee employee = new Employee(id,name,birthDate,address,filename,salary,department);
        employeeService.save(employee);
        ModelAndView modelAndView = new ModelAndView("/employee/edit");
        modelAndView.addObject("employee",employee);
        modelAndView.addObject("message","Update employee success");
        return modelAndView;
    }

    @GetMapping("/delete-employee/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        Employee employee = employeeService.findById(id);
        ModelAndView modelAndView = new ModelAndView("employee/delete");
        modelAndView.addObject("employee",employee);
        return modelAndView;
    }

    @PostMapping("/delete-employee")
    public String DeleteEmployee(@ModelAttribute("employee") Employee employee){
        employeeService.remove(employee.getId());
        return "redirect:employee";
    }
}
