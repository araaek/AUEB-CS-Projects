SELECT person_id , COUNT(gender) as diff_gender , COUNT(name) as diff_names
FROM Person
GROUP BY person_id
HAVING COUNT(gender) > 1 OR COUNT(name) > 1;

/* Βρέθηκαν 3 person_id τα οποία εμφανίζονται πάνω από μία φορά:
   το 47395 και το 1785844 για διαφορετικό gender και 
   το 63574 για διαφορετικό name.
   Παρακάτω κάνουμε τα απαιτούμενα updates */
   
UPDATE movie_cast
SET gender = 0
WHERE person_id = 47395;

UPDATE movie_crew 
SET gender = 0
WHERE person_id = 47395;

UPDATE movie_crew
set name = 'Chueng Ka-Fai'
WHERE person_id = 63574;

UPDATE movie_cast
SET gender = 0
WHERE person_id = 1785844;