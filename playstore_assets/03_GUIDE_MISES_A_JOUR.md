# 🔄 Guide des Mises à Jour après Publication

Oui, vous pouvez **modifier et améliorer votre application à l'infini** après sa publication sur le Google Play Store.

---

## 1. Modifications sans nouvelle version de l'app (instantané)
Directement depuis la **Google Play Console**, vous pouvez modifier à tout moment sans toucher au code :
- Le titre et les descriptions (courte / longue)
- Les captures d'écran, l'icône et la bannière
- Les pays de disponibilité

---

## 2. Modifications du code (nouvelles fonctionnalités ou corrections)

Respectez toujours ces **2 règles d'or** :

1. **Ne changez JAMAIS `applicationId`** dans `app/build.gradle.kts` :
   - Il doit toujours rester : `applicationId = "com.aistudio.applet.tjwfmu"`
   - *(C'est l'identité unique de votre app sur le Play Store).*

2. **Augmentez toujours `versionCode` et `versionName`** dans `app/build.gradle.kts` :
   ```kotlin
   defaultConfig {
       applicationId = "com.aistudio.applet.tjwfmu"
       minSdk = 24
       targetSdk = 36
       versionCode = 2        // ⬅️ Passez de 1 à 2 (puis 3, 4, ...)
       versionName = "1.1"    // ⬅️ Passez de "1.0" à "1.1"
   }
   ```

3. **Publiez la mise à jour** :
   - Exportez le nouveau fichier `.aab` depuis AI Studio.
   - Dans la Google Play Console, allez dans **Production > Créer une version**.
   - Déposez le nouveau `.aab`, ajoutez une note (ex. *« Nouveaux modèles et améliorations »*) et cliquez sur **Envoyer pour examen**.

Vos utilisateurs recevront automatiquement la mise à jour via le Play Store sans perdre leurs réglages.
