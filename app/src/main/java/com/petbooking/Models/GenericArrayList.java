package com.petbooking.Models;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Luciano José on 27/02/2017.
 */

public class GenericArrayList<T> implements ParameterizedType {

    private Class<?> type;

    public GenericArrayList(Class<T> type) {
        this.type = type;
    }

    public Type[] getActualTypeArguments() {
        return new Type[] {type};
    }

    public Type getRawType() {
        return ArrayList.class;
    }

    public Type getOwnerType() {
        return null;
    }
}
