package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.Student;
import com.example.demo.repo.StudentRepo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {  
    @Autowired
    private StudentRepo repo;

    // Get all the students
    @GetMapping
    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    // Get a student by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable int id) {
        Optional<Student> student = repo.findById(id);
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        } else {
            return ResponseEntity.badRequest().body("ID not found");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        if (repo.existsByRegdNo(student.getRegdNo())) {
            return ResponseEntity.badRequest().body("Error: Registration number already exists!");
        }

        if (repo.existsByEmail(student.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email ID already exists!");
        }

        Student savedStudent = repo.save(student);
        return ResponseEntity.ok(savedStudent);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable int id, @RequestBody Student student) {
        if (repo.existsById(id)) {
            Student existingStudent = repo.findById(id).orElseThrow();
            existingStudent.setStudName(student.getStudName());
            existingStudent.setRegdNo(student.getRegdNo());
            existingStudent.setEmail(student.getEmail());
            existingStudent.setCreatedBy(student.getCreatedBy());
     
            Student updatedStudent = repo.save(existingStudent);
            return ResponseEntity.ok(updatedStudent);
        }
        return ResponseEntity.badRequest().body("Error: Student ID not found!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable int id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().body("Error: Student ID not found!");
        }
        repo.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}



