import java.util.concurrent.Callable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ClosureTest {

    public static interface BiOp {
        public int exec(int a, int b);
    }

    public static interface UniOp {
        public double exec(int a);
    }

    public static void main(String[] args) {
        // normal JDK closure
        BiOp myBiOp = (a, b)->a + b;
        assert myBiOp.exec(4, 6) == 10;

        // using closure object
        Closure<Integer> add = Closure.of((BiOp) (a, b)->a + b);

        // closure are Runnable and Callable
        assert Runnable.class.isAssignableFrom(Closure.class);
        assert Callable.class.isAssignableFrom(Closure.class);

        // display closure
        assert add.toString().equals("int ClosureTest$BiOp.exec(int, int)");

        // call with arguments
        assert add.call(4, 6) == 10;

        // get information
        assert add.getDelegate() instanceof BiOp;
        assert add.getParameterCount() == 2;
        assert add.getReturnType() == int.class;

        // call omitting arguments (thus throw an error)
        try {
            System.out.println(add.call());
            add.run();
        } catch (Closure.Exception e) {
            assert e.getMessage().equals("Bad parameter count: 0. Requires 2 for calling closure int ClosureTest$BiOp.exec(int, int)");
        }

        // composition
        Closure<Double> inv = Closure.of((UniOp) (a)->1.0 / a);
        Closure<Double> addInv = add.then(inv);
        assert addInv.toString().equals("double ClosureTest$UniOp.exec(ClosureTest$BiOp.exec(int, int))");
        assert inv.call(10) == 0.1;
        assert addInv.call(4, 6) == 0.1;

        // currying
        Closure<Double> curried1 = addInv.curry(4);
        Closure<Double> curried2 = addInv.curry(6, 4);
        assert curried1.toString().equals("double ClosureTest$UniOp.exec(ClosureTest$BiOp.exec(4, int))");
        assert curried2.toString().equals("double ClosureTest$UniOp.exec(ClosureTest$BiOp.exec(6, 4))");
        assert curried1.call(6) == 0.1;
        assert curried2.call() == 0.1;

    }
}
