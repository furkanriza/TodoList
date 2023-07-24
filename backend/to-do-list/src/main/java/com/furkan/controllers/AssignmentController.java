package com.furkan.controllers;

import com.furkan.models.ApplicationUser;
import com.furkan.models.Assignment;
import com.furkan.repository.UserRepository;
import com.furkan.services.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final UserRepository userRepository;

    public AssignmentController(AssignmentService assignmentService, UserRepository userRepository) {
        this.assignmentService = assignmentService;
        this.userRepository = userRepository;
    }

    // tested
    @CrossOrigin
    @GetMapping("/")
    public ResponseEntity<List<Assignment>> getAssignmentsByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //checking if there is an authenticated user before trying to access it
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String usrname = authentication.getName();
            ApplicationUser user = userRepository.findByUsername(usrname).orElse(null);
            if (user == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            List<Assignment> assignments = assignmentService.getAssignmentsByUser_id(user.getUser_id());
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // tested
    @GetMapping("/{assignmentid}")
    public ResponseEntity<List<Assignment>> getUserAssignmentsByAssignmentId(@PathVariable Integer assignmentid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String usrname = authentication.getName();
            ApplicationUser user = userRepository.findByUsername(usrname).orElse(null);
            if (user == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            List<Assignment> assignments = assignmentService.getAssignmentsByUser_id(user.getUser_id());
            ///  OPTIMIZE HERE, IT IS SO WAST
            for (Assignment assignment: assignments) {
                // if searching assignment's id equal to any assignment's id, return the assignment as list
                if (assignment.getId()==assignmentid){
                    List<Assignment> assigns = new ArrayList<>();
                    assigns.add(assignment);
                    return new ResponseEntity<>(assigns, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // tested
    @PostMapping("/")
    public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //set assignment user id with authenticated user's id
            String usrname = authentication.getName();
            Optional<ApplicationUser> optionalApplicationUser = userRepository.findByUsername(usrname);
            int user_id = optionalApplicationUser.get().getUser_id();
            assignment.setUserId(user_id);

            Assignment createdAssignment = assignmentService.createAssignment(assignment);
            return new ResponseEntity<>(createdAssignment, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // tested
    @PutMapping("/{assignmentid}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable Integer assignmentid, @RequestBody Assignment updatedAssignment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //find authenticated user id and deny request if doesn't match with updated assignment's user id. (to block manipulative requests)
            String usrname = authentication.getName();
            Optional<ApplicationUser> optionalApplicationUser = userRepository.findByUsername(usrname);
            int user_id = optionalApplicationUser.get().getUser_id();
            if (user_id != assignmentService.getAssignmentById(assignmentid).get().getUserId()){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // update assignment
            Assignment updated = assignmentService.updateAssignment(assignmentid, updatedAssignment);
            if (updated != null) {
                return new ResponseEntity<>(updated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // tested
    @DeleteMapping("/{assignmentid}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer assignmentid) {
        Optional<Assignment> assignment = assignmentService.getAssignmentById(assignmentid);
        if (assignment.isPresent()) {
            //find authenticated user id and deny request if doesn't match with updated assignment's user id. (to block manipulative requests)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String usrname = authentication.getName();
            Optional<ApplicationUser> optionalApplicationUser = userRepository.findByUsername(usrname);
            int user_id = optionalApplicationUser.get().getUser_id();
            if (user_id != assignmentService.getAssignmentById(assignmentid).get().getUserId()){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // delete assignment

            assignmentService.deleteAssignment(assignmentid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            //failed to return deleted assignment
            /*
            Optional<Assignment> assignment1 = assignmentService.getAssignmentById(assignmentid);
            Assignment deletedAssignment = assignment1.get();
            assignmentService.deleteAssignment(assignmentid);
            List<Assignment> returnList = new ArrayList<>();
            returnList.add(deletedAssignment);
            return new ResponseEntity<>(returnList, HttpStatus.OK);*/
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // tested
    @PatchMapping("/{assignmentid}")
    public ResponseEntity<Assignment> patchAssignment(@PathVariable Integer assignmentid, @RequestBody Assignment updatedAssignment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //find authenticated user id and deny request if doesn't match with updated assignment's user id. (to block manipulative requests)
            String usrname = authentication.getName();
            Optional<ApplicationUser> optionalApplicationUser = userRepository.findByUsername(usrname);
            int user_id = optionalApplicationUser.get().getUser_id();
            if (user_id != assignmentService.getAssignmentById(assignmentid).get().getUserId()){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // update assignment
            Optional<Assignment> existingAssignment = assignmentService.getAssignmentById(assignmentid);
            if (existingAssignment.isPresent()) {
                Assignment assignment = existingAssignment.get();
                if (updatedAssignment.getDescription() != null) {
                    assignment.setDescription(updatedAssignment.getDescription());
                }
                if (updatedAssignment.getStatus() != null) {
                    assignment.setStatus(updatedAssignment.getStatus());
                }

                Assignment updated = assignmentService.updateAssignment(assignmentid, assignment);
                return new ResponseEntity<>(updated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}


