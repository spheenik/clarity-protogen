package skadistats.clarity.protogen.parser.model;

public class Enumeration {

    public static class Field {

        private final String name;
        private final int value;

        public Field(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Enumeration.Field{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
        }
    }

    private final String name;

    public Enumeration(String name) {
        System.out.println("enum " + name);
        this.name = name;
    }

    public boolean addOption(Object e) {
        System.out.println("added " + e);
        return true;
    }

    public boolean addField(Field e) {
        System.out.println("added " + e);
        return true;
    }

}
