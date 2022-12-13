package de.compilerbau;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class Main {

  public static void main(String[] args) {
    Builder<Object> builder =
        new ByteBuddy()
            .subclass(Object.class)
            .name("CreatedClass")
            .defineMethod("test", String.class, Modifier.PUBLIC)
            .intercept(FixedValue.value("Hello world!"));
    try(Unloaded<Object> make = builder.make()){
      make.saveIn(new File("generated"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
      File f = new File("generated");
      URL[] cp = {f.toURI().toURL()};
      URLClassLoader urlcl = new URLClassLoader(cp);
      Class myclass  = urlcl.loadClass("CreatedClass");
      Method test = myclass.getDeclaredMethod("test");
      Object invoke = test.invoke(myclass.getDeclaredConstructor().newInstance());
      System.out.println(invoke);
    } catch (ClassNotFoundException | MalformedURLException | NoSuchMethodException |
             InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}