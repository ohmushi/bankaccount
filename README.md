# 📜 Banck Account
> Ce projet est une implémentation d'un système de gestion de compte bancaire en suivant les principes de Clean Architecture. Il permet aux utilisateurs d'effectuer des dépôts, des retraits et de consulter leur relevé de compte.

## 🔧 Installation et exécution

Prérequis : 
* Java 21

###  Installation
```bash
git clone https://github.com/ohmushi/bankaccount.git
cd bankaccount
./mvnw clean install
```

### Exécution

```bash
./mvnw exec:java
```

## 🚀 Description du Problème

### Fonctionnalités

* ✔ Dépôt d'argent
* ✔ Retrait d'argent
* ✔ Consultation du relevé de compte

### User Stories

US 1 :
```
Afin d'épargner de l'argent
En tant que client bancaire
Je veux pouvoir déposer de l'argent sur mon compte
```

US 2 :
```
Afin de récupérer une partie ou la totalité de mes économies
En tant que client bancaire
Je veux pouvoir effectuer un retrait de mon compte
Décisions
```

## 🔍 À voir en priorité
### Features Cucumber (tests)
* `test/resources`/cat/ohmushi/account/domain/ → fichiers `.feature`
  *  Contient les scénarios BDD qui définissent le comportement attendu du système (dépôt, retrait, relevé de compte).
