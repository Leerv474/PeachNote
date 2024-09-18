# Описание целостности данных

_Пользователь_:

- логин: тип данных — varchar(128), должен быть уникальным и не пустым;
- пароль: тип данных — varchar(255), должен быть не пустым;
- номер телефона: тип данных — varchar(16).

_Задача_:

- название: тип данных — varchar(128), не должно быть пустым;
- описание: тип данных — text;
- срок: тип данных — date;
- дата создания: тип данных — date, автоматически заполняется текущей датой при создании;
- дата выполнения: тип данных — date, автоматически заполняется текущей датой при изменении статуса на "выполнено";
- приоритет: тип данных — integer, больше нуля и не больше 100.

_Проект_:

- название: тип данных — varchar(128), не должно быть пустым;
- описание: тип данных — text;
- срок: тип данных — date.

  > !ПРИМЕЧАНИЕ  
  > Узнать дату начала или конца выполнения проекта можно через первую созданную или последнюю выполненную задачу, принадлежащую проекту.

_Статус-таблица_:

- название: тип данных — varchar(128), не должно быть пустым;
- номер этапа выполнения: тип данных — integer, больше нуля и не больше 20.

_Доска_:

- название: тип данных — varchar(128), не должно быть пустым.

  > !ПРИМЕЧАНИЕ  
  > Максимальное количество статус-таблиц в одной доске не превышает 20.