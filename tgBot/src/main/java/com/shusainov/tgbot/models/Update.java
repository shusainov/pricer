package com.shusainov.tgbot.models;

import lombok.Data;

@Data
public class Update {
    private final int update_id;  //Integer	The update‘s unique identifier. Update identifiers start from a certain positive number and increase sequentially. This ID becomes especially handy if you’re using Webhooks, since it allows you to ignore repeated updates or to restore the correct update sequence, should they get out of order.
    private Message message;//	Message	Опционально. New incoming message of any kind — text, photo, sticker, etc.
    //inline_query	InlineQuery	Опционально. New incoming inline query
    //chosen_inline_result	ChosenInlineResult	Опционально. The result of an inline query that was chosen by a user and sent to their chat partner.
    //callback_query	CallbackQuery	Опционально. New incoming callback query

}
