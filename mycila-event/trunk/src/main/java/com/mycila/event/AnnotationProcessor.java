package com.mycila.event;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotationProcessor {

    private final Dispatcher dispatcher;

    public AnnotationProcessor(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void process(Object o) {
        
    }

    /**
     * Returns true if a overrides b. Assumes signatures of a and b are the same and a's declaring
     * class is a subclass of b's declaring class.
     */
    private static boolean overrides(Method a, Method b) {
      // See JLS section 8.4.8.1
      int modifiers = b.getModifiers();
      if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
        return true;
      }
      if (Modifier.isPrivate(modifiers)) {
        return false;
      }
      // b must be package-private
      return a.getDeclaringClass().getPackage().equals(b.getDeclaringClass().getPackage());
    }
    private static List<Class<?>> hierarchyFor(Class<?> type) {
      List<Class<?>> hierarchy = new ArrayList<Class<?>>();
      Class<?> current = type;
      while (current != Object.class) {
        hierarchy.add(current);
        current = current.getSupertype(current.getRawType().getSuperclass());
      }
      return hierarchy;
    }

}
