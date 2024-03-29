package com.ptudw.web.web.rest;

import com.ptudw.web.service.AssignmentGradeService;
import com.ptudw.web.service.ExcelService;
import com.ptudw.web.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class ExcelResource {

    @Autowired
    private ExcelService excelService;

    private final UserService userService;

    private final AssignmentGradeService assignmentGradeService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public ExcelResource(UserService userService, AssignmentGradeService assignmentGradeService) {
        this.userService = userService;
        this.assignmentGradeService = assignmentGradeService;
    }

    @PostMapping("/upload/student-ids-mapping")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<List<String>>> uploadExcelFileToMapStudentIds(@RequestParam("file") MultipartFile file) {
        try {
            List<List<String>> data = excelService.readExcelFile(file);
            userService.mapStudentIdsByExcel(data);

            return ResponseUtil.wrapOrNotFound(
                Optional.of(data),
                HeaderUtil.createAlert(applicationName, "userManagement.studentIdsUpdate", "studentIds")
            );
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/upload/assignment-grades/{assignmentId}")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<List<List<String>>> uploadExcelFileToMapStudentIds(
        @RequestParam("file") MultipartFile file,
        @PathVariable Long assignmentId
    ) {
        try {
            List<List<String>> data = excelService.readExcelFile(file);

            assignmentGradeService.updateAssignmentGradeByExcel(data, assignmentId);

            return ResponseUtil.wrapOrNotFound(
                Optional.of(data),
                HeaderUtil.createAlert(applicationName, "assignment.assignmentGradeUpdated", "assignmentGrade")
            );
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
