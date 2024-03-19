# Бизнес Логика Программных Систем

| Выполнил      | Группа | Преподаватель |
| :------------ | ------ | ------------- |
| Яковлев Г. А. | P33111 | Цопа Е. А.    |

## Лабораторная работа №1

**Вариант №11115:** filmpro.ru - http://www.filmpro.ru/

Описать бизнес-процесс в соответствии с нотацией BPMN 2.0, после чего реализовать его в виде приложения на базе Spring Boot.

**Порядок выполнения работы:**

1. Выбрать один из бизнес-процессов, реализуемых сайтом из варианта задания.
2. Утвердить выбранный бизнес-процесс у преподавателя.
3. Специфицировать модель реализуемого бизнес-процесса в соответствии с требованиями BPMN 2.0.
4. Разработать приложение на базе Spring Boot, реализующее описанный на предыдущем шаге бизнес-процесс. Приложение должно использовать СУБД PostgreSQL для хранения данных, для всех публичных интерфейсов должны быть разработаны REST API.
5. Разработать набор curl-скриптов, либо набор запросов для REST клиента Insomnia для тестирования публичных интерфейсов разработанного программного модуля. Запросы Insomnia оформить в виде файла экспорта.
6. Развернуть разработанное приложение на сервере `helios`.

## Выполнение

![diagram](./docs/diagram.svg)

**Бизнес процесс** – создание, поиск, просмотр, оценка рецепта

**Swagger UI** находится по адресу http://localhost:8080/swagger-ui/index.html

## Лабораторная работа №2

**Вариант №8712**

Доработать приложение из лабораторной работы #1, реализовав в нём управление транзакциями и разграничение доступа к операциям бизнес-логики в соответствии с заданной политикой доступа.

**Управление транзакциями необходимо реализовать следующим образом:**

1. Переработать согласованные с преподавателем прецеденты (или по согласованию с ним разработать новые), объединив взаимозависимые операции в рамках транзакций.
2. Управление транзакциями необходимо реализовать с помощью Spring JTA.
3. В реализованных (или модифицированных) прецедентах необходимо использовать декларативное управление транзакциями.
4. В качестве менеджера транзакций необходимо использовать Atomikos.

**Разграничение доступа к операциям необходимо реализовать следующим образом:**

1. Разработать, специфицировать и согласовать с преподавателем набор привилегий, в соответствии с которыми будет разграничиваться доступ к операциям.
2. Специфицировать и согласовать с преподавателем набор ролей, осуществляющих доступ к операциям бизнес-логики приложения.
3. Реализовать разработанную модель разграничений доступа к операциям бизнес-логики на базе Spring Security. Информацию об учётных записах пользователей необходимо сохранять в реляционую базу данных, для аутентификации использовать HTTP basic.

## Выполнение

Транзакции повесил на чувствительные методы:

1. `likeRecipe` Для атомарного добавления лайка и обновления счетчика лайков у рецепта.
2. `createRecipe` Для одновременного добавления рецепта и шагов его приготовления.
3. И другие для автоматической инициализации и транзакционности.

Роли:

1. `USER` Доступ к данным требующих авторизации, изменение/удаление своих записей.
2. `ADMIN` Доступ к данным требующих авторизации, изменение/удаление **любых** записей.
3. Неавторизованный пользователь имеет доступ к просмотру и регистрации.
