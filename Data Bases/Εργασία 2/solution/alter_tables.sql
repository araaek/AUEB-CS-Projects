-- Δημιουργία πρωτεύοντος  κλειδιού για τον πίνακα movie
ALTER TABLE movie 
	ADD CONSTRAINT PK_movie 
	PRIMARY KEY (id);
	
-- Δημιουργία πρωτεύοντος  κλειδιού για τον πίνακα genre
ALTER TABLE genre 
	ADD CONSTRAINT PK_genre 
	PRIMARY KEY (id);

-- Δημιουργία πρωτεύοντος  κλειδιού για τον πίνακα productioncompany
ALTER TABLE productioncompany 
	ADD CONSTRAINT PK_productioncompany 
	PRIMARY KEY (id);

-- Δημιουργία πρωτεύοντος  κλειδιού για τον πίνακα collection
ALTER TABLE collection 
	ADD CONSTRAINT PK_collection 
	PRIMARY KEY (id);

-- Δημιουργία πρωτεύοντος  κλειδιού για τον πίνακα movie_cast
ALTER TABLE movie_cast 
	ADD CONSTRAINT PK_movie_cast 
	PRIMARY KEY (cid);

-- Δημιουργία ξένου κλειδιού για τον πίνακα movie_cast
ALTER TABLE movie_cast 
	ADD CONSTRAINT FK_movie_cast_movie 
	FOREIGN KEY (movie_id) REFERENCES movie(id)
	
-- Δημιουργία πρωτεύοντος  κλειδιού για τον πίνακα movie_crew
ALTER TABLE movie_crew 
	ADD CONSTRAINT PK_movie_crew 
	PRIMARY KEY (cid);

-- Δημιουργία ξένου  κλειδιού για τον πίνακα movie_crew	
ALTER TABLE movie_crew 
	ADD CONSTRAINT FK_movie_crew_movie 
	FOREIGN KEY (movie_id) REFERENCES movie(id);

-- Δημιουργία πρωτεύοντος  κλειδιού για τον πίνακα keyword
ALTER TABLE keyword 
	ADD CONSTRAINT PK_keyword 
	PRIMARY KEY (id);
	
-- Δημιουργία ξένων  κλειδιών για τον πίνακα belongsTocollection
ALTER TABLE belongsTocollection 
	ADD CONSTRAINT FK_belongsTocollection_movie 
	FOREIGN KEY (movie_id) REFERENCES movie(id);
ALTER TABLE belongsTocollection	
	ADD CONSTRAINT FK_belongsTocollection_collection 
	FOREIGN KEY (collection_id) REFERENCES collection(id);
	
-- Δημιουργία ξένου  κλειδιού για τον πίνακα hasGenre
ALTER TABLE hasGenre 
	ADD CONSTRAINT FK_hasGenre_movie 
	FOREIGN KEY (movie_id) REFERENCES movie(id);
	
-- Δημιουργία ξένων  κλειδιών για τον πίνακα hasProductionCompany
ALTER TABLE hasProductioncompany 
	ADD CONSTRAINT FK_hasProductionCompany_movie 
	FOREIGN KEY (movie_id) REFERENCES movie(id);
ALTER TABLE hasProductioncompany	
	ADD CONSTRAINT FK_hasProductionCompany_productioncompany
    FOREIGN KEY (pc_id) REFERENCES productioncompany(id);
	
-- Δημιουργία ξένου  κλειδιού για τον πίνακα Ratings
ALTER TABLE ratings 
	ADD CONSTRAINT FK_ratings_movie 
	FOREIGN KEY (movie_id) REFERENCES movie(id);

-- Δημιουργία ξένων  κλειδιών για τον πίνακα hasKeyword
ALTER TABLE haskeyword 
	ADD CONSTRAINT FK_hasKeyword_movie 
	FOREIGN KEY (movie_id) REFERENCES movie(id);
ALTER TABLE haskeyword
	ADD CONSTRAINT  FK_hasKeyword_keyword
    FOREIGN KEY (keyword_id) REFERENCES keyword(id);

