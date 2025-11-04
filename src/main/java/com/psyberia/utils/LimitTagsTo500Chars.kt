package com.psyberia.utils

import java.util.ArrayList

fun limitTagsTo500Chars(tags: List<String>): List<String> {
    val processedTags: MutableList<String> = ArrayList()
    val allTags = StringBuilder()

    for (tag in tags) {
        val cleanTag = tag.replace("#", "").trim { it <= ' ' }  // Убираем символы "#", пробелы
        if (allTags.length + cleanTag.length + 1 > 500) {
            // Если добавление следующего тега превышает лимит, прекращаем добавление
            break
        }
        allTags.append(cleanTag).append(" ") // Добавляем тег и пробел
        processedTags.add(cleanTag)
    }

    return processedTags
}