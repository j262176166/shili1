package app.gl.com.shili;

/**
 * Created by Administrator on 2018/3/23.
 */

public class Person {
    public Person (int id,String name){
        super();
        this.id = id;
        this.name = name;
    }
    private int id ;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
