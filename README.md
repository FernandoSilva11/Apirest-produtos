# Apirest-produtos

Olá, eu criei uma api simples com o tema de produtos. Sou um QA e não é comum ver o pessoal de teste se envolvendo assim com desenvolvimento, porem eu quis criar minha prórpia api para poder realizar testes e também entender melhor o funcionamento de uma API REST. 

Fiz o deploy da aplicação no heroku, e você pode acessar por esse link: [API REST Produtos](https://spring-restapi-produtos.herokuapp.com/swagger-ui.html)


Tecnologias utilizadas:

Java - Springbooot
Banco de dados - PostgreSQL

O projeto foi, em grande parte, baseado nesse pequeno curso onde a Michelli Brito ensina a criar a sua prórpia api: 
[Playlist - API REST - Michelli Brito](https://www.youtube.com/watch?v=bpBRFNKg8k4&list=PL8iIphQOyG-D2FP9wkg12AavzmVRWEcnJ).

Porem algumas modificações foram feitas, levando como exemplo a api "Gerenciamento de Viagens" apresentada pelo Julio de Lima no curso Treinamento Descomplicando Testes de API Rest, esse é o link para mais informações sobre o curso: [Treinamento Descomplicando Testes de API Rest](https://descomplicando.juliodelima.com.br/).

Modificações efetuadas:

1. Os métodos Put e Delete usam o id para alterar e excluir registros, em vez de acessar essa informação no body da requisição.

2. Foi utilizada a anotação "@ApiResponse" para retornar respostas específicas para cada médoto.

3. Foi adicionado um método de busca para tratar a exeção não encontrado, caso produto não esteja cadastrado no banco de dados.
