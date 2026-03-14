package model;

public class Building {
    
    private String code = "";
    private String name = "";

    public void load(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
