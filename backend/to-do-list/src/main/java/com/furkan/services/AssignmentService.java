package com.furkan.services;

import com.furkan.models.Assignment;
import com.furkan.repository.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {
    private AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Optional<Assignment> getAssignmentById(Integer id) {
        return assignmentRepository.findById(id);
    }

    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public Assignment updateAssignment(Integer id, Assignment updatedAssignment) {
        Assignment existingAssignment = assignmentRepository.findById(id).orElse(null);
        if (existingAssignment != null) {
            existingAssignment.setDescription(updatedAssignment.getDescription());
            existingAssignment.setStatus(updatedAssignment.getStatus());
            return assignmentRepository.save(existingAssignment);
        }
        return null;
    }

    public void deleteAssignment(Integer id) {
        assignmentRepository.deleteById(id);
    }

    public List<Assignment> getAssignmentsByUser_id(Integer user_id) {
        return assignmentRepository.findAllByUserId(user_id);
    }

}
