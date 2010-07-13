package test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Main {
    public static void main(String[] args) {
        System.out.println(Main.class.getResource("/test/Main.class"));
        System.out.println(Main.class.getResource("/org/objectweb/asm/ClassVisitor.class"));
    }
}
