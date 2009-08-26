/*
 * Test Integer Square Root function
 */

public class SquareRootTest {
  public static void main(String args[]) {
    int x = -2;
    debug("" + (x == -2L));

    // perform timings...
    //testSpeed();

    // accuracy test...
    testAccuracy();

    // hang...
    do {} while (true);
  }

  static void testSpeed() {
    int i;
    long newtime;
    long oldtime;
    int N = 10000000;
    int mask = (1 << 16) - 1;

    // SquareRoot.fast_sqrt()...
    oldtime = System.currentTimeMillis();

    for (i = 0; i < N; i++) {
      int temp = SquareRoot.fastSqrt(i & mask);
    }

    newtime = System.currentTimeMillis();
    debug("SquareRoot.fast_sqrt:" + (newtime - oldtime));

    // SquareRoot.sqrt()...
    oldtime = System.currentTimeMillis();

    for (i = 0; i < N; i++) {
     int temp = SquareRoot.sqrt(i & mask);
    }

    newtime = System.currentTimeMillis();
    debug("SquareRoot.sqrt:" + (newtime - oldtime));

    /*
       // SquareRoot.mborg_sqrt()...
       oldtime = System.currentTimeMillis();
    
       for (i = 0; i < N; i++) {
          temp = SquareRoot.mborg_sqrt(i & mask);
       }
    
       newtime = System.currentTimeMillis();
       debug("SquareRoot.mborg_sqrt:" + (newtime - oldtime));
    
    
       // SquareRoot.test_sqrt()...
       oldtime = System.currentTimeMillis();
    
       for (i = 0; i < N; i++) {
          temp = SquareRoot.test_sqrt(i & mask);
       }
    
       newtime = System.currentTimeMillis();
       debug("SquareRoot.test_sqrt:" + (newtime - oldtime));
    */

    // java.lang.Math.sqrt()...
    oldtime = System.currentTimeMillis();

    for (i = 0; i < N; i++) {
     int temp = (int) (java.lang.Math.sqrt(i & mask));
    }

    newtime = System.currentTimeMillis();
    debug("java.lang.Math.sqrt:" + (newtime - oldtime));
  }

  static void testAccuracy() {
    int i;
    int a;
    int b;
    int c;
    int d;
    int e;
    int start = 0;
    int last_wrong_value = 0;

    for (i = start; ; i++) {
      a = (int) (java.lang.Math.sqrt(i));
      b = (int) (java.lang.Math.sqrt(i) + 0.5);
      c = SquareRoot.sqrt(i);
      d = SquareRoot.fastSqrt(i);
      e = SquareRoot.accurateSqrt(i);
      // e = SquareRoot.mborg_sqrt(i);
      // f = SquareRoot.test_sqrt(i);

      if (b != e) {
        //if (a > (last_wrong_value * 1.05)) { // don't print too many wrong values - just a sample...
          last_wrong_value = a;
          debug("N:" + i + " - Math.sqrt:" + a + " - Math.sqrt+:" + b + " - accurateSqrt:" + e + " - sqrt:" + c);
        }
      //}
    }
  }

  final static void debug(String o) {
    System.out.println(o);
  }
}
