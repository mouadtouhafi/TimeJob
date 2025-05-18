INSERT INTO ROLES (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO ROLES (id, name) VALUES (2, 'ROLE_ADMIN');

ALTER TABLE WEBUSERS ALTER COLUMN PASSWORD VARCHAR(255);
INSERT INTO WEBUSERS (USERNAME, EMAIL, PASSWORD) VALUES ('mtouhafi', 'mouadthf@gmail.com', '$2a$12$y4ZFPW2Verr0Th8AlRoQWurvu22vf7TRW.hIky5zZwTooCLjpyJpG');
INSERT INTO WEBUSERS (USERNAME, EMAIL, PASSWORD) VALUES ('admin', 'mouadthf@gmail.com', '$2a$12$xjC7npcMzeWugNG3AbXmI.fNSBrzcb9JfbdLVAgqe1fFWnGMzSyNa');

-- Wire users to roles in the join table (user_roles)
INSERT INTO USER_ROLES (user_id, role_id) VALUES (1, 1); --Assign ADMIN to first WEBUSER
INSERT INTO USER_ROLES (user_id, role_id) VALUES (2, 2)