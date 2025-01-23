package com.example.random_menu.Data;

import androidx.annotation.Nullable;

import java.util.Objects;

public class Element {
    public final Integer id;
    public String name;
    public String comment;
    public Integer priority;

    public Element(Integer id, String content,String comment,Integer priority) {
        this.id = id;
        this.name = content;
        this.comment = comment;
        this.priority = priority;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;  // Если ссылки одинаковые, то объекты одинаковы
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;  // Если объект null или классы разные, то объекты не равны
        }
        Element element = (Element) obj;  // Преобразуем объект в нужный тип
        return Objects.equals(id, element.id) &&     // Сравниваем идентификаторы
                Objects.equals(name, element.name) &&
                Objects.equals(comment,element.comment);  // Сравниваем имена
    }
    @Override
    public String toString() {
        return name;
    }
}
