% Définition des jours par mois (simplifié et non complet)
days(1, 31). % Janvier
days(2, 28). % Février (non bissextile)
days(3, 31). % Mars
days(4, 30). % Avril
days(5, 31). % Mai
days(6, 30). % Juin
days(7, 31). % Juillet
days(8, 31). % Aout
days(9, 30). % Septembre
days(10, 31). % Octobre
days(11, 30). % Novembre
days(12, 31). % Décembre

% Définir la date cible
target_date(2024, 5, 11).

% Calculer le premier jour de l'année (exemple fictif, 1 = lundi, 7 = dimanche)
first_day_of_year(2024, 1).

% Trouver le premier lundi de l'année
first_monday(Year, Monday) :-
    first_day_of_year(Year, FirstDay),
    Monday = ((8 - FirstDay) mod 7 )+ 1.

% Calculer le jour de l'année pour la date cible
day_of_year(Year, Month, Day, Count) :-
    target_date(Year, Month, Day),
    findall(D, (days(M, D), M), DaysList),M < Month,
    sumlist(DaysList, DaysBefore),
    Count = DaysBefore + Day.

% Déterminer le numéro de la semaine pour la date cible
week_number(Year, WeekNum) :-
    first_monday(Year, FirstMonday),
    day_of_year(Year, _, _, DayOfYear),
    WeekNum = ((DayOfYear - FirstMonday) / 7) + 1.

#show week_number/2.
