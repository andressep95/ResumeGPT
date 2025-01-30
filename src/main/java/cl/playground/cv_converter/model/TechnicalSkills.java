package cl.playground.cv_converter.model;

import java.util.List;

public class TechnicalSkills {
    private List<SkillCategory> categories;

    public TechnicalSkills() {
    }

    public TechnicalSkills(List<SkillCategory> categories) {
        this.categories = categories;
    }

    public List<SkillCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<SkillCategory> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "TechnicalSkills{" +
            "categories=" + categories +
            '}';
    }
}
