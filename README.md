# Бизнес Логика Программных Систем

| Выполнил      | Группа | Преподаватель |
| :------------ | ------ | ------------- |
| Яковлев Г. А. | P33111 | Цопа Е. А.    |

## Как развернуть в WildFly

Проект был сконфигурирован так, что вы можете и дальше запускать приложение через IDEA `mvn spring-boot:run` (через встроенный Tomcat), но если необходимо запуститься в WildFly – вы прописываете `mvn wildfly:run`. Благодаря плагину загружается сервер приложений и деплоит ваш артефакт.

В третьей лабе можно сначала запустить сервер с деплоем поваренка `mvn wildfly:run -pl povarenok-application`, а затем задеплоить туда постер сервис `mvn wildfly:deploy -pl poster-service`.  

В четвертой лабе сорян, не работает, хз почему.

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

1. `USER` Доступ к данным требующих авторизации, изменение/удаление своих записей, лайки, книга, отзывы.
2. `ADMIN` Доступ к данным требующих авторизации, изменение/удаление **любых** записей, книга.
3. Неавторизованный пользователь имеет доступ к просмотру и регистрации.

Привелегии:

1. `LIKE` Возможность лайкать.
2. `COOKBOOK` Возможность вести книгу рецептов.
3. `REVIEWER` Возможность оставлять отзывы.
4. `CREATOR` Возможность создавать рецепты.
5. `MAINTAINER` Возможность обслуживать сервис (изменение/удаление любых записей).

## Лабораторная работа №3

**Вариант №7365**

Доработать приложение из лабораторной работы #2, реализовав в нём асинхронное выполнение задач с распределением бизнес-логики между несколькими вычислительными узлами и выполнением периодических операций с использованием планировщика задач.

**Требования к реализации асинхронной обработки:**

1. Перед выполнением работы необходимо согласовать с преподавателем набор прецедентов, в реализации которых целесообразно использование асинхронного распределённого выполнения задач. Если таких прецедентов использования в имеющейся бизнес-процесса нет, нужно согласовать реализацию новых прецедентов, доработав таким образом модель бизнес-процесса из лабораторной работы #1.
2. Асинхронное выполнение задач должно использовать модель доставки "очередь сообщений".
3. В качестве провайдера сервиса асинхронного обмена сообщениями необходимо использовать очередь сообщений на базе RabbitMQ.
4. Для отправки сообщений необходимо использовать протокол AMQP 0-9-1. Библиотеку для реализации отправки сообщений можно взять любую на выбор студента.
5. Для получения сообщений необходимо использовать слушателя сообщений JMS на базе Spring Boot (@JmsListener).

**Требования к реализации распределённой обработки:**

1. Обработка сообщений должна осуществляться на двух независимых друг от друга узлах сервера приложений.
2. Если логика сценария распределённой обработки предполагает транзакционность выполняемых операций, они должны быть включены в состав распределённой транзакции.

**Требования к реализации запуска периодических задач по расписанию:**

1. Согласовать с преподавателем прецедент или прецеденты, в рамках которых выглядит целесообразным использовать планировщик задач. Если такие прецеденты отсутствуют -- согласовать с преподавателем новые и добавить их в модель автоматизируемого бизнес-процесса.
2. Реализовать утверждённые прецеденты с использованием планировщика задач Quartz.

