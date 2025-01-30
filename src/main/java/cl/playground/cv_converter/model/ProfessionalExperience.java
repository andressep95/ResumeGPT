package cl.playground.cv_converter.model;

import java.util.List;

public class ProfessionalExperience {
    private String company;
    private String location;
    private String position;
    private Period period;
    private List<String> responsibilities;

    public ProfessionalExperience() {
    }

    public ProfessionalExperience(String company, String location, String position, Period period, List<String> responsibilities) {
        this.company = company;
        this.location = location;
        this.position = position;
        this.period = period;
        this.responsibilities = responsibilities;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public List<String> getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(List<String> responsibilities) {
        this.responsibilities = responsibilities;
    }

    @Override
    public String toString() {
        return "ProfessionalExperience{" +
            "company='" + company + '\'' +
            ", location='" + location + '\'' +
            ", position='" + position + '\'' +
            ", period=" + period +
            ", responsibilities=" + responsibilities +
            '}';
    }
}
