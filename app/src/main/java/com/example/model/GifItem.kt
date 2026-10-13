package com.example.model

enum class GifCategory(val displayName: String, val iconName: String) {
    ALL("Tümü", "apps"),
    TRENDING("Trendler", "fire"),
    REACTION("Reaksiyon", "mood"),
    GAMING("Gaming", "gamepad"),
    MEME("Meme & Komedi", "face"),
    ASCII("ASCII / Metin", "text"),
    FAVORITES("Favoriler", "star")
}

data class GifItem(
    val id: String,
    val title: String,
    val url: String,
    val category: GifCategory,
    val tags: List<String> = emptyList(),
    val asciiArt: String? = null,
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false,
    val author: String = "YouTube Topluluğu"
) {
    /**
     * Formats the GIF for YouTube comments:
     * YouTube comments auto-link URLs and display markdown-like quotes nicely.
     */
    fun toYtCommentFormat(): String {
        return if (!asciiArt.isNullOrBlank()) {
            asciiArt.trim()
        } else {
            "🎬 [GIF: $title]\n➜ $url"
        }
    }

    /**
     * Formats for YouTube Live Chat (compact, single or dual line):
     */
    fun toYtLiveChatFormat(): String {
        return if (!asciiArt.isNullOrBlank()) {
            asciiArt.replace("\n", " ").take(150)
        } else {
            "⚡ $title: $url"
        }
    }

    /**
     * Plain direct link
     */
    fun toDirectLink(): String {
        return url
    }
}
