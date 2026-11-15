package com.example.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.example.data.AdBadgePosition
import com.example.data.AdBadgeStyle
import com.example.data.AdCategory
import com.example.data.AdLighting
import com.example.data.AdRatio
import com.example.data.GeneratedPhotoAd
import com.example.data.sharePhotoAd
import com.example.ui.AdViewModel
import com.example.ui.PhotoGenState
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BadgeGold
import com.example.ui.theme.BadgePurple
import com.example.ui.theme.BadgeRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCard
import com.example.ui.theme.StudioCardElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhotoAdScreen(
    viewModel: AdViewModel,
    modifier: Modifier = Modifier
) {
    val prompt by viewModel.photoPrompt.collectAsStateWithLifecycle()
    val category by viewModel.photoCategory.collectAsStateWithLifecycle()
    val lighting by viewModel.photoLighting.collectAsStateWithLifecycle()
    val ratio by viewModel.photoRatio.collectAsStateWithLifecycle()
    val brandName by viewModel.brandName.collectAsStateWithLifecycle()
    val badgeText by viewModel.badgeText.collectAsStateWithLifecycle()
    val badgeStyle by viewModel.badgeStyle.collectAsStateWithLifecycle()
    val badgePosition by viewModel.badgePosition.collectAsStateWithLifecycle()
    val headline by viewModel.headline.collectAsStateWithLifecycle()
    val sloganSuggestions by viewModel.sloganSuggestions.collectAsStateWithLifecycle()
    val isGeneratingSlogans by viewModel.isGeneratingSlogans.collectAsStateWithLifecycle()
    val state by viewModel.photoState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    var showAdvancedSettings by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Card with Studio Glow
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
                            .size(42.dp)
                            .background(
                                Brush.radialGradient(listOf(AmberGold.copy(alpha = 0.35f), Color.Transparent)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null,
                            tint = AmberGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Studio Photo Publicitaire IA",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Shooting commercial 8K • Rendu Hasselblad & Éclairage Studio",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Prompt Input Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StudioCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Description du produit / de l'annonce",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Décrivez simplement votre produit, sa matière ou son décor :",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = prompt,
                    onValueChange = { viewModel.updatePhotoPrompt(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("photo_prompt_input"),
                    minLines = 3,
                    maxLines = 6,
                    placeholder = {
                        Text(
                            "Ex: Flacon de parfum noir et doré posé sur marbre sombre avec reflets dorés et brume subtile...",
                            color = TextSecondary.copy(alpha = 0.6f)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmberGold,
                        unfocusedBorderColor = StudioBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = StudioCardElevated,
                        unfocusedContainerColor = StudioCardElevated
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Suggestion chips
                Text(
                    text = "Inspirations en 1 clic :",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val quickIdeas = listOf(
                        "Faso Dan Fani Luxe",
                        "Beurre de Karité Bio",
                        "Jus Bissap & Gingembre",
                        "Poulet Bicyclette Grillé",
                        "Smartphone Pro 5G"
                    )
                    quickIdeas.forEach { idea ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    val fullIdea = when (idea) {
                                        "Faso Dan Fani Luxe" -> "Tenue haute couture en pagne tissé Faso Dan Fani doré et indigo sur mannequin en lumière studio chaude"
                                        "Beurre de Karité Bio" -> "Pot en verre ambré de beurre de karité pur artisanal du Burkina avec noix de karité et lumière dorée"
                                        "Jus Bissap & Gingembre" -> "Bouteille givrée de jus de bissap rouge rubis et gingembre citronné avec glaçons et fleurs d'hibiscus"
                                        "Poulet Bicyclette Grillé" -> "Poulet bicyclette grillé doré aux épices burkinabè et alloco croustillant sur plat artisanal fumant"
                                        else -> "Smartphone flagship en titane brossé sur socle sombre avec éclairage néon studio pour vente en ligne"
                                    }
                                    viewModel.updatePhotoPrompt(fullIdea)
                                },
                            color = StudioCardElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                        ) {
                            Text(
                                text = "+ $idea",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = AmberGold
                            )
                        }
                    }
                }
            }
        }

        // Category Selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StudioCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Secteur & Style publicitaire",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdCategory.entries.forEach { cat ->
                        val isSelected = category == cat
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.updatePhotoCategory(cat) },
                            color = if (isSelected) AmberGold else StudioCardElevated,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) AmberGold else StudioBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = cat.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.Black else TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Format & Ratio Selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StudioCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Format d'affichage publicitaire",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdRatio.entries.forEach { r ->
                        val isSelected = ratio == r
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.updatePhotoRatio(r) },
                            color = if (isSelected) ElectricViolet else StudioCardElevated,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) ElectricViolet else StudioBorder
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = r.ratioLabel.split(" / ").first(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else AmberGold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = r.displayName.split(" ").first(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isSelected) Color.White else TextSecondary,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Advanced Marketing Options Toggle (Lighting, Branding, Badge Style & Position)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { showAdvancedSettings = !showAdvancedSettings },
            color = StudioCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Tune, contentDescription = null, tint = AmberGold, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Personnalisation Studio & Badges Publicitaires",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AmberGold
                    )
                }
                Text(
                    text = if (showAdvancedSettings) "▲ Masquer" else "▼ Personnaliser",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
        }

        AnimatedVisibility(visible = showAdvancedSettings) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Éclairage Studio & Ambiance",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    AdLighting.entries.forEach { l ->
                        val isSelected = lighting == l
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.updatePhotoLighting(l) },
                            color = if (isSelected) AmberGold.copy(alpha = 0.15f) else StudioCardElevated,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) AmberGold else StudioBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isSelected) "●" else "○",
                                    color = if (isSelected) AmberGold else TextSecondary,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = l.displayName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isSelected) TextPrimary else TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Brand name field
                    OutlinedTextField(
                        value = brandName,
                        onValueChange = { viewModel.updateBrandName(it) },
                        label = { Text("Nom de la marque") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmberGold,
                            unfocusedBorderColor = StudioBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    // Badge text field
                    OutlinedTextField(
                        value = badgeText,
                        onValueChange = { viewModel.updateBadgeText(it) },
                        label = { Text("Badge Promo (ex: NOUVEAU, -30% OFF, LIMITÉ)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmberGold,
                            unfocusedBorderColor = StudioBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    // Badge Style Selector (Colors)
                    Text(
                        text = "Couleur du badge promotionnel",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AdBadgeStyle.entries.forEach { style ->
                            val isSel = badgeStyle == style
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.updateBadgeStyle(style) },
                                color = Color(style.bgColorHex),
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isSel) 2.dp else 1.dp,
                                    if (isSel) Color.White else Color.Transparent
                                )
                            ) {
                                Text(
                                    text = style.displayName,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(style.textColorHex)
                                )
                            }
                        }
                    }

                    // Badge Position Selector
                    Text(
                        text = "Position du badge sur l'image",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AdBadgePosition.entries.forEach { pos ->
                            val isSel = badgePosition == pos
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.updateBadgePosition(pos) },
                                color = if (isSel) AmberGold else StudioCardElevated,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) AmberGold else StudioBorder
                                )
                            ) {
                                Text(
                                    text = pos.displayName,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) Color.Black else TextPrimary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Headline + AI Slogan Copilot Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Accroche / Slogan publicitaire",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable(enabled = !isGeneratingSlogans) { viewModel.generateMarketingSlogans() },
                            color = ElectricViolet.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isGeneratingSlogans) {
                                    CircularProgressIndicator(color = ElectricViolet, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Recherche...", style = MaterialTheme.typography.labelSmall, color = ElectricViolet)
                                } else {
                                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = ElectricViolet, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Idées IA", style = MaterialTheme.typography.labelSmall, color = ElectricViolet, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = headline,
                        onValueChange = { viewModel.updateHeadline(it) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmberGold,
                            unfocusedBorderColor = StudioBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    // Slogan suggestions pills if generated
                    if (sloganSuggestions.isNotEmpty()) {
                        Text(
                            text = "💡 Suggestions de slogans :",
                            style = MaterialTheme.typography.labelSmall,
                            color = CyberCyan
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            sloganSuggestions.forEach { slogan ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.updateHeadline(slogan) },
                                    color = StudioCardElevated,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("✨", fontSize = 12.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = slogan,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Action Button with Gradient Glow
        Button(
            onClick = { viewModel.generatePhotoAd() },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("generate_photo_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = AmberGold,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = state !is PhotoGenState.Generating
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (state is PhotoGenState.Generating) "Shooting IA en cours..." else "Lancer le Shooting Photo IA Pro",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        // Status / Result Display
        when (val curr = state) {
            is PhotoGenState.Generating -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StudioCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AmberGold),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = AmberGold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = curr.stepMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Synthèse d'image studio haute fidélité (Mode Studio & Gemini)\n(Délai moyen : quelques secondes)",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            color = AmberGold,
                            trackColor = StudioBorder
                        )
                    }
                }
            }

            is PhotoGenState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BadgeRed.copy(alpha = 0.15f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BadgeRed),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = BadgeRed
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Échec de génération",
                                fontWeight = FontWeight.Bold,
                                color = BadgeRed
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = curr.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { viewModel.generatePhotoAd() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberGold),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AmberGold)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Réessayer")
                        }
                    }
                }
            }

            is PhotoGenState.Success -> {
                val photoAd = curr.photoAd
                val showWatermark by viewModel.showWatermark.collectAsStateWithLifecycle()
                CommercialAdPreviewCard(
                    photoAd = photoAd,
                    showWatermark = showWatermark,
                    onCopyPrompt = {
                        clipboardManager.setText(AnnotatedString(photoAd.enhancedPrompt))
                        Toast.makeText(context, "Prompt studio copié !", Toast.LENGTH_SHORT).show()
                    },
                    onShare = {
                        sharePhotoAd(context, photoAd)
                    }
                )
            }

            is PhotoGenState.Idle -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StudioCard.copy(alpha = 0.5f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "💡 Astuce de Directeur Artistique",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = AmberGold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pour des résultats spectaculaires, mentionnez la texture (verre, métal brossé, cuir), l'arrière-plan (marbre, néon, bois brut) et le type de produit. Notre moteur IA se charge d'ajouter automatiquement l'éclairage de studio et les paramètres optiques.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

/**
 * Renders the generated commercial advertisement with camera viewfinder frame and marketing overlays
 */
@Composable
fun CommercialAdPreviewCard(
    photoAd: GeneratedPhotoAd,
    showWatermark: Boolean = true,
    onCopyPrompt: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = StudioCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, AmberGold),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Viewport header with professional studio specs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Red, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "VIEWFINDER 4K • STUDIO MASTER",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AmberGold,
                        fontSize = 10.sp
                    )
                }

                Surface(
                    color = StudioCardElevated,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                ) {
                    Text(
                        text = photoAd.ratio.ratioLabel,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // The Rendered Ad Canvas with viewfinder brackets & overlays
            val aspect = photoAd.ratio.widthRatio / photoAd.ratio.heightRatio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspect.coerceIn(0.6f, 1.8f))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black)
            ) {
                // The AI Generated Image
                Image(
                    bitmap = photoAd.bitmap.asImageBitmap(),
                    contentDescription = photoAd.prompt,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Viewfinder Corner Brackets Overlay
                ViewfinderBrackets()

                // Top Vignette gradient for branding
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.75f), Color.Transparent)
                            )
                        )
                )

                // Bottom Vignette gradient for headline
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.88f))
                            )
                        )
                )

                // Marketing Promo Badge with selected style & position
                if (photoAd.badgeText.isNotBlank()) {
                    val badgeAlign = when (photoAd.badgePosition) {
                        AdBadgePosition.TOP_LEFT -> Alignment.TopStart
                        AdBadgePosition.TOP_RIGHT -> Alignment.TopEnd
                        AdBadgePosition.BOTTOM_LEFT -> Alignment.BottomStart
                    }
                    val badgePaddingBottom = if (photoAd.badgePosition == AdBadgePosition.BOTTOM_LEFT) 70.dp else 14.dp

                    Surface(
                        modifier = Modifier
                            .padding(start = 14.dp, end = 14.dp, top = 14.dp, bottom = badgePaddingBottom)
                            .align(badgeAlign)
                            .shadow(8.dp, RoundedCornerShape(6.dp)),
                        color = Color(photoAd.badgeStyle.bgColorHex),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = photoAd.badgeText.uppercase(),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(photoAd.badgeStyle.textColorHex)
                        )
                    }
                }

                // Brand Logo/Name Tag (placed opposite to badge if on top)
                if (photoAd.brandName.isNotBlank() && photoAd.badgePosition != AdBadgePosition.TOP_RIGHT) {
                    Surface(
                        modifier = Modifier
                            .padding(14.dp)
                            .align(Alignment.TopEnd),
                        color = Color.Black.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = photoAd.brandName.uppercase(),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AmberGold
                        )
                    }
                }

                // Bottom-Right: Subtle Watermark if free tier
                if (showWatermark) {
                    Surface(
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.BottomEnd),
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "⚡ AdGen Studio",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 9.sp
                        )
                    }
                }

                // Bottom: Headline & Camera Telemetry Tag
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(
                            start = 14.dp,
                            top = 14.dp,
                            bottom = 14.dp,
                            end = if (showWatermark) 114.dp else 14.dp
                        )
                ) {
                    if (photoAd.headline.isNotBlank()) {
                        Text(
                            text = photoAd.headline,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 2
                        )
                    }
                    Text(
                        text = "HASSELBLAD 100MM F/2.2 • ${photoAd.category.displayName.uppercase()} • 8K OCTANE",
                        style = MaterialTheme.typography.labelSmall,
                        color = AmberGold,
                        fontSize = 9.sp,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCopyPrompt,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AmberGold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copier Prompt", fontSize = 12.sp)
                }

                Button(
                    onClick = onShare,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGold, contentColor = Color.Black)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Exporter Pub", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Camera Viewfinder Bracket marks giving authentic studio look
 */
@Composable
fun ViewfinderBrackets() {
    Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        // Top-Left corner
        Text("⌜", color = Color.White.copy(alpha = 0.5f), fontSize = 18.sp, modifier = Modifier.align(Alignment.TopStart))
        // Top-Right corner
        Text("⌝", color = Color.White.copy(alpha = 0.5f), fontSize = 18.sp, modifier = Modifier.align(Alignment.TopEnd))
        // Bottom-Left corner
        Text("⌞", color = Color.White.copy(alpha = 0.5f), fontSize = 18.sp, modifier = Modifier.align(Alignment.BottomStart))
        // Bottom-Right corner
        Text("⌟", color = Color.White.copy(alpha = 0.5f), fontSize = 18.sp, modifier = Modifier.align(Alignment.BottomEnd))
    }
}
