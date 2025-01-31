package cl.playground.cv_converter.model;

import java.util.List;

public class Education {
    private String institution;
    private String degree;
    private String graduationDate;
    private List<String> achievements;

    public Education() {
    }

    public Education(String institution, String degree, String graduationDate, List<String> achievements) {
        this.institution = institution;
        this.degree = degree;
        this.graduationDate = graduationDate;
        this.achievements = achievements;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(String graduationDate) {
        this.graduationDate = graduationDate;
    }

    public List<String> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<String> achievements) {
        this.achievements = achievements;
    }

    @Override
    public String toString() {
        return "Education{" +
            "institution='" + institution + '\'' +
            ", degree='" + degree + '\'' +
            ", graduationDate='" + graduationDate + '\'' +
            ", achievements=" + achievements +
            '}';
    }
}
