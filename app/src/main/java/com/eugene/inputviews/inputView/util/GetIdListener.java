package com.eugene.inputviews.inputView.util;

/**
 * Получить id по модели
 * @param <T>
 */
public interface GetIdListener<T> {
    String getId(T item);
}

