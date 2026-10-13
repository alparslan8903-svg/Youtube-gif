package com.example.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PictureInPictureAlt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GifCategory
import com.example.model.GifItem
import com.example.ui.components.AddCustomGifDialog
import com.example.ui.components.AsciiCard
import com.example.ui.components.FloatingMiniWindow
import com.example.ui.components.GifCard
import com.example.ui.components.PipModeContent
import com.example.ui.components.YtCommentPreviewDialog
import com.example.ui.theme.YtBlue
import com.example.ui.theme.YtDarkBackground
import com.example.ui.theme.YtDarkCard
import com.example.ui.theme.YtGrayText
import com.example.ui.theme.YtGreen
import com.example.ui.theme.YtRed
import com.example.viewmodel.GifViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGifScreen(
    viewModel: GifViewModel,
    onEnterSystemPip: () -> Unit,
    onLaunchYouTubeApp: () -> Unit,
    onCopyToClipboard: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredGifs by viewModel.filteredGifs.collectAsState()
    val selectedGifForPreview by viewModel.selectedGifForPreview.collectAsState()
    val lastCopiedGif by viewModel.lastCopiedGif.collectAsState()
    val isFloatingOverlayActive by viewModel.isFloatingOverlayActive.collectAsState()
    val isSystemPiPActive by viewModel.isSystemPiPActive.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    // If System PiP is active, show the ultra-compact PiP layout
    if (isSystemPiPActive) {
        PipModeContent(
            gifs = listOf(filteredGifs),
            onCopyGif = { gif ->
                val text = gif.toYtCommentFormat()
                onCopyToClipboard(text, gif.title)
                viewModel.setLastCopied(gif)
            }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(YtRed, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "YouTube GIF Asistanı",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Pencere modunda hızlı paylaşım",
                                color = YtGrayText,
                                fontSize = 11.sp
                            )
                        }
                    }
                },
                actions = {
                    // Floating in-app window toggle
                    IconButton(
                        onClick = { viewModel.toggleFloatingOverlay() },
                        modifier = Modifier.testTag("toggle_floating_window_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Widgets,
                            contentDescription = "Pencere Paneli",
                            tint = if (isFloatingOverlayActive) YtRed else Color.LightGray
                        )
                    }

                    // System Picture-in-Picture trigger
                    IconButton(
                        onClick = onEnterSystemPip,
                        modifier = Modifier.testTag("enter_pip_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureInPictureAlt,
                            contentDescription = "Pencere Moduna Geç (PiP)",
                            tint = YtRed
                        )
                    }

                    // Add custom GIF
                    IconButton(
                        onClick = { showAddDialog = true },
                        modifier = Modifier.testTag("add_custom_gif_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Özel GIF Ekle",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = YtDarkBackground
                )
            )
        },
        containerColor = YtDarkBackground,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp)
            ) {
                // Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("GIF, meme veya ASCII tepkisi ara…", color = Color.Gray, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Ara",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Temizle",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E1E1E),
                        unfocusedContainerColor = Color(0xFF181818),
                        focusedBorderColor = YtRed,
                        unfocusedBorderColor = Color(0xFF2C2C2C),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 10.dp)
                        .testTag("search_text_input")
                )

                // Category Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    items(GifCategory.values(), key = { it.name }) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { viewModel.selectCategory(category) },
                            label = {
                                Text(
                                    text = category.displayName,
                                    fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = YtRed,
                                selectedLabelColor = Color.White,
                                containerColor = Color(0xFF202020),
                                labelColor = Color.LightGray
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("category_chip_${category.name}")
                        )
                    }
                }

                // Grid or Empty State
                if (filteredGifs.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "Hiç GIF bulunamadı 🔍",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Farklı bir arama terimi deneyin veya kendi GIF bağlantınızı ekleyin.",
                                color = YtGrayText,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showAddDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = YtRed),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Özel GIF Ekle", color = Color.White)
                            }
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 90.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(filteredGifs, key = { it.id }) { item ->
                            if (item.category == GifCategory.ASCII) {
                                AsciiCard(
                                    item = item,
                                    onClick = { viewModel.selectGifForPreview(item) },
                                    onCopyClick = {
                                        val text = item.toYtCommentFormat()
                                        onCopyToClipboard(text, item.title)
                                        viewModel.setLastCopied(item)
                                    },
                                    onFavoriteToggle = { viewModel.toggleFavorite(item.id) }
                                )
                            } else {
                                GifCard(
                                    gif = item,
                                    onClick = { viewModel.selectGifForPreview(item) },
                                    onCopyClick = {
                                        val text = item.toYtCommentFormat()
                                        onCopyToClipboard(text, item.title)
                                        viewModel.setLastCopied(item)
                                    },
                                    onFavoriteToggle = { viewModel.toggleFavorite(item.id) },
                                    onDeleteCustom = if (item.isCustom) {
                                        { viewModel.deleteCustomGif(item.id) }
                                    } else null
                                )
                            }
                        }
                    }
                }
            }

            // Floating Bottom Bar when user copies a GIF
            AnimatedVisibility(
                visible = lastCopiedGif != null,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(14.dp)
            ) {
                lastCopiedGif?.let { copiedItem ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(YtGreen.copy(alpha = 0.2f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = YtGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Kopyalandı: ${copiedItem.title}",
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "YouTube yorumuna yapıştırılmaya hazır!",
                                        color = YtGrayText,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Button(
                                onClick = onLaunchYouTubeApp,
                                colors = ButtonDefaults.buttonColors(containerColor = YtRed),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("launch_yt_after_copy_btn")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.OpenInNew,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("YouTube'a Git", fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            // Draggable In-App Mini Window (Pencere Modu Overlay)
            FloatingMiniWindow(
                isVisible = isFloatingOverlayActive,
                recentGifs = filteredGifs.take(8),
                onClose = { viewModel.toggleFloatingOverlay() },
                onExpand = { viewModel.toggleFloatingOverlay() },
                onTriggerSystemPip = onEnterSystemPip,
                onCopyGif = { item ->
                    val text = item.toYtCommentFormat()
                    onCopyToClipboard(text, item.title)
                    viewModel.setLastCopied(item)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 12.dp)
            )

            // Preview & Share Dialog
            selectedGifForPreview?.let { gif ->
                YtCommentPreviewDialog(
                    gif = gif,
                    onDismiss = { viewModel.selectGifForPreview(null) },
                    onCopyAndLaunch = { formattedText ->
                        onCopyToClipboard(formattedText, gif.title)
                        viewModel.setLastCopied(gif)
                        viewModel.selectGifForPreview(null)
                        onLaunchYouTubeApp()
                    },
                    onCopyOnly = { formattedText ->
                        onCopyToClipboard(formattedText, gif.title)
                        viewModel.setLastCopied(gif)
                        viewModel.selectGifForPreview(null)
                    },
                    onFavoriteToggle = {
                        viewModel.toggleFavorite(gif.id)
                    }
                )
            }

            // Add Custom GIF Dialog
            if (showAddDialog) {
                AddCustomGifDialog(
                    onDismiss = { showAddDialog = false },
                    onAdd = { title, url, category ->
                        viewModel.addCustomGif(title, url, category)
                        Toast.makeText(context, "Özel GIF eklendi!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
