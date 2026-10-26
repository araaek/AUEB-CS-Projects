/* 1. Αριθμός ταινιών ανά έτος (year, movies_per_year) 
για ταινίες με budget ταινίας μεγαλύτερο από 1000000 */
SELECT YEAR(release_date) AS year, COUNT(*) AS movies_per_year
FROM movie
WHERE budget > 1000000
GROUP BY YEAR(release_date)
ORDER BY year;  -- τα ταξινομούμε ως προς το έτος


/* 2. Αριθμός ταινιών ανά είδος (genre, movies_per_genre) 
για ταινίες που έχουν budget μεγαλύτερο από 1,000,000 
ή διάρκεια μεγαλύτερη από 2 ώρες. */
SELECT g.name AS genre, COUNT(*) AS movies_per_genre
FROM movie m
JOIN hasGenre hg ON m.id = hg.movie_id
JOIN genre g ON hg.genre_id = g.id
WHERE m.budget > 1000000 OR m.runtime > 120
GROUP BY g.name
ORDER BY movies_per_genre DESC;


/* 3. Αριθμός ταινιών ανά είδος και ανά έτος 
(genre, year, movies_per_gy) */
SELECT g.name AS genre, YEAR(m.release_date) AS year, COUNT(*) AS movies_per_gy
FROM movie m
JOIN hasGenre hg ON m.id = hg.movie_id
JOIN genre g ON hg.genre_id = g.id
GROUP BY g.name, YEAR(m.release_date)
ORDER BY genre, year;


/* 4. Για τον αγαπημένο σας ηθοποιό, το σύνολο των εσόδων (revenue) 
για τις ταινίες στις οποίες έχει συμμετάσχει ανά έτος (year, revenues_per_year).
Αγαπημένη ηθοποιός: Angelina Jolie */
SELECT YEAR(m.release_date) AS year, SUM(m.revenue) AS revenues_per_year
FROM movie m
JOIN movie_cast mc ON m.id = mc.movie_id
WHERE mc.name = 'Angelina Jolie'
GROUP BY YEAR(m.release_date)
ORDER BY year;


/* 5. Το υψηλότερο budget ταινίας ανά έτος (year, max_budget), όταν το budget αυτό
δεν είναι μηδενικό. */
SELECT YEAR(release_date) AS year, MAX(budget) AS max_budget
FROM movie
WHERE budget > 0
GROUP BY YEAR(release_date)
ORDER BY year;


/* 6. Τις συλλογές του πίνακα Collection που αναφέρονται σε τριλογίες, 
δηλαδή η συλλογή έχει ακριβώς 3 ταινίες (trilogy_name). */
SELECT name AS trilogy_name
FROM Collection
GROUP BY name
HAVING COUNT(*) = 3;


/* 7. Για κάθε χρήστη του πίνακα Ratings, να επιστραφούν η μέση βαθμολογία του χρήστη
και ο αριθμός των βαθμολογιών του (avg_rating, rating_count). */
SELECT user_id, AVG(rating) AS avg_rating , COUNT(rating) AS rating_count
from ratings
GROUP BY user_id;


/* 8. Οι 10 ταινίες με το υψηλότερο budget (movie_title, budget). Σε περίπτωση που
έχουμε ισοβαθμία μεταξύ δύο ή περισσοτέρων ταινιών για την θέση 10 και μετά, να
επιστραφεί μία από τις δύο.
(hint: Να χρησιμοποιηθεί ο τελεστής ORDER BY σε συνδυασμό με τον τελεστή TOP της
Microsoft SQL) */
SELECT TOP(10) title AS movie_title, budget
FROM movie
ORDER BY  budget DESC, title ASC;


/* 9. Χρησιμοποιώντας το ερώτημα 5, βρείτε με εμφώλευση την ταινία (ταινίες) με το
μεγαλύτερο budget ανά χρονιά (year, movies_with_max_budget), έχοντας
ταξινόμηση ως προς το έτος και το όνομα της ταινίας. */
WITH query_five (year,budget) AS (
    SELECT YEAR(release_date) AS year, MAX(budget) AS max_budget
    FROM movie
    WHERE budget > 0
    GROUP BY YEAR(release_date)
)
SELECT YEAR(release_date) as year , title AS movies_with_max_budget 
FROM movie, query_five 
WHERE YEAR(release_date) = query_five.year
AND movie.budget = query_five.budget
ORDER BY year, movies_with_max_budget;


