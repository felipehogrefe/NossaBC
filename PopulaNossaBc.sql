DELETE FROM jali;
DELETE FROM nota;
DELETE FROM gostei;
DELETE FROM mensagem;
DELETE FROM comentario;
DELETE FROM pedido_amizade;
DELETE FROM amizade;
DELETE FROM usuario;
DELETE FROM livro;

INSERT INTO usuario
	(usuario_id, usuario, senha, nome, sobrenome, matricula, curso, bio, idade, cpf)
VALUES 
	('101', '101','101', 'joao', 'silva', '100102', 'historia', 'alguma bio aqui', 21, 11122233344),
	('102', '102','102', 'maria', 'lima', '100309', 'matematica', 'alguma bio aqui', 18, 11022233344),
	('103', '103','103', 'jose', 'souza', '100701', 'letras', 'alguma bio aqui', 17, 11222233344),
	('104', '104','104', 'tereza', 'dia', '100503', 'fisica', 'alguma bio aqui', 20, 11322233344);
  
INSERT INTO livro
	(livro_id, titulo, chamada, status, edicao, autor)
VALUES
	('1001', 'Calculo 1', '10M1', 1, '5', 'James'),
	('1002', 'Calculo 1', '20M1', 0, '6', 'James'),
	('1011', 'Calculo 2', '10M2', 1, '5', 'James'),
	('1012', 'Calculo 2', '20M2', 0, '6', 'James'),
	('1101', 'Redes', '10C1', 1, '2', 'Kurose'),
	('1102', 'Redes', '20C1', 0, '3', 'Kurose'),
	('2001', 'Grafos', '30C1', 1, '1', 'Boaventura'),
	('2002', 'Grafos', '40C1', 0, '2', 'Boaventura'),
	('3001', 'Java como programar', '31C1', 1, '5', 'Deitel'),
	('3002', 'Java como programar', '41C1', 0, '6', 'Deitel');
    
INSERT INTO amizade
	(usuario1, usuario2)
VALUES
	('101','102'),
    ('101','103');
    
INSERT INTO comentario
	(comentario_id, comentario, livro_id, usuario_id,data)
VALUES
	(0, 'adoro esse livro', '1001', '101','2016-04-23 11:30:45'),
	(1, 'odeio esse livro', '1002', '102','2016-04-23 11:31:45'),
	(2, 'amo esse livro', '1011', '103','2016-04-23 11:32:45'),
	(3, 'esse livro e improtante', '1012', '104','2016-04-23 11:33:45'),
	(4, 'tem erros nesse livro', '1101', '101','2016-04-23 11:34:45'),
	(5, 'esse livro esta velho', '1102', '102','2016-04-23 12:30:45'),
	(6, 'o livro esta todo rasgado', '2001', '103','2016-04-23 13:30:45'),
	(7, 'deviam pedir mais exemplares', '2002', '104','2016-04-23 14:30:45'),
	(8, 'adoro java', '3001', '101','2016-04-23 15:30:45'),
	(9, 'java java java', '3002', '102','2016-04-23 16:30:45'),
	(10, 'calculo e dificil', '1001', '103','2016-04-23 17:30:45'),
	(11, 'quinta vez que pago calculo', '1001', '104','2016-04-23 18:30:45');
    
INSERT INTO gostei
	(usuario_id, comentario_id)
VALUES
	('101',1),
	('102',0),
	('103',0),
	('104',0),
	('101',11),
	('102',11),
	('103',11),
	('104',1);
    
INSERT INTO jali
	(livro_id, usuario_id, data)
VALUES
	('1001','101','2016-04-23 11:29:45'),
	('1002','102','2016-04-23 11:30:45'),
	('1011','103','2016-04-23 11:31:45'),
	('1012','104','2016-04-23 11:32:45'),
	('1101','101','2016-04-23 11:33:45'),
	('1102','102','2016-04-23 12:29:45'),
	('2001','103','2016-04-23 13:29:45'),
	('2002','104','2016-04-23 14:29:45'),
	('3001','101','2016-04-23 15:29:45'),
	('3002','102','2016-04-23 16:29:45'),
	('1001','103','2016-04-23 17:29:45'),
	('1001','104','2016-04-23 18:29:45');
    
INSERT INTO nota
	(livro_id, usuario_id, nota)
VALUES
	('1001', '101',5),
	('1002', '102',4),
	('1011', '103',3),
	('1012', '104',2),
	('1101', '101',1),
	('1102', '102',5),
	('2001', '103',4),
	('2002', '104',3),
	('3001', '101',2),
	('3002', '102',1),
	('1001', '103',5),
	('1001', '104',4);
    
INSERT INTO pedido_amizade
	(solicitante, solicitado)
VALUES
	('101','104'),
	('103','102');
    
INSERT INTO mensagem
	(mensagem_id, usuario1, usuario2, mensagem, data)
VALUES
	('0','101','102', 'OI','2016-04-22 11:29:45'),
	('1','102','101', 'ola','2016-04-22 12:46:12'),
	('2','101','102', 'td bem com vc?','2016-04-22 12:51:23'),
	('3','102','101', 'td bem, e vc?','2016-04-22 12:52:14'),
	('4','101','102', 'comigo tambem','2016-04-22 12:52:50');