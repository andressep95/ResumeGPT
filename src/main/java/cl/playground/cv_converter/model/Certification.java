package cl.playground.cv_converter.model;

public class Certification {
    private String name;
    private String dateObtained;

    public Certification() {
    }

    public Certification(String name, String dateObtained) {
        this.name = name;
        this.dateObtained = dateObtained;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateObtained() {
        return dateObtained;
    }

    public void setDateObtained(String dateObtained) {
        this.dateObtained = dateObtained;
    }

    @Override
    public String toString() {
        return "Certification{" +
            "name='" + name + '\'' +
            ", dateObtained='" + dateObtained + '\'' +
            '}';
    }
}
