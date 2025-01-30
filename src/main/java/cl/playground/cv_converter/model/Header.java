package cl.playground.cv_converter.model;

public class Header {
    private String name;
    private Contact contact;

    public Header() {
    }

    public Header(String name, Contact contact) {
        this.name = name;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Header{" +
            "name='" + name + '\'' +
            ", contact=" + contact +
            '}';
    }
}
