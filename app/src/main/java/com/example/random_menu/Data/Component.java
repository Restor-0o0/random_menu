package com.example.random_menu.Data;

import androidx.annotation.Nullable;

import java.util.Objects;

public class Component {
    public final Integer id;
    public String name;
    public String comment;
    public String quantity;

    public Component(Integer id, String name, String comment, String quantity) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;  // Если ссылки одинаковые, то объекты одинаковы
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;  // Если объект null или классы разные, то объекты не равны
        }
        Component component = (Component) obj;  // Преобразуем объект в нужный тип
        return Objects.equals(id, component.id) &&     // Сравниваем идентификаторы
                Objects.equals(name, component.name) &&
                Objects.equals(comment,component.comment);  // Сравниваем имена
    }
    @Override
    public String toString() {
        return name;
    }
}
