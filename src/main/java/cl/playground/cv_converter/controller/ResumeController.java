package cl.playground.cv_converter.controller;

import cl.playground.cv_converter.service.ResumeService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> processResume(
        @RequestParam("file")MultipartFile file,
        @RequestParam("language")String language,
        @RequestParam(value = "comments", required = false) String comments
        ) {

        return null;
    }

}
