package cl.playground.cv_converter.service;


import cl.playground.cv_converter.model.Resume;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;


public interface ResumeService {

    byte[] processResume(MultipartFile file, String language, String comments);

    CompletableFuture<Resume> processResumeData(MultipartFile file, String language, String comments);

}
