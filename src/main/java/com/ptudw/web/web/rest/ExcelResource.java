package com.ptudw.web.web.rest;

import com.ptudw.web.service.ExcelService;
import com.ptudw.web.service.UserService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ExcelResource {

    @Autowired
    private ExcelService excelService;

    private final UserService userService;

    public ExcelResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/upload/student-ids-mapping")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<List<String>>> uploadExcelFileToMapStudentIds(@RequestParam("file") MultipartFile file) {
        try {
            List<List<String>> data = excelService.readExcelFile(file);
            userService.mapStudentIdsByExcel(data);
            return ResponseEntity.ok(data);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
