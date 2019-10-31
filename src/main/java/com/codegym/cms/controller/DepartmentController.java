package com.codegym.cms.controller;

import com.codegym.cms.model.Department;
import com.codegym.cms.model.Employee;
import com.codegym.cms.service.DepartmentService;
import com.codegym.cms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/create-department")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("/department/create");
        modelAndView.addObject("department",new Department());
        return modelAndView;
    }
    @PostMapping("/create-department")
    public ModelAndView saveDepartment(@ModelAttribute("department") Department department){
        departmentService.save(department);
        ModelAndView modelAndView = new ModelAndView("department/create");
        modelAndView.addObject("department",department);
        modelAndView.addObject("message","Create new department success !");
        return modelAndView;
    }
    @GetMapping("/department")
    public ModelAndView listDepartment(){
        Iterable<Department> departments = departmentService.findAll();
        ModelAndView modelAndView = new ModelAndView("department/list");
        modelAndView.addObject("departments",departments);
        return modelAndView;
    }
    @GetMapping("/edit-department/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Department department = departmentService.findById(id);
        ModelAndView modelAndView = new ModelAndView("department/edit");
        modelAndView.addObject("department",department);
        return modelAndView;
    }
    @PostMapping("/edit-department")
    public ModelAndView updateDepartment(@ModelAttribute("department") Department department){
        departmentService.save(department);
       ModelAndView modelAndView = new ModelAndView("department/edit");
       modelAndView.addObject("department",department);
       modelAndView.addObject("message","Update department success!");
       return modelAndView;
    }
    @GetMapping("/delete-department/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        Department department = departmentService.findById(id);
        ModelAndView modelAndView = new ModelAndView("department/delete");
        modelAndView.addObject("department",department);
        return modelAndView;
    }
    @PostMapping("delete-department")
    public String deleteDepartment(@ModelAttribute("department") Department department){
        departmentService.remove(department.getId());
        return "redirect:department";
    }
    @GetMapping("/view-department/{id}")
    public ModelAndView viewDepartment(@PathVariable("id") Long id){
        Department department = departmentService.findById(id);
        Iterable<Employee> employees = employeeService.findAllByDepartment(department);
        ModelAndView modelAndView = new ModelAndView("department/view");
        modelAndView.addObject("department",department);
        modelAndView.addObject("employees",employees);
        return modelAndView;

    }
}
