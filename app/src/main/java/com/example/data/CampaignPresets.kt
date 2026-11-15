package com.example.data

object CampaignPresets {
    val presets = listOf(
        AdCampaignPreset(
            id = "faso_dan_fani",
            title = "Faso Dan Fani Haute Couture",
            category = AdCategory.STREETWEAR,
            description = "Pagne tissé traditionnel Faso Dan Fani aux motifs géométriques or et indigo, coupe moderne portée en plein cœur de Ouagadougou",
            prompt = "Tenue haute couture en pagne tissé Faso Dan Fani doré et indigo sur mannequin en lumière studio chaude et élégante",
            suggestedRatio = AdRatio.PORTRAIT,
            sampleDrawableName = "ad_sample_sneaker"
        ),
        AdCampaignPreset(
            id = "karite_burkina",
            title = "Beurre de Karité Pur du Burkina",
            category = AdCategory.WELLNESS,
            description = "Pot en verre ambré de beurre de karité 100% naturel et bio du Burkina Faso, entouré de noix de karité dorées et feuilles fraîches",
            prompt = "Pot de beurre de karité pur artisanal du Burkina sur bois noble avec noix de karité et lumière dorée naturelle",
            suggestedRatio = AdRatio.SQUARE,
            sampleDrawableName = "ad_sample_perfume"
        ),
        AdCampaignPreset(
            id = "bissap_gingembre",
            title = "Jus Frais Bissap & Gingembre",
            category = AdCategory.FOOD,
            description = "Bouteille givrée de jus de bissap rouge rubis et gingembre citronné avec gouttes de fraîcheur, fleurs d'hibiscus et glaçons",
            prompt = "Bouteille givrée de jus de bissap rouge rubis et gingembre avec glaçons, éclaboussures fraîches et fleurs d'hibiscus",
            suggestedRatio = AdRatio.STORY_REEL,
            sampleDrawableName = "ad_sample_burger"
        ),
        AdCampaignPreset(
            id = "poulet_bicyclette",
            title = "Poulet Bicyclette Grillé",
            category = AdCategory.FOOD,
            description = "Poulet bicyclette braisé doré aux épices burkinabè, servi fumant avec alloco croustillant et oignons caramélisés, livraison express Ouaga & Bobo",
            prompt = "Poulet bicyclette grillé doré aux épices et plantains frits alloco sur plat artisanal, fumée légère et éclairage culinaire chaud",
            suggestedRatio = AdRatio.SQUARE,
            sampleDrawableName = "ad_sample_burger"
        ),
        AdCampaignPreset(
            id = "perfume_luxe",
            title = "Parfum Élixir Noir & Or",
            category = AdCategory.LUXURY,
            description = "Flacon de parfum haute parfumerie en verre noir taillé, accents or pur, posé sur marbre noir avec brume dorée",
            prompt = "Flacon de parfum de luxe noir et doré sur marbre sombre avec brume dorée et reflets d'eau",
            suggestedRatio = AdRatio.SQUARE,
            sampleDrawableName = "ad_sample_perfume"
        ),
        AdCampaignPreset(
            id = "smartphones_ouaga",
            title = "Boutique Tech & Smartphones",
            category = AdCategory.TECH,
            description = "Smartphone haut de gamme en titane posé sur socle sombre avec reflets néon, idéal pour les annonces Facebook & WhatsApp au Burkina",
            prompt = "Smartphone flagship en titane brossé en lévitation avec éclairage studio cyberpunk et reflets de verre cristallins",
            suggestedRatio = AdRatio.PORTRAIT,
            sampleDrawableName = "ad_sample_sneaker"
        )
    )
}
