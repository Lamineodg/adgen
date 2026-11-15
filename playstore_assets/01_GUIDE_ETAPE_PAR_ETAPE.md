# 🚀 Dossier Complet de Publication — AdGen Studio

Tout ce dont vous avez besoin pour publier **AdGen Studio** sur le **Google Play Store** est regroupé ici dans ce dossier (`playstore_assets/`).

---

## 📂 Contenu de votre dossier

| Fichier | Rôle |
| :--- | :--- |
| `01_GUIDE_ETAPE_PAR_ETAPE.md` | Ce guide complet avec toutes les étapes dans l'ordre |
| `02_QUESTIONNAIRES_ET_DATA_SAFETY.md` | Réponses exactes aux formulaires obligatoires de la Play Console |
| `03_GUIDE_MISES_A_JOUR.md` | Comment modifier et mettre à jour l'app après publication |
| `FICHE_PLAY_STORE.md` | Titre, description courte, description longue (ASO) et tags |
| `POLITIQUE_CONFIDENTIALITE.md` | Texte légal en Markdown (pour Google Sites ou Notion) |
| `site_web/index.html` | Page web prête à héberger (présentation + politique de confidentialité) |
| `icon_512x512.png` | Icône HD 512×512 px conforme Play Store |
| `feature_graphic_1024x500.png` | Bannière officielle 1024×500 px conforme Play Store |

---

## ✅ Checklist des étapes à suivre

### Étape 1 : Préparer vos captures d'écran (5 min)
Google Play exige au minimum **2 captures d'écran** de téléphone (recommandé : 4).
Dans l'émulateur à droite dans AI Studio, prenez une capture de :
- [ ] L'écran **Photos** avec une pub générée (ex. Parfum ou Sneakers).
- [ ] L'écran **Vidéos** avec le simulateur en cours de lecture.
- [ ] L'écran **Modèles** (templates préconfigurés).
- [ ] L'écran **Portfolio** ou **Compte / Paramètres**.

---

### Étape 2 : Mettre en ligne votre page Web / Politique de Confidentialité (2 min)
Google Play exige une URL publique (`https://...`) pour la politique de confidentialité. Vous avez **2 options gratuites** :

- **Option A — La plus rapide (avec le mini-site fourni `site_web/index.html`) :**
  1. Téléchargez le dossier `playstore_assets/site_web/`.
  2. Allez sur **[app.netlify.com/drop](https://app.netlify.com/drop)** (ou GitHub Pages / Vercel).
  3. Glissez-déposez le dossier `site_web` sur la page.
  4. Vous obtenez immédiatement un lien public `https://...` prêt à coller dans la Play Console !

- **Option B — Via Google Sites :**
  1. Allez sur **[sites.google.com](https://sites.google.com)** et créez une page.
  2. Copiez-collez tout le texte de `POLITIQUE_CONFIDENTIALITE.md`.
  3. Cliquez sur **Publier** et copiez le lien généré.

---

### Étape 3 : Créer l'application sur la Google Play Console
1. Rendez-vous sur **[play.google.com/console](https://play.google.com/console)** (frais uniques d'ouverture de compte développeur : 25 $).
2. Cliquez sur **Créer une application** :
   - **Nom** : `AdGen Studio : Photo Vidéo IA` *(28 / 30 caractères max)*
   - **Langue par défaut** : Français (France)
   - **Application ou jeu** : Application
   - **Gratuite ou payante** : Gratuite
3. Acceptez les déclarations puis cliquez sur **Créer l'application**.

---

### Étape 4 : Remplir la Fiche du Play Store
Dans le menu de gauche : **Développer > Présence sur le Play Store > Fiche principale du Play Store** :
- [ ] **Nom / Description courte / Description complète** : copiez-collez depuis `FICHE_PLAY_STORE.md`.
- [ ] **Icône de l'application** : importez `icon_512x512.png`.
- [ ] **Graphique de fonctionnalité** : importez `feature_graphic_1024x500.png`.
- [ ] **Captures d'écran téléphone** : importez vos 2 à 4 captures de l'Étape 1.
- [ ] Enregistrez les modifications.

---

### Étape 5 : Remplir les questionnaires obligatoires
Dans le menu de gauche : **Règlement et programmes > Contenu de l'application** :
- [ ] Suivez mot pour mot les réponses préparées dans **`02_QUESTIONNAIRES_ET_DATA_SAFETY.md`**.

---

### Étape 6 : Exporter l'app et l'envoyer pour examen
1. Dans **Google AI Studio**, utilisez le menu d'exportation / téléchargement en haut à droite pour obtenir votre fichier **`.aab` (Android App Bundle)** signé.
2. Sur la **Google Play Console** :
   - Allez dans **Tester et publier > Production** *(ou **Tests fermés** si Google vous demande 14 jours de test pour un nouveau compte personnel)*.
   - Cliquez sur **Créer une version**.
   - Importez votre fichier `.aab`.
   - Copiez les notes de version depuis `02_QUESTIONNAIRES_ET_DATA_SAFETY.md`.
   - Cliquez sur **Suivant**, vérifiez qu'il n'y a aucune erreur, puis cliquez sur **Envoyer pour examen**.

⏳ **Délai de validation Google :** généralement 1 à 3 jours ouvrés.
