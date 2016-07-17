package io.github.benas.jcql.code;

@Bar(important = true)
public class Foo extends AbstractFoo implements Named, Gendered {

    private String name;

    private Gender gender;

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

    public Gender getGender() {
        return gender;
    }
}
