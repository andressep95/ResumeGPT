package cl.playground.cv_converter.model;

import java.util.List;

public class TechnicalSkills {
    private List<String> skills;

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "TechnicalSkills{" +
            "skills=" + skills +
            '}';
    }
}
