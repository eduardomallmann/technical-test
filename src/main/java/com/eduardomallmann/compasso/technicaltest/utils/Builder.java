package com.eduardomallmann.compasso.technicaltest.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Builder pattern implementation.
 * <p>Acquired from https://howtocodetutorial.wordpress.com/generic-builder-pattern-in-java-8 in 07/22/2019</p>
 *
 * @param <T> The class of the object that will be sent in the response content.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
public class Builder<T> {

    private T instance;
    private boolean ifCond = true;

    /**
     * Private constructor to be use internally only.
     *
     * @param clazz   the class of the instance
     * @param objects parameters to instantiate the constructor
     */
    private Builder(Class<T> clazz, Object... objects) {
        try {
            Stream<Constructor<?>> constructors = Stream.of(clazz.getDeclaredConstructors()).peek(constructor -> constructor.setAccessible(true));
            Optional<Constructor<?>> constructor;
            if (null == objects) {
                constructor = constructors.filter(cons -> cons.getParameterCount() == 0).findFirst();
                if (constructor.isPresent()) {
                    instance = clazz.cast(constructor.get().newInstance());
                }
            } else {
                constructor = constructors.filter(cons -> cons.getParameterCount() == objects.length)
                                      .filter(cons -> {
                                          List<Class<?>> consClass = Arrays.asList(cons.getParameterTypes());
                                          List<Class<?>> paramsClass = Arrays.stream(objects).map(Object::getClass).collect(Collectors.toList());
                                          return consClass.stream()
                                                         .allMatch(con -> paramsClass.stream()
                                                                                  .anyMatch(param -> (con.isPrimitive() &&
                                                                                                              (Number.class.isAssignableFrom(param) ||
                                                                                                                       Boolean.class.isAssignableFrom(param) ||
                                                                                                                       Character.class.isAssignableFrom(param) ||
                                                                                                                       Byte.class.isAssignableFrom(param))) ||
                                                                                                             param.equals(con) ||
                                                                                                             param.isAssignableFrom(con) ||
                                                                                                             con.isAssignableFrom(param)));
                                      })
                                      .findFirst();
                if (constructor.isPresent()) {
                    instance = clazz.cast(constructor.get().newInstance(objects));
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Instantiate a new Builder.
     *
     * @param clazz class of the instance
     * @param <T>   type of the class
     *
     * @return an instance of builder.
     */
    public static <T> Builder<T> create(Class<T> clazz) {
        return new Builder<>(clazz, (Object[]) null);
    }

    /**
     * Instantiate a new Builder with parameters.
     *
     * @param clazz   class of the instance
     * @param <T>     type of the class
     * @param objects parameters to instantiate the constructor
     *
     * @return an instance of builder.
     */
    public static <T> Builder<T> create(Class<T> clazz, Object... objects) {
        return new Builder<>(clazz, objects);
    }

    /**
     * Set a value into a parameter of the instance.
     *
     * @param setter Consumer to insert the value
     *
     * @return the current builder.
     */
    public Builder<T> with(Consumer<T> setter) {
        if (ifCond)
            setter.accept(instance);
        return this;
    }

    /**
     * @return the instance to the producer.
     */
    public T build() {
        return instance;
    }

    /**
     * Creates a flux control to insert the value of the parameters.
     *
     * @param condition Condition to be analyzed
     *
     * @return the current builder.
     */
    public Builder<T> If(BooleanSupplier condition) {
        this.ifCond = condition.getAsBoolean();
        return this;
    }

    /**
     * Finalize the flux control.
     *
     * @return the current builder.
     */
    public Builder<T> endIf() {
        this.ifCond = true;
        return this;
    }
}

