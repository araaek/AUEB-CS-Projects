/* Ερώτημα 1
Δείξε μου τους τίτλους των ταινιών και τον μέσο όρο της βαθμολογία τους, 
για τις ταινίες που έχουν μέσο όρο βαθμολογίας μεταξύ 4 και 5. 
Να υπάρχει φθίνουσα ταξινόμηση ως προς την βαθμολογία και σε περίπτωση 
ίσης βαθμολογίας οι ταινίες να εμφανίζονται αλφαβητικά.
Output: 323 rows */
SELECT m.title, AVG(r.rating) AS average_rating
FROM movie m
INNER JOIN ratings r ON m.id = r.movie_id
GROUP BY m.id, m.title
HAVING AVG(r.rating) BETWEEN 4 AND 5 
ORDER BY average_rating DESC, title;


/* Ερώτημα 2
Δείξε μου πόσες ταινίες υπάρχουν από κάθε είδος, 
με τα αποτελέσματα να είναι σε φθίνουσα σειρά ως προς τον αριθμό των ταινιών
Output: 32 rows */
SELECT g.name AS genre, COUNT(hg.movie_id) AS num_of_movies
FROM genre g
LEFT OUTER JOIN hasGenre hg ON g.id = hg.genre_id
GROUP BY g.name
ORDER BY COUNT(hg.movie_id) DESC;


/* Ερώτημα 3
Δείξε μου τους τίτλους των ταινιών που έχουν λέξεις-κλειδιά που περιέχουν το "action"
Output: 39 rows*/
SELECT DISTINCT m.title
FROM movie m
INNER JOIN hasKeyword hk ON m.id = hk.movie_id
INNER JOIN keyword k ON hk.keyword_id = k.id
WHERE k.name LIKE '%action%';


/* Ερώτημα 4
Δείξε μου τους τίτλους των 100 ταινιών με τον μεγαλύτερο μέσο όρο βαθμολογίας
Output: 100 rows*/
SELECT TOP(100) m.title, AVG(r.rating) AS average_rating
FROM movie m
INNER JOIN ratings r ON m.id = r.movie_id
GROUP BY m.title
ORDER BY AVG(r.rating) DESC;

/* Ερώτημα 5
Δείξε μου τα ονόματα των ηθοποιών και τον αριθμό των ταινιών στις οποίες έχουν πρωταγωνιστήσει,
τα αποτελέσματα ταξινομούνται σε φθίνουσα σειρά βάσει του αριθμού των ταινιών και για 
ηθοποιούς με ίσο αριθμό ταινιών, υπάρχει αλφαβητική ταξινόμηση.
Output: 69009 rows*/
SELECT name, COUNT(*) AS num_of_movies
FROM movie_cast
GROUP BY name
ORDER BY num_of_movies DESC, name;

/* Ερώτημα 6
Δείξε μου τις ταινίες από τις κατηγορίες Action και Comedy, που στο cast εμφανίζονται περισσότεροι
από 8 ηθοποιοί. Τα αποτελέσματα να είναι ταξινομημένα σε φθίνουσα σειρά με βάση τον αριθμό των ηθοποιών
Output: 1497 rows*/
SELECT m.title, COUNT(mc.cid) AS num_of_actors
FROM movie m
INNER JOIN movie_cast mc ON m.id = mc.movie_id
INNER JOIN hasGenre hg ON m.id = hg.movie_id
INNER JOIN genre g ON hg.genre_id = g.id
WHERE g.name IN ('Action', 'Comedy')
GROUP BY m.title
HAVING COUNT(mc.cid) > 8

/* Ερώτημα 7
Δείξε μου τις ταινίες με Αγγλική γλώσσα. Στην περιγραφή (overview) να βρίσκεται η λέξη boring, 
love ή documentary και η κατηγορία να είναι Romance, Family ή Comedy.
Output: 68 rows*/
SELECT m.title
FROM movie m
INNER JOIN hasGenre hg ON m.id = hg.movie_id
INNER JOIN genre g ON hg.genre_id = g.id
WHERE m.original_language = 'en'
AND (m.overview LIKE '%boring%' OR m.overview LIKE '%love%' OR m.overview LIKE '%documentary%')
AND g.name IN ('Romance', 'Family', 'Comedy');

/* Ερώτημα 8
Δείξε μου τους τίτλους και την διάρκεια των ταινιών με διάρκεια μεταξύ δύο και δυόμιση ωρών,
σε αύξουσα σειρά ως προς την διάρκεια.
Output: 1346 rows*/
SELECT title, runtime
FROM movie
WHERE runtime BETWEEN 120 AND 150
ORDER BY runtime;

/* Ερώτημα 9
Δείξε μου αλφαβητικά τις πρώτες 50 ταινίες με τον μεγαλύτερο μέσο όρο βαθμολογίας, στην κατηγορία Drama.
Output: 50 rows*/
SELECT TOP(50) m.title, AVG(r.rating) AS average_rating
FROM movie m
INNER JOIN ratings r ON m.id = r.movie_id
INNER JOIN hasGenre hg ON m.id = hg.movie_id
INNER JOIN genre g ON hg.genre_id = g.id
WHERE g.name = 'Drama'
GROUP BY m.title
ORDER BY average_rating DESC;

/* Ερώτημα 10
Δείξε μου τους ηθοποιούς που έπαιξαν σε μια ταινία και ταυτόχρονα δούλεψαν ως μέλος της παραγωγής της, 
τον χαρακτήρα που έπαιξαν καθώς και την δουλειά που είχαν στην παραγωγή της.
Output: 4128 rows*/
SELECT DISTINCT cast.name, cast.character, crew.job
FROM movie_cast AS cast
INNER JOIN movie_crew AS crew ON cast.person_id = crew.person_id
AND cast.movie_id = crew.movie_id;

/* Ερώτημα 11
Δείξε μου τον αριθμός ταινιών ανά εταιρεία παραγωγής
Output: 6530 rows*/
SELECT pc.name, COUNT(*) AS num_movies
FROM ProductionCompany pc
LEFT JOIN hasProductionCompany hpc ON pc.id = hpc.pc_id
GROUP BY pc.name;

/* Ερώτημα 12
Δείξε μου τις ταινίες (με λέξεις-κλειδί) και το πλήθος των λέξεων-κλειδί τους σε αύξουσα σειρά πλήθους.
Output: 8332 rows*/
SELECT m.title as Movie_Title,COUNT(k.name) as Keyword_Count
FROM movie AS m, haskeyword AS hk, keyword AS k
WHERE m.id = hk.movie_id AND hk.keyword_id = k.id
GROUP BY m.title
ORDER BY Keyword_Count;





 