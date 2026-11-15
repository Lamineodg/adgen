package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.GeneratedPhotoAd
import com.example.data.VideoAdCampaign
import com.example.data.sharePhotoAd
import com.example.ui.AdViewModel
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BadgeRed
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCard
import com.example.ui.theme.StudioCardElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SavedGalleryScreen(
    viewModel: AdViewModel,
    onNavigateToPhoto: () -> Unit,
    onNavigateToVideo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val savedPhotos by viewModel.savedPhotos.collectAsStateWithLifecycle()
    val savedCampaigns by viewModel.savedCampaigns.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StudioCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(AmberGold.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = null,
                            tint = AmberGold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Portfolio & Créations Publicitaires",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Mémoire locale • ${savedPhotos.size} photos • ${savedCampaigns.size} vidéos",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = StudioCard,
            contentColor = AmberGold,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = AmberGold
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        "Photos Publicitaires (${savedPhotos.size})",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        "Campagnes Vidéos (${savedCampaigns.size})",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }

        if (selectedTab == 0) {
            if (savedPhotos.isEmpty()) {
                EmptyGalleryCard(
                    icon = Icons.Default.PhotoCamera,
                    title = "Aucune photo publicitaire créée",
                    message = "Générez votre premier visuel publicitaire pro en tapant une description dans le Studio Photo.",
                    actionLabel = "Créer une Photo Pub",
                    onAction = onNavigateToPhoto
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    items(savedPhotos, key = { it.id }) { photo ->
                        SavedPhotoItem(
                            photoAd = photo,
                            onOpen = {
                                viewModel.loadPhotoIntoStudio(photo)
                                onNavigateToPhoto()
                            },
                            onDelete = { viewModel.removeSavedPhoto(photo.id) },
                            onCopyPrompt = {
                                clipboardManager.setText(AnnotatedString(photo.enhancedPrompt))
                                Toast.makeText(context, "Prompt copié !", Toast.LENGTH_SHORT).show()
                            },
                            onShare = {
                                sharePhotoAd(context, photo)
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        } else {
            if (savedCampaigns.isEmpty()) {
                EmptyGalleryCard(
                    icon = Icons.Default.Movie,
                    title = "Aucune campagne vidéo réalisée",
                    message = "Créez votre première vidéo pub complète avec storyboard, plans cinéma et voix-off.",
                    actionLabel = "Créer une Vidéo Pub",
                    onAction = onNavigateToVideo
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    items(savedCampaigns, key = { it.id }) { camp ->
                        SavedCampaignItem(
                            campaign = camp,
                            onPlay = {
                                viewModel.loadCampaignIntoStudio(camp)
                                onNavigateToVideo()
                            },
                            onDelete = { viewModel.removeSavedCampaign(camp.id) },
                            onCopyScript = {
                                val scriptText = buildString {
                                    appendLine("=== Campagne : ${camp.title} ===")
                                    appendLine("Marque : ${camp.brandName}")
                                    appendLine("Accroche : ${camp.hook}")
                                    appendLine("CTA : ${camp.ctaText}")
                                    appendLine()
                                    camp.scenes.forEach { s ->
                                        appendLine("--- Plan ${s.sceneNumber} (${s.timeCode}) ---")
                                        appendLine("Caméra : ${s.cameraMovement}")
                                        appendLine("Visuel : ${s.visualDescription}")
                                        appendLine("Voix-off : ${s.voiceover}")
                                        appendLine("Prompt IA : ${s.aiVideoPrompt}")
                                        appendLine()
                                    }
                                }
                                clipboardManager.setText(AnnotatedString(scriptText))
                                Toast.makeText(context, "Script complet copié !", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
fun SavedPhotoItem(
    photoAd: GeneratedPhotoAd,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
    onCopyPrompt: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = StudioCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
            ) {
                Image(
                    bitmap = photoAd.bitmap.asImageBitmap(),
                    contentDescription = photoAd.prompt,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (photoAd.badgeText.isNotBlank()) {
                    Surface(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopStart),
                        color = BadgeRed,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = photoAd.badgeText,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = photoAd.prompt,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${photoAd.category.displayName} • ${photoAd.ratio.displayName}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = onOpen,
                        modifier = Modifier.testTag("open_saved_photo_button"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberGold),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AmberGold.copy(alpha = 0.6f))
                    ) {
                        Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ouvrir", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = onCopyPrompt,
                        modifier = Modifier.testTag("copy_saved_photo_button"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberGold),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copier", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = onShare,
                        modifier = Modifier.testTag("share_saved_photo_button"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberGold),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Partager", fontSize = 11.sp)
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.testTag("delete_saved_photo_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Supprimer",
                        tint = BadgeRed
                    )
                }
            }
        }
    }
}

@Composable
fun SavedCampaignItem(
    campaign: VideoAdCampaign,
    onPlay: () -> Unit,
    onDelete: () -> Unit,
    onCopyScript: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = StudioCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = campaign.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = ElectricViolet,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Supprimer", tint = BadgeRed)
                }
            }

            Text(
                text = "Accroche : \"${campaign.hook}\"",
                style = MaterialTheme.typography.bodySmall,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${campaign.scenes.size} Plans • Durée : ${campaign.totalDurationSeconds}s • CTA : ${campaign.ctaText}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPlay,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("play_saved_campaign_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet, contentColor = Color.White)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Lire", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onCopyScript,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("copy_saved_campaign_button"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricViolet),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copier Script", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EmptyGalleryCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    message: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = StudioCard.copy(alpha = 0.6f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onAction,
                modifier = Modifier.testTag("empty_gallery_action_button"),
                colors = ButtonDefaults.buttonColors(containerColor = AmberGold, contentColor = Color.Black)
            ) {
                Text(actionLabel, fontWeight = FontWeight.Bold)
            }
        }
    }
}
