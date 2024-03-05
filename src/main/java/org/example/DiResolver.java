package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DiResolver {
    Map<Class, Object> objects = new HashMap<>();
    Map<Class, Supplier> recipes = new HashMap<>();


    static Class getSupplierReturnType(Class supplierClass) {
        try {
            return supplierClass.getMethod("get").getReturnType();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("unable to get class " + supplierClass.getName() + " return type");
        }
    }

    static Object createFromParams(Constructor ctor, Object[] params) {
        try {
            return ctor.newInstance(params);
        } catch (InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException("unable to get class " + ctor.getName() + " return type");
        }
    }

    public DiResolver withSuppliers(Class... suppliers) {
        for (Class<Supplier> supplierClass : suppliers) {
            if (!Supplier.class.isAssignableFrom(supplierClass)) {
                throw new RuntimeException(supplierClass.getName() + " is not a supplier");
            }
            Class returnClass = getSupplierReturnType(supplierClass);
            objects.put(returnClass, null);
            recipes.put(returnClass, () -> {
                Supplier supplierInstance = create(supplierClass);
                return supplierInstance.get();
            });
        }
        return this;
    }

    public DiResolver withEntities(Class... classes) {
        for (Class e : classes) {
            objects.put(e, null);
            recipes.put(e, () -> create(e));
        }
        return this;
    }

    <T> T create(Class request) { // todo get rid of recursion
        if (objects.containsKey(request) && objects.get(request) != null) {
            return (T) objects.get(request);
        }
        Constructor constructor = Arrays.stream(request.getConstructors()).findFirst()
                .orElseThrow(() -> new RuntimeException(request.getName() + " lacks a constructor"));
        Class[] types = constructor.getParameterTypes();
        Set<Class> unknownTypes = Arrays.stream(types).filter(e -> !objects.containsKey(e)).collect(Collectors.toSet());
        if (!unknownTypes.isEmpty()) {
            String unknownTypesFormatted = unknownTypes.stream().map(Class::getName).reduce((lhs, rhs) -> lhs + ", " + rhs).get();
            throw new RuntimeException("unknown types: " + unknownTypesFormatted);
        }
        Object[] params = Arrays.stream(types).map(recipes::get).map(Supplier::get).toArray();
        T res = (T) createFromParams(constructor, params);
        objects.put(request, res);
        return res;
    }

    public <T> T get(Class request) {
        return (T) recipes.get(request).get();
    }
}