/* 10. Χρησιμοποιώντας εμφώλευση, επιστρέψτε τους σκηνοθέτες (name, surname) που
έχουν σκηνοθετήσει τόσο ταινίες τρόμου, όσο και κωμωδίες, αλλά κανένα άλλο είδος
ταινίας.
(hint: Μπορούν να χρησιμοποιηθούν οι τελεστές EXISTS, NOT EXISTS μαζί με
συνθήκη ζεύξης μεταξύ του εξωτερικού και του εσωτερικού ερωτήματος) */
SELECT DISTINCT crew.name AS 'name  surname'
FROM movie_crew AS crew
JOIN hasGenre hg1 ON crew.movie_id = hg1.movie_id
JOIN genre g1 ON hg1.genre_id = g1.id
WHERE crew.job = 'Director'
AND g1.name = 'Horror'
AND EXISTS (
    SELECT *
    FROM movie_crew crew2
    JOIN hasGenre hg2 ON crew2.movie_id = hg2.movie_id
    JOIN genre g2 ON hg2.genre_id = g2.id
    WHERE crew2.person_id = crew.person_id
    AND crew2.job = 'Director'
    AND g2.name = 'Comedy'
)
AND NOT EXISTS (
    SELECT *
    FROM movie_crew crew3
    JOIN hasGenre hg3 ON crew3.movie_id = hg3.movie_id
    JOIN genre g3 ON hg3.genre_id = g3.id
    WHERE crew3.person_id = crew.person_id
    AND crew3.job = 'Director'
    AND g3.name NOT IN ('Horror', 'Comedy')
);


/* 11. Να απαντηθεί το προηγούμενο ερώτημα χρησιμοποιώντας τελεστές για σύνολα UNION,
INTERSECT, EXCEPT. */
-- Σκηνοθέτες που έχουν σκηνοθετήσει ταινίες τρόμου
SELECT DISTINCT crew.name AS 'name  surname'
FROM movie_crew crew
JOIN hasGenre hg1 ON crew.movie_id = hg1.movie_id
JOIN genre g1 ON hg1.genre_id = g1.id
WHERE crew.job = 'Director'
AND g1.name = 'Horror'

-- Σκηνοθέτες που έχουν σκηνοθετήσει ταινίες κωμωδίας
INTERSECT
SELECT DISTINCT crew.name
FROM movie_crew crew
JOIN hasGenre hg2 ON crew.movie_id = hg2.movie_id
JOIN genre g2 ON hg2.genre_id = g2.id
WHERE crew.job = 'Director'
AND g2.name = 'Comedy'

-- Σκηνοθέτες που έχουν σκηνοθετήσει άλλες ταινίες εκτός τρόμου και κωμωδίας
EXCEPT
SELECT DISTINCT crew.name
FROM movie_crew crew
JOIN hasGenre hg3 ON crew.movie_id = hg3.movie_id
JOIN genre g3 ON hg3.genre_id = g3.id
WHERE crew.job = 'Director'
AND g3.name NOT IN ('Horror', 'Comedy');


/* Θεωρείστε ότι ένα ζεύγος ταινιών είναι δημοφιλές όταν υπάρχουν πάνω από 10 χρήστες που
έχουν βαθμολογήσει και τις 2 ταινίες με άνω του 4 βαθμολογία.
12. Προσδιορίστε το ερώτημα το οποίο θα χρησιμοποιηθεί για να φτιάξουμε ένα view (όψη)
με το όνομα Popular_Movie_Pairs που περιέχει τα ids από τα δημοφιλή ζεύγη
ταινιών (id1,id2). */
SELECT R1.movie_id AS id1, R2.movie_id AS id2
FROM Ratings R1
JOIN Ratings R2 ON R1.user_id = R2.user_id 
    AND R1.movie_id < R2.movie_id  -- εξασφαλίζουμε ότι κάθε ζεύγος ταινιών εμφανίζεται μόνο μια φορά
WHERE R1.rating >= 4 AND R2.rating >= 4
GROUP BY R1.movie_id, R2.movie_id
HAVING COUNT(DISTINCT R1.user_id) > 10;

