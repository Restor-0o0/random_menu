package com.example.random_menu.Data;

import androidx.annotation.Nullable;

import java.util.Objects;

public class Group {
    public final Integer id;
    public String name;
    public String comment;
    public Integer priority;
    public Integer countElems;

    public Group(Integer id, String content, String comment, Integer priority,Integer countElems) {
        this.id = id;
        this.name = content;
        this.comment = comment;
        this.priority = priority;
        this.countElems = countElems;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;  // Если ссылки одинаковые, то объекты одинаковы
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;  // Если объект null или классы разные, то объекты не равны
        }
        Group group = (Group) obj;  // Преобразуем объект в нужный тип
        return Objects.equals(id, group.id) &&     // Сравниваем идентификаторы
                Objects.equals(name, group.name) &&
                Objects.equals(comment,group.comment);  // Сравниваем имена
    }

    @Override
    public String toString() {
        return name;
    }
}
