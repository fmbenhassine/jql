package io.github.benas.jcql.code;

@Bar(important = true)
public class Foo implements Named {

    private String name;

    public Foo(String name) {
        this.name = name;
    }

    public void sayHi(String to){
        System.out.println(name + ": Hi " + to);
    }

    @Override
    public String getName() {
        return name;
    }
}
