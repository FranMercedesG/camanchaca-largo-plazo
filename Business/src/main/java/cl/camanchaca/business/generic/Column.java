package cl.camanchaca.business.generic;


import lombok.Getter;

@Getter
public class Column {
    private String name;
    private String id;

    public Column(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public static Column of(String name, String id){
        return new Column(name, id);
    }

}
