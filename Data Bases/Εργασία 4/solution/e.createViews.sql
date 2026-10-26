CREATE VIEW Actor AS
    SELECT DISTINCT person_id, gender, name
    FROM movie_cast;

CREATE VIEW CrewMember AS
    SELECT DISTINCT person_id, gender, name 
    FROM movie_crew;

CREATE VIEW Person AS
    SELECT person_id, gender, name
    FROM Actor
UNION
    SELECT person_id, gender, name
    FROM CrewMember;

