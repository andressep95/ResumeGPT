package cl.playground.cv_converter.model;

import java.util.List;

public class SkillCategory {
    private List<String> skills;

    public SkillCategory() {
    }

    public SkillCategory(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "SkillCategory{" +
            "skills=" + skills +
            '}';
    }
}
