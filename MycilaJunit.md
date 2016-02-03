## Junit integration ##

  * MycilaJunit3Test
  * MycilaJunit4Test
  * MycilaJunitRunner

### Junit 3 ###

```
public final class MyTest extends MycilaJunit3Test {
    public void test_something() {
        // test something
    }
}
```

### Junit >= 4.5 ###

```
public final class MyTest extends MycilaJunit4Test {
    @Test
    public void test_something() {
        // test something
    }
}
```

Preferred way since does not extend any class:

```
@RunWith(MycilaJunitRunner.class)
public final class MyTest {
    @Test
    public void test_something() {
        // test something
    }
}
```