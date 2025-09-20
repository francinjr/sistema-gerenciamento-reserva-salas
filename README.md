# Sistema de Gerenciamento de Reserva de Salas

Este projeto é uma aplicação web full-stack desenvolvida em Java com Spring Boot e JSP, criada como solução para um desafio prático. O sistema permite o gerenciamento de reservas de salas, atendendo a três perfis de usuários distintos: **Administradores**, **Recepcionistas** e **Clientes**.

## 1. Visão Geral e Funcionalidades Principais

O sistema foi projetado para ser uma plataforma completa para a reserva de salas, com um fluxo de trabalho claro e seguro.

### 1.1. Funcionalidades por Perfil de Usuário

* **Administrador (`ROLE_ADMINISTRADOR`)**
    * **Gestão de Entidades:** Realiza o CRUD (Criar, Ler, Atualizar, Deletar) completo para Setores, Salas e Recepcionistas.
    * **Configuração do Sistema:** Define a estrutura organizacional da empresa (quais setores existem, quais salas pertencem a cada um, etc.).
    * **Visualização de Relatórios(Histórico de agendamentos/Relatórios ao mesmo tempo):** Tem acesso a relatórios completos de faturamento de todos os setores.

* **Recepcionista (`ROLE_RECEPCIONISTA`)**
    * **Gestão Operacional do Setor:** Abre e fecha o seu setor, controlando a visibilidade das salas para os clientes(o cliente só vai visualizar o setor e suas salas se a recpecionista abrir o setor, quando ela fecha o cliente não consegue visualizar o setor e suas salas).
    * **Gestão de Agendamentos:** Visualiza as solicitações de agendamento dos clientes para o seu setor, podendo **confirmá-las** ou **recusá-las**.
    * **Finalização de Reservas:** Finaliza agendamentos confirmados após a utilização da sala pelo cliente, registando o pagamento final.
    * **Agendamento Instantâneo:** Realiza agendamentos diretos para clientes, sem a necessidade de uma solicitação prévia.
    * **Visualização de Relatórios(Histórico de agendamentos/Relatórios ao mesmo tempo):** Tem acesso a relatórios de faturamento do seu próprio setor.

* **Cliente (`ROLE_CLIENTE`)**
    * **Cadastro Livre:** Pode se cadastrar no sistema de forma autônoma.
    * **Visualização de Salas:** Vê uma lista de todos os setores **abertos** e suas respectivas salas disponíveis para reserva.
    * **Solicitação de Agendamento:** Pode solicitar o agendamento de uma sala para um período de tempo específico, com cálculo de valores em tempo real.
    * **Gestão de Solicitações:** Pode visualizar e cancelar suas próprias solicitações de agendamento pendentes.
    * **Histórico de Agendamentos(Histórico de agendamentos/Relatórios ao mesmo tempo):** Tem acesso a um histórico de todos os seus agendamentos passados (finalizados e cancelados).

---

## 2. Decisões de Arquitetura

O projeto foi construído sobre uma base sólida de princípios de design para garantir manutenibilidade, escalabilidade e clareza. A escolha de cada padrão e tecnologia visa a longevidade e a facilidade de evolução do software.

### 2.1. Arquitetura em Camadas com Domain-Driven Design (DDD)

A aplicação segue uma arquitetura em camadas bem definida, inspirada nos princípios do DDD, que coloca o domínio do negócio no centro do software.

* **Camada de Domínio (`domain`):** É o coração da aplicação.
    * **Entidades Ricas:** Classes como `Agendamento`, `Setor` e `PessoaFisica` não são apenas estruturas de dados anêmicas. Elas contêm a lógica de negócio e as regras de validação (invariantes) que governam seu estado. Por exemplo, a entidade `Agendamento` é a única responsável por saber como calcular seus próprios valores e como transitar entre os status (`SOLICITADO`, `CONFIRMADO`, etc.).
    * **Por que esta abordagem é melhor?** Esta abordagem de "domínio rico" garante que a lógica de negócio esteja encapsulada e coesa, evitando que regras de negócio se espalhem por outras camadas. Isso facilita a manutenção, reduz a repetição de código e torna o sistema mais resiliente a mudanças.

    * **Value Objects (VOs):** Conceitos do domínio que são definidos pelo seu valor, como `Dinheiro` e `Cpf`, são modelados como objetos imutáveis que se autovalidam no momento da sua criação.
    * **Por que esta abordagem é melhor?** Garante que nunca teremos um CPF com formato inválido ou um valor monetário inconsistente dentro do nosso domínio. O sistema de tipos do Java passa a trabalhar a nosso favor, aumentando a segurança, a robustez e a clareza do código.

