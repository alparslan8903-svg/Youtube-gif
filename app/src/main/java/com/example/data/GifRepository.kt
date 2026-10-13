package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.GifCategory
import com.example.model.GifItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class GifRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("yt_gif_prefs", Context.MODE_PRIVATE)

    private val defaultGifs = listOf(
        // Trending
        GifItem(
            id = "trend_1",
            title = "Kedi Dansı (Vibing Cat)",
            url = "https://media.giphy.com/media/jpbnoe3UIa8TU8LM13/giphy.gif",
            category = GifCategory.TRENDING,
            tags = listOf("kedi", "dans", "müzik", "vibing", "komik", "cat")
        ),
        GifItem(
            id = "trend_2",
            title = "Mısır Yiyen Adam (Popcorn Drama)",
            url = "https://media.giphy.com/media/gl0mkIZOW6Nwc/giphy.gif",
            category = GifCategory.TRENDING,
            tags = listOf("mısır", "popcorn", "drama", "yorumlar", "izliyorum", "kavga")
        ),
        GifItem(
            id = "trend_3",
            title = "Şaşkın Kedi (Oha)",
            url = "https://media.giphy.com/media/mlvseq9yvZhba/giphy.gif",
            category = GifCategory.TRENDING,
            tags = listOf("şok", "kedi", "şaşkın", "nasılyani", "oha")
        ),
        GifItem(
            id = "trend_4",
            title = "Alkışlayan Leonardo DiCaprio",
            url = "https://media.giphy.com/media/g9582DNuQppxC/giphy.gif",
            category = GifCategory.TRENDING,
            tags = listOf("alkış", "tebrik", "gatsby", "leonardo", "kadeh", "bravo")
        ),

        // Reactions
        GifItem(
            id = "react_1",
            title = "Gülmekten Kırılma (Laughing)",
            url = "https://media.giphy.com/media/26n6Gx9moCgs1QXbW/giphy.gif",
            category = GifCategory.REACTION,
            tags = listOf("gülme", "kahkaha", "komik", "ahah", "lol", "lmao")
        ),
        GifItem(
            id = "react_2",
            title = "Facepalm (Yok Artık)",
            url = "https://media.giphy.com/media/XsUtdIeJ0MWMo/giphy.gif",
            category = GifCategory.REACTION,
            tags = listOf("facepalm", "utanç", "yokartık", "rezil", "of")
        ),
        GifItem(
            id = "react_3",
            title = "Ağlayan Sevimli Kedi",
            url = "https://media.giphy.com/media/L95W4wvxxB1G8/giphy.gif",
            category = GifCategory.REACTION,
            tags = listOf("ağlama", "hüzün", "duygusal", "sad", "üzgün")
        ),
        GifItem(
            id = "react_4",
            title = "Akıl Almaz Zihin Patlaması (Mind Blown)",
            url = "https://media.giphy.com/media/26ufdipQqU2lhNA4g/giphy.gif",
            category = GifCategory.REACTION,
            tags = listOf("mindblown", "beyinyandı", "şok", "patlama", "inanılmaz")
        ),
        GifItem(
            id = "react_5",
            title = "Evet Onaylıyorum (Nodding Head)",
            url = "https://media.giphy.com/media/10Jpr9KSaXLchW/giphy.gif",
            category = GifCategory.REACTION,
            tags = listOf("evet", "jacknicholson", "onay", "katılıyorum", "haklı")
        ),

        // Gaming
        GifItem(
            id = "game_1",
            title = "GG (Good Game)",
            url = "https://media.giphy.com/media/artj92V8o75VPL7AeQ/giphy.gif",
            category = GifCategory.GAMING,
            tags = listOf("gg", "oyun", "goodgame", "zafer", "tebrik")
        ),
        GifItem(
            id = "game_2",
            title = "Klavye Parçalayan Gamer (Rage Quit)",
            url = "https://media.giphy.com/media/xT0xeJpnrWC4XWblEk/giphy.gif",
            category = GifCategory.GAMING,
            tags = listOf("rage", "sinir", "klavye", "çıldırdı", "gamer")
        ),
        GifItem(
            id = "game_3",
            title = "Zafer Dansı (Fortnite / Gaming Win)",
            url = "https://media.giphy.com/media/blSTtZehjAZ8I/giphy.gif",
            category = GifCategory.GAMING,
            tags = listOf("zafer", "dans", "win", "kazandık", "ez")
        ),
        GifItem(
            id = "game_4",
            title = "Pro Gamer Odaklanma",
            url = "https://media.giphy.com/media/unQ3IJU2RG7DO/giphy.gif",
            category = GifCategory.GAMING,
            tags = listOf("pro", "tryhard", "gamer", "hız", "odak")
        ),

        // Memes
        GifItem(
            id = "meme_1",
            title = "Rickroll (Never Gonna Give You Up)",
            url = "https://media.giphy.com/media/Vuw9m5wXviFIQ/giphy.gif",
            category = GifCategory.MEME,
            tags = listOf("rickroll", "rickastley", "troll", "meme", "şaka")
        ),
        GifItem(
            id = "meme_2",
            title = "Gigachad (Sigma Meme)",
            url = "https://media.giphy.com/media/CAYVZA5NRb529kKQUc/giphy.gif",
            category = GifCategory.MEME,
            tags = listOf("gigachad", "chad", "sigma", "kral", "efsane")
        ),
        GifItem(
            id = "meme_3",
            title = "This is Fine (Her Şey Yolunda)",
            url = "https://media.giphy.com/media/NTur7XlVDUdqM/giphy.gif",
            category = GifCategory.MEME,
            tags = listOf("thisisfine", "yangın", "köpek", "sakin", "idareeder")
        ),
        GifItem(
            id = "meme_4",
            title = "Confused Travolta (Neredeyim Ben)",
            url = "https://media.giphy.com/media/g01ZnwAUvutuK8GIQn/giphy.gif",
            category = GifCategory.MEME,
            tags = listOf("travolta", "şaşkın", "kayıp", "anlamadım", "nerede")
        ),

        // ASCII & Text Memes for direct YouTube comments
        GifItem(
            id = "ascii_1",
            title = "Mısır Yiyen Kedi (ASCII Sanatı)",
            url = "https://media.giphy.com/media/gl0mkIZOW6Nwc/giphy.gif",
            category = GifCategory.ASCII,
            tags = listOf("ascii", "yorum", "mısır", "metin", "kedi"),
            asciiArt = """
( •_•)
( •_•)>⌐■-■
(⌐■_■) 🍿 Yorumları okumaya geldim...
            """.trimIndent()
        ),
        GifItem(
            id = "ascii_2",
            title = "Dans Eden Tavşan (Canlı Sohbet)",
            url = "https://media.giphy.com/media/jpbnoe3UIa8TU8LM13/giphy.gif",
            category = GifCategory.ASCII,
            tags = listOf("ascii", "tavşan", "dans", "parti"),
            asciiArt = """
(\_/)
( •_•)
/ >🎵 Kopuyoruz!
            """.trimIndent()
        ),
        GifItem(
            id = "ascii_3",
            title = "Hype Treni / Alevler (YouTube Chat)",
            url = "https://media.giphy.com/media/artj92V8o75VPL7AeQ/giphy.gif",
            category = GifCategory.ASCII,
            tags = listOf("ascii", "hype", "canlı", "sohbet", "alev"),
            asciiArt = """
🔥🚂 CANLI YAYIN HYPE TRENI 🚂🔥
[░░░░░░░░░░░░░░░░░░░░░] 100%
🚀 HERKES EKRANA ÇİFT TIKLASIN! 💥
            """.trimIndent()
        ),
        GifItem(
            id = "ascii_4",
            title = "Alkışlayan Ayıcık (Kutlama)",
            url = "https://media.giphy.com/media/g9582DNuQppxC/giphy.gif",
            category = GifCategory.ASCII,
            tags = listOf("ascii", "alkış", "tebrik", "ayıcık"),
            asciiArt = """
ʕ•ᴥ•ʔ 👏 Harika video!
(つ✧ω✧)つ Eline sağlık reis!
            """.trimIndent()
        ),
        GifItem(
            id = "ascii_5",
            title = "Rage Gamer Çığlığı",
            url = "https://media.giphy.com/media/xT0xeJpnrWC4XWblEk/giphy.gif",
            category = GifCategory.ASCII,
            tags = listOf("ascii", "rage", "çığlık", "lol"),
            asciiArt = """
(╯°□°)╯︵ ┻━┻
NASIL ÖLDÜM BEN ORADA?!
┬─┬ノ( º _ ºノ) Tamam sakinim...
            """.trimIndent()
        )
    )

    private val _gifs = MutableStateFlow<List<GifItem>>(emptyList())
    val gifs: StateFlow<List<GifItem>> = _gifs.asStateFlow()

    init {
        loadGifs()
    }

    private fun loadGifs() {
        val favoriteIds = prefs.getStringSet("favorite_ids", emptySet()) ?: emptySet()
        val customGifsJson = prefs.getString("custom_gifs", "[]") ?: "[]"
        val customList = mutableListOf<GifItem>()

        try {
            val jsonArray = JSONArray(customGifsJson)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                customList.add(
                    GifItem(
                        id = obj.getString("id"),
                        title = obj.getString("title"),
                        url = obj.getString("url"),
                        category = try {
                            GifCategory.valueOf(obj.getString("category"))
                        } catch (e: Exception) {
                            GifCategory.TRENDING
                        },
                        tags = listOf("özel", "custom"),
                        isFavorite = favoriteIds.contains(obj.getString("id")),
                        isCustom = true
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val allGifs = (defaultGifs.map { item ->
            item.copy(isFavorite = favoriteIds.contains(item.id))
        } + customList).distinctBy { it.id }

        _gifs.value = allGifs
    }

    fun toggleFavorite(gifId: String) {
        val currentFavs = prefs.getStringSet("favorite_ids", emptySet())?.toMutableSet() ?: mutableSetOf()
        if (currentFavs.contains(gifId)) {
            currentFavs.remove(gifId)
        } else {
            currentFavs.add(gifId)
        }
        prefs.edit().putStringSet("favorite_ids", currentFavs).apply()

        _gifs.value = _gifs.value.map { item ->
            if (item.id == gifId) item.copy(isFavorite = currentFavs.contains(gifId)) else item
        }
    }

    fun addCustomGif(title: String, url: String, category: GifCategory = GifCategory.TRENDING) {
        val newId = "custom_${System.currentTimeMillis()}"
        val newGif = GifItem(
            id = newId,
            title = title.ifBlank { "Özel GIF" },
            url = url.trim(),
            category = category,
            tags = listOf("özel", "kullanıcı"),
            isCustom = true
        )

        val customGifsJson = prefs.getString("custom_gifs", "[]") ?: "[]"
        val jsonArray = try {
            JSONArray(customGifsJson)
        } catch (e: Exception) {
            JSONArray()
        }

        val obj = JSONObject().apply {
            put("id", newGif.id)
            put("title", newGif.title)
            put("url", newGif.url)
            put("category", newGif.category.name)
        }
        jsonArray.put(obj)
        prefs.edit().putString("custom_gifs", jsonArray.toString()).apply()

        _gifs.value = _gifs.value + newGif
    }

    fun deleteCustomGif(gifId: String) {
        val customGifsJson = prefs.getString("custom_gifs", "[]") ?: "[]"
        try {
            val jsonArray = JSONArray(customGifsJson)
            val newArray = JSONArray()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                if (obj.getString("id") != gifId) {
                    newArray.put(obj)
                }
            }
            prefs.edit().putString("custom_gifs", newArray.toString()).apply()
            _gifs.value = _gifs.value.filterNot { it.id == gifId }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
