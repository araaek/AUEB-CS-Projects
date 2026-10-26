/* hasGenre */
ALTER TABLE hasGenre
ADD CONSTRAINT PK_hasGenre PRIMARY KEY (movie_id, genre_id);

/* hasKeyword */
ALTER TABLE hasKeyword
ADD CONSTRAINT PK_hasKeyword PRIMARY KEY (movie_id, keyword_id);

/* belongsTocollection */
ALTER TABLE belongsTocollection
ADD CONSTRAINT PK_belongsTocollection PRIMARY KEY (movie_id, collection_id);

/* hasProductioncompany */
ALTER TABLE hasProductioncompany
ADD CONSTRAINT PK_hasProductioncompany PRIMARY KEY (movie_id, pc_id);

/* Ratings */
ALTER TABLE Ratings
ADD CONSTRAINT PK_Ratings PRIMARY KEY (user_id, movie_id);






