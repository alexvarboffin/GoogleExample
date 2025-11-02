package com.psyberia.outh2

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.regex.Pattern

object TextUtilz {
    fun selectAndShuffleTags(tags: List<String>, maxLength: Int): String {
        Collections.shuffle(tags)
        val selectedTags = StringBuilder()
        for (tag in tags) {
            if (selectedTags.length + tag.length + 1 > maxLength) {
                break
            }
            if (selectedTags.length > 0) {
                selectedTags.append(" ")
            }
            selectedTags.append(tag)
        }
        return selectedTags.toString()
    }

    fun extractTextBetween(input: String): String {
        val regex = "\\d+_(.*?)\\.mp4"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(input)
        return if (matcher.find()) {
            matcher.group(1)
        } else {
            input
        }
    }

    @Throws(IOException::class)
    fun readAllLines(filePath: String): List<String> {
        // Чтение всех строк из файла с помощью метода Files.readAllLines
        return Files.readAllLines(Paths.get(filePath))
    }

    fun divideString(input: String, lines: Int): List<String> {
        val dividedStrings: MutableList<String> = ArrayList()
        val wordsPerLine = input.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray().size / lines // ������� ��������� ���������� ���� �� ������ ������
        val words = input.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var currentLine = StringBuilder()
        var wordCount = 0
        for (word in words) {
            if (wordCount == wordsPerLine) {
                dividedStrings.add(currentLine.toString())
                currentLine = StringBuilder()
                wordCount = 0
            }
            currentLine.append(word).append(" ")
            wordCount++
        }
        dividedStrings.add(currentLine.toString()) // ��������� ���������� �����
        return dividedStrings
    }
}
