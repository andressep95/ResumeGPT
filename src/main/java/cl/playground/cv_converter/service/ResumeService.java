package cl.playground.cv_converter.service;


import cl.playground.cv_converter.model.Resume;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {

    byte[] processResume(MultipartFile file, String language, String comments);
    Resume processResumeData(MultipartFile file, String language, String comments);

}
