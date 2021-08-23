# jeuInfection

UNIVERSITE DE CAEN NORMANDIE
L2 INFORMATIQUE

SECURITE ET AIDE A LA DECISION
JEU D'INFECTION


PARTICIPANTS
    1. Ben KABONGO BUZANGU - 21911598
    2. Mouctar MAMADOU BAH - 21911323
    3. Meryam EL BOUDOUTI  - 22012902


LANGAGE DE PROGRAMMATION
    Java


CLASSES IMPLEMENTEES

    Algorithm : classe mère des classes d'algorithmes (Negamax et Alphabeta)
    Alphabeta : implémentation de alphabeta
    Move      : gestionnaire des mouvements des joueurs
    Negamax   : implémentation de negamax
    State     : état de jeu

    Main      : programme principal
    MainRandom: programme principal pour jeu aléatoire
    MainUser  : programme principal pour jeu utilisateur contre machine


PROGRAMMES PRINCIPAUX

    Main.java

        Modes de lancement du programme en ligne de commande

        1.  java Main <int a> <int b> <boolean alphabetaFlag>
            @param a: indique la profondeur de raisonnement du premier joueur
            @param b: indique la profondeur de raisonnement du second joueur
            @param alphabetaFlag: si True, on utilise Alphabeta au lieu de Negamax
        2.  java Main <int a> <int b>
            Equivalent à java Main a b true
            On utilise alphabeta
        3.  java Main <int a>
            Equivalent à java Main a a true
            Les deux joueurs ont la même profondeur de raisonnement
            On utilise alphabeta
        4.  java Main
            Equivalent à java Main 3 3 true
            Les deux joueurs ont la même profondeur de raisonnement, par défaut 4


    MainRandom.java

        Modes de lancement du programme en ligne de commande

        1.  java MainRandom


    MainUser.java

            Modes de lancement du programme en ligne de commande

            1.  java MainUser
