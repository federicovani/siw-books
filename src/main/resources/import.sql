-- Inserimenti per l'entità Autore
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazionalita) VALUES (10000, 'Umberto', 'Eco', '1932-01-05', '2016-02-19', 'Italiano');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazionalita) VALUES (10001, 'Gabriel', 'García Márquez', '1927-03-06', '2014-04-17', 'Colombiano');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazionalita) VALUES (10002, 'J.K.', 'Rowling', '1965-07-31', NULL, 'Britannico');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazionalita) VALUES (10003, 'Lev', 'Tolstoj', '1828-09-09', '1910-11-20', 'Russo');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazionalita) VALUES (10004, 'Jane', 'Austen', '1775-12-16', '1817-07-18', 'Inglese');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazionalita) VALUES (10005, 'Franz', 'Kafka', '1883-07-03', '1924-06-03', 'Ceco');

-- Inserimenti per l'entità Libro
INSERT INTO libro (id, titolo, anno_pubblicazione) VALUES (10000, 'Il nome della rosa', 1980);
INSERT INTO libro (id, titolo, anno_pubblicazione) VALUES (10001, 'Cento anni di solitudine', 1967);
INSERT INTO libro (id, titolo, anno_pubblicazione) VALUES (10002, 'Harry Potter e la pietra filosofale', 1997);
INSERT INTO libro (id, titolo, anno_pubblicazione) VALUES (10003, 'Guerra e pace', 1869);
INSERT INTO libro (id, titolo, anno_pubblicazione) VALUES (10004, 'Orgoglio e pregiudizio', 1813);
INSERT INTO libro (id, titolo, anno_pubblicazione) VALUES (10005, 'La metamorfosi', 1915);

-- Inserimenti per la relazione tra Autore e Libro
INSERT INTO libro_autori (libro_id, autore_id) VALUES (10000, 10000);
INSERT INTO libro_autori (libro_id, autore_id) VALUES (10001, 10001);
INSERT INTO libro_autori (libro_id, autore_id) VALUES (10002, 10002);
INSERT INTO libro_autori (libro_id, autore_id) VALUES (10003, 10003);
INSERT INTO libro_autori (libro_id, autore_id) VALUES (10004, 10004);
INSERT INTO libro_autori (libro_id, autore_id) VALUES (10005, 10005);

-- Inserimenti per l'entità Recensione
INSERT INTO recensione (id, titolo, voto, testo, libro_id) VALUES (10000, 'Bellissimo!', 5, 'Un capolavoro della letteratura moderna.', 10000);
INSERT INTO recensione (id, titolo, voto, testo, libro_id) VALUES (10001, 'Noioso.', 2, 'Troppo lungo e pesante da seguire.', 10001);
INSERT INTO recensione (id, titolo, voto, testo, libro_id) VALUES (10002, 'Fantastico!', 5, 'Un libro che mi ha stregato dall’inizio alla fine.', 10002);
INSERT INTO recensione (id, titolo, voto, testo, libro_id) VALUES (10003, 'Classico intramontabile', 4, 'Un po’ complesso ma merita la lettura.', 10003);
INSERT INTO recensione (id, titolo, voto, testo, libro_id) VALUES (10004, 'Un capolavoro per l\epoca', 4, 'Molto interessante.', 10004);
INSERT INTO recensione (id, titolo, voto, testo, libro_id) VALUES (10005, 'Particolare', 3, 'Una storia surreale e unica.', 10005);