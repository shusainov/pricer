package com.shusainov.tgbot.api.models;

import lombok.Data;

@Data
public class Message {
    private int message_id;    //Integer	Уникальный идентификатор сообщения
    //from	User	Опционально. Отправитель. Может быть пустым в каналах.
    private int date;       //Integer	Дата отправки сообщения (Unix time)
    private Chat chat;      //Chat	Диалог, в котором было отправлено сообщение
    //forward_from	User	Опционально. Для пересланных сообщений: отправитель оригинального сообщения
    //forward_date	Integer	Опционально. Для пересланных сообщений: дата отправки оригинального сообщения
    //reply_to_message	Message	Опционально. Для ответов: оригинальное сообщение. Note that the Message object in this field will not contain further reply_to_message fields even if it itself is a reply.
    private String text;    //String	Опционально. Для текстовых сообщений: текст сообщения, 0-4096 символов
    //entities	Массив из MessageEntity	Опционально. Для текстовых сообщений: особые сущности в тексте сообщения.
    //audio	Audio	Опционально. Информация об аудиофайле
    //document	Document	Опционально. Информация о файле
    //photo	Массив из PhotoSize	Опционально. Доступные размеры фото
    //sticker	Sticker	Опционально. Информация о стикере
    //video	Video	Опционально. Информация о видеозаписи
    //voice	Voice	Опционально. Информация о голосовом сообщении
    //caption	String	Опционально. Подпись к файлу, фото или видео, 0-200 символов
    //contact	Contact	Опционально. Информация об отправленном контакте
    //location	Location	Опционально. Информация о местоположении
    //venue	Venue	Опционально. Информация о месте на карте
    //new_chat_member	User	Опционально. Информация о пользователе, добавленном в группу
    //left_chat_member	User	Опционально. Информация о пользователе, удалённом из группы
    //new_chat_title	String	Опционально. Название группы было изменено на это поле
    //new_chat_photo	Массив из PhotoSize	Опционально. Фото группы было изменено на это поле
    //delete_chat_photo	True	Опционально. Сервисное сообщение: фото группы было удалено
    //group_chat_created	True	Опционально. Сервисное сообщение: группа создана
    //supergroup_chat_created	True	Опционально. Сервисное сообщение: супергруппа создана
    //channel_chat_created	True	Опционально. Сервисное сообщение: канал создан
    //migrate_to_chat_id	Integer	Опционально. Группа была преобразована в супергруппу с указанным идентификатором. Не превышает 1e13
    //migrate_from_chat_id	Integer	Опционально. Cупергруппа была создана из группы с указанным идентификатором. Не превышает 1e13
    //pinned_message	Message	Опционально. Указанное сообщение было прикреплено. Note that the Message object in this field will not contain further reply_to_message fields even if it is itself a reply.
}