* **Camada de Serviço (`services`):** Atua como um orquestrador da lógica de aplicação.
    * **Lógica de Aplicação:** Os serviços não contêm regras de negócio das entidades. A sua responsabilidade é orquestrar o fluxo de uma operação, coordenando as chamadas aos repositórios e a outros serviços. Por exemplo, o `RecepcionistaService`, no método `criar`, orquestra a criação de uma `PessoaFisica`, um `Usuario` e, finalmente, um `Recepcionista`, garantindo que tudo ocorra dentro de uma única transação atômica.
    * **Por que esta abordagem é melhor?** Mantém a camada de serviço enxuta e focada em casos de uso, deixando a lógica de domínio onde ela pertence. Isso facilita os testes e a reutilização da lógica de negócio.

* **Camada de Apresentação (`web`):** Lida com a interação com o usuário (HTTP e Views).
    * **DTOs (Data Transfer Objects):** Usamos DTOs como um "contrato" seguro entre o frontend e o backend. Isso desacopla a view das entidades de domínio.
    * **Por que esta abordagem é melhor?** Garante que apenas os dados necessários sejam trafegados, prevenindo a exposição de dados sensíveis e evitando problemas de performance com o carregamento de entidades complexas (como `LazyInitializationException`). A separação entre DTOs de leitura (`Buscar...Dto`) e escrita (`Salvar...Dto`) torna a intenção de cada operação explícita.
    * **Mappers:** Componentes dedicados que têm a responsabilidade única de converter Entidades para DTOs, mantendo os serviços e controllers limpos e alinhados com o Princípio da Responsabilidade Única.
    * **Controllers:** Seguem o padrão MVC, recebendo requisições HTTP, validando os DTOs de entrada, chamando os serviços e selecionando a view apropriada para a resposta.

### 2.2. Versionamento de Banco de Dados com Flyway

Utilizei o **Flyway** para gerir todas as alterações no esquema do banco de dados.
* **Por que esta abordagem é melhor?** Garante que o banco de dados seja uma fonte de verdade consistente e versionada. Qualquer desenvolvedor pode recriar o banco de dados do zero, em qualquer estado, apenas executando as migrations. Isso elimina problemas de inconsistência entre ambientes de desenvolvimento e produção e torna o deploy da aplicação um processo mais seguro e automatizado.
* **Implementação:** Todos os `CREATE TABLE`, `ALTER TABLE` e a criação de `FUNCTIONS` e `TRIGGERS` são definidos em scripts SQL numerados na pasta `resources/db/migration`.

### 2.3. Segurança com Spring Security

A segurança é gerenciada de forma centralizada pelo Spring Security, controlando tanto a **autenticação** (quem você é) quanto a **autorização** (o que você pode fazer).
* **Por que esta abordagem é melhor?** O Spring Security é um framework robusto e padrão de mercado que oferece proteção contra as principais vulnerabilidades web (como CSRF). Centralizar a configuração de segurança num único local (`SecurityConfig`) torna a aplicação mais segura e fácil de auditar.
* **Implementação:** O `UsuarioService` implementa a interface `UserDetailsService` para se integrar ao Spring Security. As senhas são armazenadas de forma segura usando o algoritmo **BCrypt**. A autorização é feita tanto a nível de rotas (no `SecurityConfig`) quanto a nível de interface (com a Spring Security Tag Library no JSP), garantindo uma experiência de usuário segura e intuitiva.

---

## 3. Distribuição da Lógica de Negócio (Aplicação vs. Banco de Dados)

Seguindo os requisitos do desafio, a lógica de negócio foi estrategicamente distribuída entre a aplicação Java e o banco de dados PostgreSQL para obter o melhor de ambos os mundos.

### 3.1. Regras de Negócio na Aplicação (Java)

A aplicação é responsável pela **lógica de domínio rica e pela orquestração do fluxo de trabalho**.

