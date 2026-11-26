package com.gorilla.gallery.ui.components

import androidx.compose.runtime.setValue

import androidx.compose.runtime.getValue

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gorilla.gallery.GalleryApp
import com.gorilla.gallery.data.model.MediaItem
import com.gorilla.gallery.ui.theme.pressScale

private val CellShape = RoundedCornerShape(0.dp)

/** A single timeline/album thumbnail: photo or video first-frame, with badges + selection. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaCell(
    item: MediaItem,
    thumbnailSizePx: Int,
    onClick: (Rect) -> Unit,
    onLongClick: () -> Unit,
    selectionMode: Boolean,
    selected: Boolean,
    highQualityThumbnails: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val app = context.applicationContext as GalleryApp
    val interaction = remember { MutableInteractionSource() }
    val scale = if (selectionMode && selected) 0.86f else 1f
    val coordsRef = remember { arrayOfNulls<androidx.compose.ui.layout.LayoutCoordinates>(1) }

    DisposableEffect(item.uri) {
        onDispose {
            app.itemBounds.remove(item.uri)
        }
    }

    Box(
        modifier
            .aspectRatio(1f)
            .clip(CellShape)
            .background(Color(0xFF141414))
            .onGloballyPositioned { 
                coordsRef[0] = it
                app.itemBounds[item.uri] = it.boundsInWindow()
            }
            .combinedClickable(
                interactionSource = interaction,
                indication = null,
                onClick = { 
                    val bounds = coordsRef[0]?.boundsInWindow() ?: Rect.Zero
                    onClick(bounds) 
                },
                onLongClick = onLongClick,
            ),
    ) {
        val model = remember(item.uri, item.dateModifiedSec, thumbnailSizePx) {
            coil.request.ImageRequest.Builder(context)
                .data(item.uri)
                .size(thumbnailSizePx)
                .crossfade(false)
                .memoryCacheKey("${item.id}_${item.dateModifiedSec}_$thumbnailSizePx")
                .diskCacheKey("${item.id}_${item.dateModifiedSec}")
                .build()
        }

        AsyncImage(
            model = model,
            contentDescription = item.displayName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .scale(scale)
                .clip(CellShape),
        )

        // Video affordances: play glyph + duration pill.
        if (item.isVideo) {
            Icon(
                Icons.Rounded.PlayArrow,
                contentDescription = "Video",
                tint = Color.White,
                modifier = Modifier.align(Alignment.Center).size(28.dp),
            )
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.65f))
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(
                    formatDuration(item.durationMs),
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        // Selection checkbox.
        AnimatedVisibility(
            visible = selectionMode,
            enter = fadeIn(animationSpec = spring(dampingRatio = 0.7f, stiffness = 380f)) + scaleIn(animationSpec = spring(dampingRatio = 0.7f, stiffness = 380f)),
            exit = fadeOut(animationSpec = spring(dampingRatio = 0.82f, stiffness = 380f)) + scaleOut(animationSpec = spring(dampingRatio = 0.82f, stiffness = 380f)),
            modifier = Modifier.align(Alignment.TopEnd).padding(5.dp),
        ) {
            Icon(
                if (selected) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                contentDescription = if (selected) "Selected" else "Not selected",
                tint = if (selected) com.gorilla.gallery.ui.theme.LocalDynamicColors.current.accent else Color.White,
                modifier = Modifier.size(22.dp),
            )
        }

        if (selected) {
            Box(Modifier.fillMaxSize().clip(CellShape).background(Color.Black.copy(alpha = 0.25f)))
        }
    }
}

