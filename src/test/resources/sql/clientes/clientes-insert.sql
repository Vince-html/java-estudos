insert into USUARIOS (id, username, email, password, role) values (1000, 'Vicente', 'vicente.maga@live.com', '$2a$12$KMde.JuPeLBlaMOJeUfnp.7yC4FYVMOX5gKYo865XaEPWxsTKnOvi', 'ROLE_ADMIN');
insert into USUARIOS (id, username, email, password, role) values (1001, 'Bia', 'bia.maga@live.com', '$2a$12$KMde.JuPeLBlaMOJeUfnp.7yC4FYVMOX5gKYo865XaEPWxsTKnOvi', 'ROLE_ADMIN');
insert into USUARIOS (id, username, email, password, role) values (1002, 'Bob', 'bob.maga@live.com', '$2a$12$KMde.JuPeLBlaMOJeUfnp.7yC4FYVMOX5gKYo865XaEPWxsTKnOvi', 'ROLE_CLIENTE');
insert into USUARIOS (id, username, email, password, role) values (10, 'JohnDevolta', 'john_devolta@live.com', '$2a$12$KMde.JuPeLBlaMOJeUfnp.7yC4FYVMOX5gKYo865XaEPWxsTKnOvi', 'ROLE_CLIENTE');


insert into CLIENTES (id, nome, cpf, id_usuario) values (10, 'Bob Bertioga', '56637623024', 1002);
