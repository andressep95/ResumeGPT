package cl.playground.cv_converter.model;

import java.util.ArrayList;
import java.util.List;

public class Resume {
    private Header header;
    private List<Education> education;
    private TechnicalSkills technicalSkills;
    private List<ProfessionalExperience> professionalExperience;
    private List<Certification> certifications;
    private List<Project> projects;

    public Resume() {
        this.education = new ArrayList<>();
        this.technicalSkills = new TechnicalSkills();
        this.professionalExperience = new ArrayList<>();
        this.certifications = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public Resume(Header header, List<Education> education, TechnicalSkills technicalSkills, List<ProfessionalExperience> professionalExperience, List<Certification> certifications, List<Project> projects) {
        this.header = header;
        this.education = education;
        this.technicalSkills = technicalSkills;
        this.professionalExperience = professionalExperience;
        this.certifications = certifications;
        this.projects = projects;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education;
    }

    public TechnicalSkills getTechnicalSkills() {
        return technicalSkills;
    }

    public void setTechnicalSkills(TechnicalSkills technicalSkills) {
        this.technicalSkills = technicalSkills;
    }

    public List<ProfessionalExperience> getProfessionalExperience() {
        return professionalExperience;
    }

    public void setProfessionalExperience(List<ProfessionalExperience> professionalExperience) {
        this.professionalExperience = professionalExperience;
    }

    public List<Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "Resume{" +
            "header=" + header +
            ", education=" + education +
            ", technicalSkills=" + technicalSkills +
            ", professionalExperience=" + professionalExperience +
            ", certifications=" + certifications +
            ", projects=" + projects +
            '}';
    }
}
