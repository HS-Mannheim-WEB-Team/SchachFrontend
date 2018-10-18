package web.schach.gruppe6.gui.util;

public enum ColorEnum {
    BLACK("black"), WHITE("white"), RED("red"), BLUE("blue"), GREEN("chartreuse"), BROWN("chocolate");


    private String name;

    ColorEnum(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