* Vérifie que les règles métier sont bien respectées et testées.
* Voir la section [BDD avec Cucumber](#bdd-avec-cucumber) pour plus d'informations.

### Exemple
* `App.java`
* Montre un exemple d'utilisation du système par les use cases
* Composition et injection de dépendances
* Print dans stdout l'extrait de compte formatté

### Use Cases (Cas d'utilisation)
* `account/application/usecases/`
  * Chaque interface représente une action clé du système (ex: DepositMoneyInAccount).
* Voir la section [Cas d'utilisation ](#cas-dutilisation) pour plus d'informations.

### Domaine
* `account/domain/`
  * Définit les entités principales, leurs comportements et leurs règles métier.
* Voir la section [Domain-Driven Design (DDD)](#domain-driven-design-ddd) pour plus d'informations.

## 🏛 Architecture et principes

### Clean Architecture

J'ai choisi d'implémenter une approche Clean Architecture avec un design centré sur le domaine afin de garantir que la logique métier reste indépendante des frameworks et des préoccupations externes.

```
src/main/java/cat/ohmushi/account
├───application
│   ├───exceptions
│   ├───services
│   └───usecases
├───domain
│   ├───events
│   ├───exceptions
│   ├───account
│   └───repositories
├───exposition
│   ├───formatters
│   └───rest
└───infrastructure
    └───persistence
```

Le découpage est le suivant :
* application : couche d'orchestration des cas d'utilisation en appelant le domaine et en interagissant avec les autres couches (ex: services, persitence, gestion des erreurs applicatives)
* domain : contient la logique métier, avec les entités pure encapsulant les règles métier, et les interfaces des repositories
* exposition : gère la communication avec l'extérieur (ex: API REST)
* infrastructure : implémente les services techniques comme la persistance des données ou l'intégration avec d'autres systèmes

### Domain-Driven Design (DDD)
> Les règles métier sont encapsulées dans le domaine.

L'architecture du projet suit les principes du Domain-Driven Design (DDD) pour structurer le code autour du domaine métier, faciliter une séparation des responsabilités et assurer la maintenabilité et la flexibilité de l'application.

* Le dossier `domain` contient les modèles métier, les événements et les exceptions propres au domaine bancaire
* L’application ne dépend pas de framework : les règles métier sont autonomes et peuvent être testées indépendamment de l’infrastructure
* Le cœur du métier est isolé des détails d’implémentation comme la base de données ou l’exposition

Exemples dans le projet :

* domain/account/Account.java définit un compte bancaire avec son identifiant, son solde et ses opérations

* domain/account/Money.java encapsule la logique de manipulation de montants en devises

Grâce à cette approche, l'application reste modulaire, testable et évolutive, car les modifications du métier n’impactent pas la couche technique et inversement.

### Cas d'utilisation 

Chaque cas d'utilisation est traité comme un contrat (interface).
Il y a assez peu de cas d'utilisation, j'ai donc décidé de réunir leur implémentations dans un seul service. Le service implémente les use cases DepositMoneyInAccount, WithdrawMoneyFromAccount et GetStatementOfAccount.

Les cas d'utilisation sont dans `application/usecases`. On peut y retrouver par exemple :
```java
public interface WithdrawMoneyFromAccount {
    void withdraw(String accountId, BigDecimal amount) throws AccountApplicationException;
}
```

Les cas d'utilisations sont les points d'entré dans l'application, ils définissent les actions que l'on peut effectuer sur le système, et donc c'est ce qui doit être appellé par l'exposition (par exemple les controllers de l'api REST).

### Gestion des erreurs

J'ai fortement utilisé des exceptions personnalisées pour gérer les erreurs spécifiques au domaine et à l'application.

J'ai aussi utilisé les types optionnels (Optional) lorsque nécessaire afin d'éviter les problèmes liés aux valeurs nulles et garantir une gestion sécurisée des données manquantes.

#### Erreurs domaines

Les erreurs domaines sont relatives à des violations de règle métier, par exemple si l'on essaie de faire un dépot d'un montant négatif, une exception `TransfertException` est lancée.

#### Erreurs applicatives

Les erreurs applicatives relèvent plus d'une opération dans un service qui ne s'est pas déroulé normalement. Par exemple déposer de l'argent dans un compte dont l'id n'est pas connu va lancer une `AccountNotFoundException`.

## 🧪 Test

```bash
./mvnw test
```
### BDD avec Cucumber

J'ai utilisé Cucumber pour le développement piloté par le comportement (BDD).
Les 3 fonctionnalités principales, Dépôt, Retrait, Relevé ont des fichier `feature` faisant office de tests écrits Gherkin (en langage naturel : given → when → then) validé ensuite par Junit et AssertJ.
Ces features garantissent que les règles métier sont bien définies et testées du point de vue de l'utilisateur final.

Les features ressemblent par exemple à :

```gherkin
Scenario: Display statement after multiple transactions
    Given an account with an initial balance of €1000
    And a deposit of €500 on 01/03/2025
    And a withdraw of €200 on 02/03/2025
    When I check my statement
    Then my statement should be:
      | Date       | Operation  | Amount | Balance |
      | 01/03/2025 | Deposit    | +€500  | €1500   |
      | 02/03/2025 | Withdrawal | -€200  | €1300   |
```

### Tests unitaires

JUnit & AssertJ sont utilisés pour les tests unitaires afin de garantir la fiabilité et la justesse des composants individuels. Le domaine a été développé en premier et en TDD (Test-Driven Development : red → green → refactor), en combinaison avec les tests Cucumber.

## 💡 Décisions Techniques & Justifications

| Choix                                                     | Avantages                                                                                | Désavantages                                                                            |
| --------------------------------------------------------- | ---------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------- |
| **Java 21**                                               | S'inscrit bien dans le domaine bancaire, dernière version LTS, stabilité                 |                                                                                         |
| **Clean Architecture**                                    | Séparation claire des préoccupations, facilite la maintenance et l'évolution du projet   | Complexité accrue, nécessite plus de couches et de classes                              |
| **Domain-Driven Design (DDD)**                            | Encapsule les règles métier, facilite leur écriture et leur lecture                      | Demande un effort initial important pour bien structurer le domaine                     |
| **Tests BDD avec Cucumber**                               | Vérifie le respect des règles métier du point de vue utilisateur  (Gherkin)              | Demande un bon maintien des scénarios                                                   |
| **Tests unitaires avec JUnit & AssertJ**                  | Garantit la fiabilité des composants métier avec une approche TDD                        |                                                                                         |
| **Gestion des erreurs via exceptions personnalisées**     | Meilleure lisibilité et contrôle des erreurs spécifiques au domaine et à l’application   | Multiplication des exceptions qui implique une complexification de leur gestion         |

## 🎯 Améliorations Futures
* Implementation complète d'une API REST
* Support des devises autre de l'euro et le dollar
  * Récupération des devises chez service externe
* Notifications utilisateur
* Extrait de compte sous d'autres formats (json, excel, csv, ...)

## 🪶 Auteur

Theo OMNES (@ohmushi)