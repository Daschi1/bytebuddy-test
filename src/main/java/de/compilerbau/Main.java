package de.compilerbau;

import java.io.File;
import java.io.IOException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Unloaded;

public class Main {

  public static void main(String[] args) {
    Builder<Object> builder = new ByteBuddy()
        .subclass(Object.class)
        .name("CreatedClass");
    try(Unloaded<Object> make = builder.make()){
      make.saveIn(new File("generated"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}