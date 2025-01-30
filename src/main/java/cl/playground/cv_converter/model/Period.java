package cl.playground.cv_converter.model;

public class Period {
    private String start;
    private String end;

    public Period() {
    }

    public Period(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Period{" +
            "start='" + start + '\'' +
            ", end='" + end + '\'' +
            '}';
    }
}
