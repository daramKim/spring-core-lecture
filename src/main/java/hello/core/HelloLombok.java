package hello.core;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// getter setter를 코드 작성없이 생성해준다.
@Getter
@Setter

// toString을 만들어줌.
@ToString
public class HelloLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok();
        helloLombok.setName("asdsada");

        String name = helloLombok.getName();
        System.out.println("name = " + name);

        System.out.println("toString: "+ helloLombok.toString());
    }
}