* **Validação de Invariantes de Entidades:** A garantia de que um objeto de domínio nunca esteja num estado inválido reside nas próprias entidades (ex: um agendamento não pode ter uma data de fim anterior à de início).
* **Validação de Formato de Value Objects:** A lógica para validar se um CPF ou E-mail tem um formato correto está encapsulada dentro dos seus respectivos VOs.
* **Validações das entidades e regras de domínio usando Java(sem anotações como do jakarta para determinadas validações), para garantir um dominio mais puro, que não sofre com mudanças de tecnologia e que segue de fato as regras do dominio da empresa(nos dtos as anotações do jakarta para validações de formatos são usadas, porque os dtos não precisam ser puros, e eles também carregam uma responsabilidade de servir como forma de falhar rápido no momento de requisições, para que não seja necessário ir para a camada de dominio desncessariamente, caso formatos de dados estejam inválidos).
* **Orquestração de Processos:** A camada de serviço orquestra processos complexos que envolvem múltiplas entidades, como o cancelamento de solicitações conflitantes após a confirmação de uma delas(quando tem várias solicitações de agendamento para um mesmo horário, o cliente que pagar primeiro terá seu agendamento confirmado pela recepcionista, e quando isso acontece, todas as outras solicitações conflitantes com esse horário são canceladas automáticamente, isso é feito no serviço pois envolve operações com multiplas mudanças de registros na base de dados, então para ter uma regra de negócio bem escrita e fácil de entender, isso é feito no serviço).
* **Controle de Acesso (Autorização):** A lógica que define que um `CLIENTE` não pode aceder ao painel da recepcionista é controlada pelo Spring Security na aplicação.

### 3.2. Regras de Negócio no Banco de Dados (PostgreSQL)

O banco de dados é a **fonte da verdade final** e garante a integridade dos dados e as regras de negócio mais críticas, que não podem ser contornadas.

* **Garantia de Unicidade:** Constraints `UNIQUE` (ex: `setores_nome_uk`, `pessoas_fisicas_cpf_uk`) garantem que não existam dados duplicados. Esta é a forma mais segura e performática de impor a unicidade, existem vários campos únicos na aplicação. Esse tipo de regra de negócio é feita no banco de dados pois envolve sua própria infrastrutura e o banco de dados tem acesso facilmente aos seus registros, garantindo a aplicação correta da regra de negócio. Além disso os controladores conseguem pegar adequadamente o contexto da exceção DataIntegrityViolationException do banco de daods, para apresentar um eror com uma mensagem de erro adequada ao usuário(Existe na aplicação um DataIntegrityViolationTradutor que pega o erro de validaçaõ de campo unico do banco de dados e pega a mensagem adequada para mostrar na tela para o usuário).
* **Validação de Conflito de Horários:** Esta é a regra mais complexa no banco. Usamos uma **Função** e um **Gatilho (Trigger)** no PostgreSQL para verificar, antes de cada `INSERT` ou `UPDATE` na tabela `tb_agendamentos`, se um novo agendamento `CONFIRMADO` conflita com outro já existente. Se houver conflito, o próprio banco lança uma exceção, impedindo a operação e garantindo a integridade do calendário de reservas.
* **Cálculo Acumulativo do Caixa:** Também foi implementada uma **Função** e um **Gatilho** para automatizar a atualização do caixa de cada sala. Quando um agendamento é `CONFIRMADO` ou `FINALIZADO`, o gatilho é disparado e atualiza automaticamente o valor do caixa na tabela `tb_salas`, garantindo que este cálculo seja sempre consistente e atômico.

---

## 4. Instruções de Setup e Execução

Siga os passos abaixo para configurar e executar o projeto no seu ambiente local.

### 4.1. Pré-requisitos

* **JDK 21** ou superior.
* **Apache Maven** 3.8 ou superior.
* **PostgreSQL** 14 ou superior.
* Uma IDE Java, como o **IntelliJ IDEA**.

### 4.2. Configuração do Banco de Dados

1.  Crie um novo banco de dados no seu PostgreSQL com o nome: `sistema_gerenciamento_reserva_salas`.
2.  Abra o ficheiro `src/main/resources/application.properties`.
3.  Ajuste as seguintes propriedades com as suas credenciais do PostgreSQL:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/sistema_gerenciamento_reserva_salas
    spring.datasource.username=seu_usuario_postgres
    spring.datasource.password=sua_senha_postgres
    ```

### 4.3. Build e Execução

1.  Abra o projeto na sua IDE.
2.  Execute um build do Maven para descarregar todas as dependências. No IntelliJ, pode usar o painel do Maven e clicar em `Lifecycle -> install`.
3.  Encontre a classe `SistemaGerenciamentoReservaSalasApplication.java` e execute o método `main()`.
4.  O Spring Boot irá iniciar a aplicação e o **Flyway executará automaticamente todas as migrations** do banco de dados, criando as tabelas e inserindo os dados iniciais.
5.  Aceda à aplicação no seu navegador: `http://localhost:8080`.

### 4.4. Credenciais Iniciais

A aplicação é iniciada com um utilizador **Administrador** padrão para que você possa começar a configurar o sistema.

* **Email:** `admin@email.com`
* **Senha:** `admin`
