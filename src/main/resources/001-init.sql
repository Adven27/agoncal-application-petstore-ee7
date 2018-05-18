--liquibase formatted sql
--changeset me:001 context:prod

INSERT INTO transaction(id, amount, dt, category) VALUES (1001, 1000, CURRENT_DATE , 'cat 1');
INSERT INTO transaction(id, amount, dt, category) VALUES (1002, 200, CURRENT_DATE , 'cat 2');
INSERT INTO transaction(id, amount, dt, category) VALUES (1003, 220, CURRENT_DATE , 'cat 2');