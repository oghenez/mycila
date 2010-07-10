/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Test {
    public static void main(String[] args) {
        B b = new B();
        b.a = "hello";
        System.out.println(((A) b).a);
    }

    static class A {
        public String a;
    }

    static class B extends A {
        public String a;
    }
}
