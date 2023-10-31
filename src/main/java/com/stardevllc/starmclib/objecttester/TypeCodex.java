package com.stardevllc.starmclib.objecttester;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class TypeCodex {
    protected Class<?> mainClass;
    protected Set<Class<?>> classes = new HashSet<>();
    protected String overridePrefix;
    
    public TypeCodex(String overridePrefix, Class<?> mainClass, Class<?>... classes) {
        this.overridePrefix = overridePrefix;
        this.mainClass = mainClass;
        this.classes.add(mainClass);
        if (classes != null) {
            this.classes.addAll(Arrays.asList(classes));
        }
    }

    public Class<?> getMainClass() {
        return mainClass;
    }

    public Set<Class<?>> getClasses() {
        return classes;
    }

    public String getOverridePrefix() {
        return overridePrefix;
    }

    public boolean isValidType(Class<?> clazz) {
        return this.classes.contains(clazz);
    }
    
    public boolean isValidType(Object object) {
        return isValidType(object.getClass());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        TypeCodex typeCodex = (TypeCodex) object;
        return Objects.equals(classes, typeCodex.classes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classes);
    }

    public abstract String serialize(Object object);
    public abstract Object deserialize(String serialized);
}