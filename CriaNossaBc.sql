CREATE DATABASE `nossabc` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE `usuario` (
  `usuario_id` varchar(11) NOT NULL,
  `usuario` varchar(45) DEFAULT NULL,
  `senha` varchar(45) DEFAULT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `sobrenome` varchar(45) DEFAULT NULL,
  `matricula` varchar(45) DEFAULT NULL,
  `curso` varchar(45) DEFAULT NULL,
  `bio` varchar(144) DEFAULT NULL,
  `idade` int(11) DEFAULT NULL,
  `cpf` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`usuario_id`),
  UNIQUE KEY `usuario_id_UNIQUE` (`usuario_id`),
  UNIQUE KEY `cpf_UNIQUE` (`cpf`),
  UNIQUE KEY `matricula_UNIQUE` (`matricula`),
  UNIQUE KEY `usuario_UNIQUE` (`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `livro` (
  `livro_id` varchar(45) NOT NULL,
  `titulo` varchar(45) DEFAULT NULL,
  `chamada` varchar(45) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `edicao` varchar(45) DEFAULT NULL,
  `autor` varchar(45) DEFAULT NULL,
  `autor_secundario` varchar(45) DEFAULT NULL,
  `descricao_fisica` varchar(45) DEFAULT NULL,
  `titulo_principal` varchar(45) DEFAULT NULL,
  `publicacao` varchar(45) DEFAULT NULL,
  `numero_normalizado` varchar(45) DEFAULT NULL,
  `assuntos` varchar(45) DEFAULT NULL,
  `notas` varchar(144) DEFAULT NULL,
  `titulo_uniforme` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`livro_id`),
  UNIQUE KEY `livro_id_UNIQUE` (`livro_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `amizade` (
  `usuario1` varchar(11) NOT NULL,
  `usuario2` varchar(11) NOT NULL,
  PRIMARY KEY (`usuario1`,`usuario2`),
  KEY `usuario2_idx` (`usuario2`),
  CONSTRAINT `usuario1` FOREIGN KEY (`usuario1`) REFERENCES `usuario` (`usuario_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usuario2` FOREIGN KEY (`usuario2`) REFERENCES `usuario` (`usuario_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `comentario` (
  `comentario_id` int(11) NOT NULL,
  `comentario` varchar(1024) DEFAULT NULL,
  `livro_id` varchar(45) DEFAULT NULL,
  `usuario_id` varchar(45) DEFAULT NULL,
  `data` datetime DEFAULT NULL,
  PRIMARY KEY (`comentario_id`),
  UNIQUE KEY `comentario_id_UNIQUE` (`comentario_id`),
  KEY `livro_id_idx` (`livro_id`),
  KEY `usuario_id_idx` (`usuario_id`),
  CONSTRAINT `comentario_livro` FOREIGN KEY (`livro_id`) REFERENCES `livro` (`livro_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usuario_id` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`usuario_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `gostei` (
  `usuario_id` varchar(11) NOT NULL,
  `comentario_id` int(11) NOT NULL,
  PRIMARY KEY (`usuario_id`,`comentario_id`),
  KEY `comentario_id_idx` (`comentario_id`),
  CONSTRAINT `gostei_comentario` FOREIGN KEY (`comentario_id`) REFERENCES `comentario` (`comentario_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `gostei_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`usuario_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `jali` (
  `livro_id` varchar(45) NOT NULL,
  `usuario_id` varchar(11) NOT NULL,
  `data` datetime DEFAULT NULL,
  PRIMARY KEY (`livro_id`,`usuario_id`),
  KEY `jali_usuario_idx` (`usuario_id`),
  CONSTRAINT `jali_livro` FOREIGN KEY (`livro_id`) REFERENCES `livro` (`livro_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `jali_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`usuario_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `mensagem` (
  `mensagem_id` varchar(45) NOT NULL,
  `usuario1` varchar(11) NOT NULL,
  `usuario2` varchar(11) NOT NULL,
  `mensagem` varchar(1024) DEFAULT NULL,
  `data` datetime DEFAULT NULL,
  PRIMARY KEY (`mensagem_id`),
  KEY `mensagem_usuario1_idx` (`usuario1`),
  KEY `mensagem_usuario2_idx` (`usuario2`),
  CONSTRAINT `mensagem_usuario1` FOREIGN KEY (`usuario1`) REFERENCES `usuario` (`usuario_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `mensagem_usuario2` FOREIGN KEY (`usuario2`) REFERENCES `usuario` (`usuario_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `nota` (
  `livro_id` varchar(45) NOT NULL,
  `usuario_id` varchar(11) NOT NULL,
  `nota` int(11) DEFAULT NULL,
  PRIMARY KEY (`livro_id`,`usuario_id`),
  KEY `nota_usuario_idx` (`usuario_id`),
  CONSTRAINT `nota_livro` FOREIGN KEY (`livro_id`) REFERENCES `livro` (`livro_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `nota_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`usuario_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `pedido_amizade` (
  `solicitante` varchar(11) NOT NULL,
  `solicitado` varchar(11) NOT NULL,
  PRIMARY KEY (`solicitante`,`solicitado`),
  KEY `pedido_solicitado_idx` (`solicitado`),
  CONSTRAINT `pedido_solicitado` FOREIGN KEY (`solicitado`) REFERENCES `usuario` (`usuario_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `pedido_solicitante` FOREIGN KEY (`solicitante`) REFERENCES `usuario` (`usuario_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;