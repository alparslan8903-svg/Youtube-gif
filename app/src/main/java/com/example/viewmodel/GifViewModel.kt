package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GifRepository
import com.example.model.GifCategory
import com.example.model.GifItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class CopyFormat(val title: String, val description: String) {
    YT_COMMENT("YouTube Yorum Formatı", "Karakter başlığı ve doğrudan oynatılabilir bağlantı"),
    LIVE_CHAT("Canlı Sohbet Formatı", "Canlı yayında spam yemeyen hızlı ve kompakt format"),
    DIRECT_LINK("Yalnızca GIF Linki", "Ham GIF/Medya bağlantısı")
}

class GifViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GifRepository(application)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(GifCategory.ALL)
    val selectedCategory: StateFlow<GifCategory> = _selectedCategory.asStateFlow()

    private val _selectedGifForPreview = MutableStateFlow<GifItem?>(null)
    val selectedGifForPreview: StateFlow<GifItem?> = _selectedGifForPreview.asStateFlow()

    private val _lastCopiedGif = MutableStateFlow<GifItem?>(null)
    val lastCopiedGif: StateFlow<GifItem?> = _lastCopiedGif.asStateFlow()

    private val _isFloatingOverlayActive = MutableStateFlow(false)
    val isFloatingOverlayActive: StateFlow<Boolean> = _isFloatingOverlayActive.asStateFlow()

    private val _isSystemPiPActive = MutableStateFlow(false)
    val isSystemPiPActive: StateFlow<Boolean> = _isSystemPiPActive.asStateFlow()

    private val _selectedCopyFormat = MutableStateFlow(CopyFormat.YT_COMMENT)
    val selectedCopyFormat: StateFlow<CopyFormat> = _selectedCopyFormat.asStateFlow()

    val filteredGifs: StateFlow<List<GifItem>> = combine(
        repository.gifs,
        _searchQuery,
        _selectedCategory
    ) { gifs, query, category ->
        var result = gifs

        if (category == GifCategory.FAVORITES) {
            result = result.filter { it.isFavorite }
        } else if (category != GifCategory.ALL) {
            result = result.filter { it.category == category }
        }

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            result = result.filter { item ->
                item.title.lowercase().contains(q) ||
                        item.tags.any { it.lowercase().contains(q) } ||
                        (item.asciiArt != null && item.asciiArt.lowercase().contains(q))
            }
        }

        result
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: GifCategory) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(gifId: String) {
        repository.toggleFavorite(gifId)
    }

    fun addCustomGif(title: String, url: String, category: GifCategory = GifCategory.TRENDING) {
        repository.addCustomGif(title, url, category)
    }

    fun deleteCustomGif(gifId: String) {
        repository.deleteCustomGif(gifId)
    }

    fun selectGifForPreview(gif: GifItem?) {
        _selectedGifForPreview.value = gif
    }

    fun setLastCopied(gif: GifItem) {
        _lastCopiedGif.value = gif
    }

    fun toggleFloatingOverlay() {
        _isFloatingOverlayActive.value = !_isFloatingOverlayActive.value
    }

    fun setSystemPiPActive(active: Boolean) {
        _isSystemPiPActive.value = active
    }

    fun setCopyFormat(format: CopyFormat) {
        _selectedCopyFormat.value = format
    }
}
