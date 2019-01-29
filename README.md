# IFT3913 - TP1

* Diagramme de classe conceptuel : fait
* Diagramme séquence système :
	Depuis Acteur Administrateur:
	- Création, modification et suppression d'aéroports
	- Création, modification et suppression de compagnies aériennes
	- Création, modification et suppression de vol
	- Création de sections d'un avion
	- Consultation des vols (qui arrivent, qui partent)

	Depuis Acteur Client:
	- Vérification des vols disponibles
	- Reserver siege (contrat - postcondition : siegé reservé/bloqué)
	- Payer siege (contrat - précond : avoir réservé)
	- Annuler reservation (contrat - précond : avoir réservé)
	- Changer réservation (contrat - précond : avoir réservé)