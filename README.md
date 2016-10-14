# NossaBC
Projeto de rede social para sistemas de biblioteca.

Descrição do projeto

O projeto consiste em um aplicativo para Android com a finalidade de integrar as
funções de um sistema de biblioteca com funcionalidades de uma rede social. Este 
foi requisito avaliativo das disciplinas Metodologia e Processos e Redes de 
Computadores 1.

Para o sistema de bibliotecas tem-se por objetivo implementar as funções de 
consulta ao acervo, reserva de livro, renovação de empréstimo, além de funcionalidades 
extras como um quadro de avisos informando data do próximo livro a vencer.

Já a parte da rede social foi baseada no Facebook, e no Filmow, ou seja, os usuários 
interagem diretamente entre sim, através de mensagens ou adicionando amigos, e também 
interagem através dos portfólios de livros (retornados pelas consultas, por exemplo), 
nesse último caso a interação acontece via comentários sobre o livro em questão, 
sendo possível curtir os comentários e também dar nota aos livros.

Como o projeto foi elaborado também para uma disciplina de redes de computadores a
conectividade do aplicativo foi implementada com o uso de sockets, o que acabou deixando 
o sistema bastante deselegante e também ineficiente. Com o uso de sockets além do 
aplicativo Android foi necessário implementar um servidor, o qual garante acesso aos 
dados, estes por sua vez foram estruturados utilizando MySQL. Toda a comunicação se 
dá seguindo o seguinte padrão: mensagens compostas por identificador de função, número 
de 3 dígitos, e corpo da mensagem, uma vez que o servidor tenha identificado a função
a ser executada irá interpretar o corpo da mensagem de várias formas diferentes, seja 
por meio de JSON seja por meio de substrings.

O banco de dados foi preenchido com a imagem do banco de dados da biblioteca do Pólo 
de Santana do Ipanema, uma vez que não foi garantido acesso direto a qualquer sistema
da biblioteca.

Por se tratar de um projeto de disciplina alguns aspectos foram tratados como secundários, 
o mais notável deles foi a interface do programa. Por ser uma área bastante desconhecida 
ao desenvolvedor optou-se por focar em questões mais importantes para o funcionamento 
geral do aplicativo.
