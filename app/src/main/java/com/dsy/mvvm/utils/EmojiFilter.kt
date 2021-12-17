package com.dsy.mvvm.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class EmojiFilter : InputFilter {
    private val emoji = Pattern.compile(
        "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\ud83e\udd00-\ud83e\udfff]|[\u2600-\u27ff]",
        Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE
    )

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence {
        val emojiMatcher = emoji.matcher(source)
        return if (emojiMatcher.find()) {
            ""
        } else source
    }
}