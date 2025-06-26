-- Inserimenti per l'entità Autore
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazionalita) VALUES (1, 'Umberto', 'Eco', '1932-01-05', '2016-02-19', 'Italiano');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazionalita) VALUES (2, 'Gabriel', 'García Márquez', '1927-03-06', '2014-04-17', 'Colombiano');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazionalita) VALUES (3, 'J.K.', 'Rowling', '1965-07-31', NULL, 'Britannico');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazionalita) VALUES (4, 'Lev', 'Tolstoj', '1828-09-09', '1910-11-20', 'Russo');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazionalita) VALUES (5, 'Jane', 'Austen', '1775-12-16', '1817-07-18', 'Inglese');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazionalita) VALUES (6, 'Franz', 'Kafka', '1883-07-03', '1924-06-03', 'Ceco');

-- Inserimenti per l'entità Libro
INSERT INTO libro (id, titolo, anno_pubblicazione) VALUES (1, 'Il nome della rosa', 1980);
INSERT INTO libro (id, titolo, anno_pubblicazione) VALUES (2, 'Cento anni di solitudine', 1967);
INSERT INTO libro (id, titolo, anno_pubblicazione) VALUES (3, 'Harry Potter e la pietra filosofale', 1997);
INSERT INTO libro (id, titolo, anno_pubblicazione) VALUES (4, 'Guerra e pace', 1869);
INSERT INTO libro (id, titolo, anno_pubblicazione) VALUES (5, 'Orgoglio e pregiudizio', 1813);
INSERT INTO libro (id, titolo, anno_pubblicazione) VALUES (6, 'La metamorfosi', 1915);

-- Inserimenti per la relazione tra Autore e Libro
INSERT INTO libro_autori (libri_id, autori_id) VALUES (1, 1); -- Umberto Eco è l'autore di "Il nome della rosa"
INSERT INTO libro_autori (libri_id, autori_id) VALUES (2, 2); -- Gabriel García Márquez è l'autore di "Cent'anni di solitudine"
INSERT INTO libro_autori (libri_id, autori_id) VALUES (3, 3); -- J.K. Rowling è l'autrice di "Harry Potter e la pietra filosofale"
INSERT INTO libro_autori (libri_id, autori_id) VALUES (4, 4); -- Lev Tolstoj è l'autore di "Guerra e pace"
INSERT INTO libro_autori (libri_id, autori_id) VALUES (5, 5); -- Jane Austen è l'autrice di "Orgoglio e pregiudizio"
INSERT INTO libro_autori (libri_id, autori_id) VALUES (6, 6); -- Franz Kafka è l'autore di "La metamorfosi"
INSERT INTO libro_autori (libri_id, autori_id) VALUES (2, 4); -- Gabriel García Márquez e Lev Tolstoj collaborano in "Cent'anni di solitudine"
INSERT INTO libro_autori (libri_id, autori_id) VALUES (2, 6); -- Anche Franz Kafka contribuisce a "Cent'anni di solitudine"