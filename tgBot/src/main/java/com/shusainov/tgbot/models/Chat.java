package com.shusainov.tgbot.models;

import lombok.Data;

@Data
public class Chat {
    long id;     //Integer	Уникальный идентификатор чата. Абсолютное значение не превышает 1e13
    String type;//Enum	Тип чата: “private”, “group”, “supergroup” или “channel”
    //title	String	Опционально. Название, для каналов или групп
    //username	String	Опционально. Username, для чатов и некоторых каналов
    //first_name	String	Опционально. Имя собеседника в чате
    //last_name	String	Опционально. Фамилия собеседника в чате
    //all_members_are_administrators	Boolean	Опционально.True, если все участники чата являются администраторами
}
