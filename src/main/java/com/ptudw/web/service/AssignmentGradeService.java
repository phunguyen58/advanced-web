package com.ptudw.web.service;

import com.ptudw.web.domain.Assignment;
import com.ptudw.web.domain.AssignmentGrade;
import com.ptudw.web.domain.Authority;
import com.ptudw.web.domain.Course;
import com.ptudw.web.domain.GradeBoard;
import com.ptudw.web.domain.GradeComposition;
import com.ptudw.web.domain.User;
import com.ptudw.web.domain.UserCourse;
import com.ptudw.web.domain.enumeration.GradeType;
import com.ptudw.web.repository.AssignmentGradeRepository;
import com.ptudw.web.repository.AssignmentRepository;
import com.ptudw.web.repository.AuthorityRepository;
import com.ptudw.web.repository.CourseRepository;
import com.ptudw.web.repository.GradeCompositionRepository;
import com.ptudw.web.repository.UserCourseRepository;
import com.ptudw.web.repository.UserRepository;
import com.ptudw.web.security.SecurityUtils;
import com.ptudw.web.web.rest.AssignmentResource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssignmentGrade}.
 */
@Service
@Transactional
public class AssignmentGradeService {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AssignmentGradeService.class);

    private final AssignmentGradeRepository assignmentGradeRepository;
    private final UserCourseRepository userCourseRepository;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final GradeCompositionRepository gradeCompositionRepository;
    private final AuthorityRepository authorityRepository;

    public AssignmentGradeService(
        AssignmentGradeRepository assignmentGradeRepository,
        UserCourseRepository userCourseRepository,
        UserRepository userRepository,
        AssignmentRepository assignmentRepository,
        CourseRepository courseRepository,
        GradeCompositionRepository gradeCompositionRepository,
        AuthorityRepository authorityRepository
    ) {
        this.assignmentGradeRepository = assignmentGradeRepository;
        this.userCourseRepository = userCourseRepository;
        this.userRepository = userRepository;
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
        this.gradeCompositionRepository = gradeCompositionRepository;
        this.authorityRepository = authorityRepository;
    }

    public List<GradeBoard> getGradeBoardsByCourseId(Long courseId) {
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));

        Optional<User> currentUser = userRepository.findOneByLogin(userLogin);
        if (!currentUser.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }

        List<UserCourse> userCourses = userCourseRepository.findAllByCourseId(courseId);
        if (!currentUser.get().getAuthorities().contains(authorityRepository.findOneByName("ROLE_TEACHER"))) {
            userCourses =
                userCourses
                    .stream()
                    .filter(userCourse -> userCourse.getUserId().equals(currentUser.get().getId()))
                    .collect(Collectors.toList());
        }

        List<Long> userIds = userCourses.stream().map(userCourse -> userCourse.getUserId()).collect(Collectors.toList());
        List<User> users = userRepository
            .findAllByIdIn(userIds)
            .stream()
            .filter(user -> !user.getAuthorities().contains(authorityRepository.findOneByName("ROLE_TEACHER")))
            .collect(Collectors.toList());
        Course course = courseRepository.findOneById(courseId);
        List<Assignment> assignmentsInCourse = assignmentRepository
            .findAllByCourseId(courseId)
            .stream()
            .filter(assignment -> {
                log.debug("assignment.getGradeComposition().getIsPublic() {}", assignment.getGradeComposition().getIsPublic());
                return assignment.getGradeComposition().getIsPublic();
            })
            .collect(Collectors.toList());
        List<Long> assignmentsIdInCourse = assignmentsInCourse.stream().map(assignment -> assignment.getId()).collect(Collectors.toList());

        List<GradeBoard> gradeBoards = new ArrayList();
        users
            .stream()
            .forEach(user -> {
                List<AssignmentGrade> userAssignmentGrades = assignmentGradeRepository
                    .findAllByStudentId(user.getStudentId())
                    .stream()
                    .filter(grade -> grade.getAssignment().getGradeComposition().getIsPublic())
                    .collect(Collectors.toList());

                List<AssignmentGrade> userAssignmentGradesInCourse = userAssignmentGrades
                    .stream()
                    .filter(assignmentGrade -> assignmentsIdInCourse.contains(assignmentGrade.getAssignment().getId()))
                    .collect(Collectors.toList());
                GradeComposition gradeComposition = gradeCompositionRepository.findOneByCourseId(course.getId());
                Double finalGrade = 0D;
                if (gradeComposition.getType() != null && userAssignmentGradesInCourse.size() > 0) {
                    if (gradeComposition.getType().equals(GradeType.PERCENTAGE)) {
                        long a = userAssignmentGradesInCourse
                            .stream()
                            .map(grade -> grade.getGrade() / 100 * grade.getAssignment().getGradeComposition().getScale())
                            .mapToLong(Long::longValue)
                            .sum();
                        long b = assignmentsInCourse
                            .stream()
                            .map(assignment -> assignment.getGradeComposition().getScale())
                            .mapToLong(Long::longValue)
                            .sum();
                        finalGrade = (double) a / b;
                    } else {
                        long a = userAssignmentGradesInCourse.stream().map(grade -> grade.getGrade()).mapToLong(Long::longValue).sum();
                        long b = assignmentsInCourse
                            .stream()
                            .map(assignment -> assignment.getGradeComposition().getScale())
                            .mapToLong(Long::longValue)
                            .sum();
                        finalGrade = (double) a / b;
                    }
                }
                gradeBoards.add(
                    new GradeBoard(
                        user,
                        user.getStudentId(),
                        userAssignmentGradesInCourse,
                        assignmentsInCourse,
                        finalGrade,
                        gradeComposition.getType(),
                        course
                    )
                );
            });
        // List<Assignment> assignments

        log.debug("Request to get all AssignmentGrades");

        return gradeBoards;
    }

    /**
     * Save a assignmentGrade.
     *
     * @param assignmentGrade the entity to save.
     * @return the persisted entity.
     */
    public AssignmentGrade save(AssignmentGrade assignmentGrade) {
        log.debug("Request to save AssignmentGrade : {}", assignmentGrade);
        return assignmentGradeRepository.save(assignmentGrade);
    }

    /**
     * Update a assignmentGrade.
     *
     * @param assignmentGrade the entity to save.
     * @return the persisted entity.
     */
    public AssignmentGrade update(AssignmentGrade assignmentGrade) {
        log.debug("Request to update AssignmentGrade : {}", assignmentGrade);
        return assignmentGradeRepository.save(assignmentGrade);
    }

    /**
     * Partially update a assignmentGrade.
     *
     * @param assignmentGrade the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AssignmentGrade> partialUpdate(AssignmentGrade assignmentGrade) {
        log.debug("Request to partially update AssignmentGrade : {}", assignmentGrade);

        return assignmentGradeRepository
            .findById(assignmentGrade.getId())
            .map(existingAssignmentGrade -> {
                if (assignmentGrade.getStudentId() != null) {
                    existingAssignmentGrade.setStudentId(assignmentGrade.getStudentId());
                }
                if (assignmentGrade.getGrade() != null) {
                    existingAssignmentGrade.setGrade(assignmentGrade.getGrade());
                }
                if (assignmentGrade.getIsDeleted() != null) {
                    existingAssignmentGrade.setIsDeleted(assignmentGrade.getIsDeleted());
                }
                if (assignmentGrade.getCreatedBy() != null) {
                    existingAssignmentGrade.setCreatedBy(assignmentGrade.getCreatedBy());
                }
                if (assignmentGrade.getCreatedDate() != null) {
                    existingAssignmentGrade.setCreatedDate(assignmentGrade.getCreatedDate());
                }
                if (assignmentGrade.getLastModifiedBy() != null) {
                    existingAssignmentGrade.setLastModifiedBy(assignmentGrade.getLastModifiedBy());
                }
                if (assignmentGrade.getLastModifiedDate() != null) {
                    existingAssignmentGrade.setLastModifiedDate(assignmentGrade.getLastModifiedDate());
                }
                if (assignmentGrade.getGradeReviewId() != null) {
                    existingAssignmentGrade.setGradeReviewId(assignmentGrade.getGradeReviewId());
                }

                return existingAssignmentGrade;
            })
            .map(assignmentGradeRepository::save);
    }

    /**
     * Get all the assignmentGrades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssignmentGrade> findAll(Pageable pageable) {
        log.debug("Request to get all AssignmentGrades");
        return assignmentGradeRepository.findAll(pageable);
    }

    /**
     * Get one assignmentGrade by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssignmentGrade> findOne(Long id) {
        log.debug("Request to get AssignmentGrade : {}", id);
        return assignmentGradeRepository.findById(id);
    }

    /**
     * Delete the assignmentGrade by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AssignmentGrade : {}", id);
        assignmentGradeRepository.deleteById(id);
    }

    public List<AssignmentGrade> saveAll(List<AssignmentGrade> assignmentGrades) {
        log.debug("Request to save a list of AssignmentGrades");
        return assignmentGradeRepository.saveAll(assignmentGrades);
    }

    public void updateAssignmentGradeByExcel(List<List<String>> data, Long assignmentId) {
        log.debug("Request to update assignment grade by excel");
        List<AssignmentGrade> assignmentGrades = assignmentGradeRepository.findAllByAssignmentId(assignmentId);
        for (List<String> row : data) {
            for (AssignmentGrade assignmentGrade : assignmentGrades) {
                if (row.get(0).equals(assignmentGrade.getStudentId())) {
                    assignmentGrade.setGrade(Long.parseLong(row.get(1)));
                    assignmentGradeRepository.save(assignmentGrade);
                }
            }
        }
    }
}
