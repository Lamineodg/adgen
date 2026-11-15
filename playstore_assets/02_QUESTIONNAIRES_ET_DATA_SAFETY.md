# 📋 Questionnaires Play Console & Sécurité des Données

Voici les réponses exactes à sélectionner dans la section **Contenu de l'application** de la Google Play Console pour **AdGen Studio**.

---

## 1. Règles de confidentialité
- **URL** : Collez l'adresse `https://...` de votre site hébergé (`site_web/index.html` ou Google Sites).

---

## 2. Accès aux applications
- Sélectionnez : **« Toutes les fonctionnalités sont disponibles sans restriction »**  
  *(Aucun identifiant ni mot de passe n'est bloquant pour accéder à l'application).*

---

## 3. Annonces publicitaires
- Sélectionnez : **« Non, mon application ne contient pas d'annonces »**  
  *(Aucun SDK publicitaire tiers comme AdMob n'est intégré).*

---

## 4. Classification du contenu (PEGI / IARC)
- **Adresse e-mail** : Votre adresse e-mail développeur (ex. `lamineodg92@gmail.com`).
- **Catégorie** : **Utilitaire, productivité, communication ou autre**.
- **Réponses au questionnaire** : Répondez **Non** à toutes les questions concernant la violence, la nudité, les jeux d'argent, le langage grossier ou le partage de localisation.
- **Résultat attendu** : **PEGI 3 / Tous publics**.

---

## 5. Public cible et contenu
- **Tranches d'âge cibles** : Cochez **18 ans et plus** *(ou 13-15, 16-17 et 18+ ; évitez les moins de 13 ans pour ne pas déclencher l'examen Familles)*.
- **L'application pourrait-elle plaire aux enfants ?** : **Non** *(outil de marketing et création publicitaire professionnelle)*.

---

## 6. Applications d'actualités / Santé / Finances / Gouvernement
- Répondez **Non** à toutes ces catégories spéciales.

---

## 7. Sécurité des données (Data Safety)

1. **Votre application collecte-t-elle ou partage-t-elle des données utilisateur requises ?**
   - Vous pouvez répondre **Oui** *(ou Non si vous considérez uniquement les requêtes anonymes de génération IA ; répondre Oui pour les requêtes textuelles est plus transparent)* :
   - Si vous répondez **Oui** :
     - **Les données sont-elles chiffrées en transit ?** : **Oui** (HTTPS/TLS).
     - **Proposez-vous un moyen de supprimer les données ?** : **Oui** (les données locales sont supprimables directement dans le Portfolio ou via les paramètres Android ; aucune base de données serveur utilisateur n'est conservée).
     - **Type de données (si déclaré)** : *Contenu généré par l'utilisateur / Autre texte saisi dans l'application* (uniquement pour la **Fonctionnalité de l'application**, non partagé avec des tiers à des fins publicitaires).
   - **Partage avec des tiers** : **Non** (aucune donnée vendue ou partagée à des fins marketing).

---

## 8. Déclaration d'identifiant publicitaire (Advertising ID)
- **Votre application utilise-t-elle un identifiant publicitaire ?** : **Non**.

---

## 9. Notes de version (à coller lors de l'envoi du `.aab`)

```text
Lancement initial d'AdGen Studio v1.0 :
• Studio Photo Publicitaire IA multi-formats (1:1, 9:16, 16:9, 4:5)
• Directeur Vidéo Pub & Storyboard interactif avec voix-off
• Modèles publicitaires préconfigurés et badges personnalisables
• Support clé API personnelle (BYOK) et Portfolio local
```
