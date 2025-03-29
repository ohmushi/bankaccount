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
> ici utilisation de maven wrapper (mvnw)

> remplacer par mvnw.cmd sur Windows

> ou utiliser mvn sans le wrapper

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

## 🏛 Architecture et principes

### Clean Architecture

J'ai choisi d'implémenter une approche Clean Architecture avec un design centré sur le domaine afin de garantir que la logique métier reste indépendante des frameworks et des préoccupations externes.
Le projet a une approche événementielle pour avoir un historique précis des actions effectuées sur le compte.

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

Le projet suit une structure de projet qui respecte les principes de Clean Architure, c'est à dire la séparation des règles métier et le reste (exposition et persistence).

### Domain-Driven Design (DDD)
> Les règles métier sont encapsulées dans le domaine.

L'architecture du projet suit les principes du Domain-Driven Design (DDD) pour garantir une séparation claire entre la logique métier et les préoccupations techniques.

* Le dossier `domain` contient les modèles métier, les événements et les exceptions propres au domaine bancaire
* L’application ne dépend pas de framework : les règles métier sont autonomes et peuvent être testées indépendamment de l’infrastructure
* Le cœur du métier est isolé des détails d’implémentation comme la base de données ou l’exposition (api REST par exemple)

Exemples dans le projet :

* domain/account/Account.java définit un compte bancaire avec son identifiant, son solde et ses opérations.

* domain/account/Money.java encapsule la logique de manipulation de montants en devises.

Grâce à cette approche, l'application reste modulaire, testable et évolutive, car les modifications du métier n’impactent pas la couche technique et inversement.

### Event Driven

#### Pourquoi une approche événementielle ?

La gestion d'évènements dans l'entité Account permet d'avoir un historique précis des actions effectuées sur le compte.

Par exemple l'événement pour un dépôt d’argent :

```java
domain/events/AccountEvent.java

record MoneyDepositedInAccount(
            Money deposited,
            LocalDateTime eventDate,
            Money newBalance) implements AccountEvent 
    { 
        // ... 
    }
```

Dans un système plus avancé, ces événements pourraient être publiés dans un bus d’événements pour être traités de manière asynchrone, par exemple avec **Kafka** ou RabbitMQ (ou un bus en mémoire).

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
Les 3 fonctionnalités principales, Dépôt, Retrait, Relevé ont des fichier `feature` qui sont des tests écrits Gherkin (en langage naturel : given → when → then).
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

### Java 21

L’utilisation de Java est utilisé depuis plus de 25 ans dans des applications critiques comme la finance, la banque et l’industrie pour différentes raisons :

* 1️⃣ Sécurité et fiabilité → essentiel pour une application bancaire
* 2️⃣ Support à long terme → stabilité assurée sur plusieurs années
* 2️⃣ Écosystème riche → outils et frameworks adaptés aux besoins métiers
* 3️⃣ Performance et scalabilité → optimisé pour les charges lourdes et multi-threading
* 4️⃣ Portabilité et interopérabilité → compatible avec de nombreux systèmes

La version 21 est la dernière LTS (Long-Term Support) à ce jour (mars 2025), ce qui garantit un support à long terme avec des mises à jour de sécurité et de stabilité, et permet d’éviter les migrations fréquentes vers de nouvelles versions.

### Clean Architecture

#### Avantages

* ✔ Séparation claire des préoccupations, rendant le système plus facile à maintenir et à faire évoluer.
* ✔ La logique métier reste indépendante des frameworks et de l'infrastructure, assurant une adaptabilité à long terme.
* ✔ Gestion robuste des erreurs pour éviter les défaillances silencieuses et assurer la fiabilité du système.
* ✔ Stratégie de test bien définie pour garantir le bon fonctionnement et le comportement attendu du système.

#### Inconvénients

* ⚠ L'architecture Clean ajoute une certaine complexité dans la structuration du projet.
* ⚠ L'utilisation d'exceptions personnalisées et de types optionnels nécessite un effort supplémentaire en gestion.

## 🎯 Améliorations Futures
* Implementation complète d'une API REST
* Support des devises autre de l'euro et le dollar
  * Récupération des devises chez service externe
* Notifications utilisateur
* Extrait de compte sous d'autres formats (json, excel, csv, ...)

## 🪶 Auteur

Theo OMNES (@ohmushi)