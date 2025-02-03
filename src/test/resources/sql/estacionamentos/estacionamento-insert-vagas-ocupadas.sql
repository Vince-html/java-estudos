insert into USUARIOS (id, username, email, password, role) values (1000, 'Vicente', 'vicente.maga@live.com', '$2a$12$KMde.JuPeLBlaMOJeUfnp.7yC4FYVMOX5gKYo865XaEPWxsTKnOvi', 'ROLE_ADMIN');
insert into USUARIOS (id, username, email, password, role) values (1001, 'Bia', 'bia.maga@live.com', '$2a$12$KMde.JuPeLBlaMOJeUfnp.7yC4FYVMOX5gKYo865XaEPWxsTKnOvi', 'ROLE_ADMIN');
insert into USUARIOS (id, username, email, password, role) values (1002, 'Bob', 'bob.maga@live.com', '$2a$12$KMde.JuPeLBlaMOJeUfnp.7yC4FYVMOX5gKYo865XaEPWxsTKnOvi', 'ROLE_CLIENTE');
insert into USUARIOS (id, username, email, password, role) values (10, 'JohnDevolta', 'john_devolta@live.com', '$2a$12$KMde.JuPeLBlaMOJeUfnp.7yC4FYVMOX5gKYo865XaEPWxsTKnOvi', 'ROLE_CLIENTE');



insert into CLIENTES (id, nome, cpf, id_usuario) values (21, 'Biatriz Rodrigues', '09191773016', 10);
insert into CLIENTES (id, nome, cpf, id_usuario) values (22, 'Rodrigo Silva', '98401203015', 1002);

insert into vagas (id, codigo, status) values (100, 'A-01', 'OCUPADO');
insert into vagas (id, codigo, status) values (200, 'A-02', 'OCUPADO');
insert into vagas (id, codigo, status) values (300, 'A-03', 'OCUPADO');
insert into vagas (id, codigo, status) values (400, 'A-04', 'OCUPADO');
insert into vagas (id, codigo, status) values (500, 'A-05', 'OCUPADO');

insert into clientes_tem_vaga (recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20230313-101300', 'FIT-1010', 'FIAT', 'PALIO', 'VERDE', '2025-02-01 10:15:00', 22, 100);
insert into clientes_tem_vaga (recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'BRANCO', '2023-03-14 10:15:00', 21, 200);
insert into clientes_tem_vaga (recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20230315-101500', 'FIT-1030', 'FIAT', 'PALIO', 'VERDE', '2023-03-14 10:15:00', 22, 300);
insert into clientes_tem_vaga (recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20230316-101600', 'SIE-1040', 'FIAT', 'SIENA', 'VERDE', '2023-03-14 10:15:00', 21, 400);
insert into clientes_tem_vaga (recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20230317-101700', 'SIE-1050', 'FIAT', 'SIENA', 'VERDE', '2023-03-14 10:15:00', 22, 500);