# Приложение для составления заметок и расписания по системе "Getting Things Done"

Название: **PeachNote**

Цель: разработать и реализовать систему заметок на основе методологии "Getting Things Done", использование которой обеспечивает структурированное управление задачами человека или группы людей.

## Описание предметной области

Система заметок работает на основе методологии "Getting Things Done", которая разбивает задачи по сложности, важности и условиям выполнения. Элементы системы "Getting Things Done", разработанной Дэвидом Алленом, предусматривают несколько "куч" задач с различными характеристиками, описанными ниже:

1. Задачи — действия и цели пользователя.
2. Проекты — задачи, для выполнения которых требуется несколько шагов.
3. Контексты — условия, при которых эти задачи могут быть выполнены.
4. Справочные материалы.
5. Календарь.
6. Списки ожидания — задачи, выполнение которых зависит от внешних факторов.

### Описание методологии GTD

Применение методологии состоит из 4 шагов:  
Запись → Обработка → Пересмотр → Выполнение.

Обработка заключается в присвоении категории задачам по следующим вопросам:

1. Является ли это задачей?
2. Эта задача одношаговая?
3. Если это задача, зависит ли она от внешних факторов?
4. Эта задача неотложная?

Следуя этим вопросам, задачи распределяются по нескольким категориям:

1. Корзина — изначальный список задач.
2. Проекты — список проектов.
3. Отложенные задачи — задачи, не требующие немедленного выполнения.
4. Текущие задачи — задачи, выполнение которых началось.
5. Ожидание — задачи, зависящие от внешних факторов.
6. Выполненные задачи.

## Описание требований к системе

### Понятия системы

- _Система заметок GTD_ — система заметок на основе методологии "Getting Things Done", разработанной Дэвидом Алленом, предназначенная для структурированного контроля выполнения задач.
- _Пользователь_ — любое зарегистрированное лицо, которое пользуется данной системой.
  > Атрибуты: имя пользователя, пароль, электронная почта, статус активности аккаунта, статус подтверждения аккаунта, Telegram тег. Telegram тег - необязательное поле.
- _Роль_ - роль пользователя в доске, от которой зависит его уровень доступа.
  > Атрибуты: название, описание, уровень доступа, статус активности.
- _Задача_ — любая заметка, добавленная пользователем для дальнейшего распределения и выполнения.
  > Атрибуты: название, описание, срок, дата создания, дата выполнения, приоритет. Срок и описание условий выполнения — необязательные поля.
- _Проект_ — это задача, на выполнение которой требуется несколько шагов (подзадач).
  > Атрибуты: название, описание, срок. Срок и описание — необязательные поля.
- _Статус-таблица_ — это список, в который заносятся задачи и проекты в зависимости от статуса их выполнения.
  > Атрибуты: название, номер этапа выполнения.
- _Доска_ — это хранилище статус-таблиц, в рамках которого создаются задачи и проекты.
  > Атрибуты: название.

_Проекты_ состоят из _Задач_ (один ко многим).  
_Статус-таблицы_ состоят из _Задач_ и _Проектов_ (один ко многим).  
_Доски_ состоят из _Статус-таблиц_ (один ко многим).  
_Доски_ принадлежат _Пользователям_ (многие к одному).

### Функции системы

Общие функции:
- авторизация пользователя;
- регистрация пользователя;
- подтверждение почты;
- редактирование профиля пользователя;
- отключение аккаунта пользователя.
- удаление пользователя.
- создание досок пользователя;
- создание дополнительных статус-таблиц;
- создание задач, проектов, статус-таблиц и досок;
- редактирование свойств задач, проектов, статус-таблиц и досок;
- преобразование задач в проекты;
- изменение статуса задач и проектов;
- добавление доступа на просмотр досок;

Реализация функционала:

В досках по умолчанию присутствуют 5 статус-таблиц: Проекты, Отложенные задачи, Текущие задачи, Ожидание, Выполнено. При этом можно добавить дополнительные _статус-таблицы_ под нужды пользователя. Дополнительные _статус-таблицы_ воспринимаются как стадии выполнения задач и не могут служить изначальным статусом.
Отдельно от досок существует _Корзина_ - список выгруженных задач.

Созданные _задачи_ изначально попадают в _Корзину_. Присвоить _статус_ можно либо при создании, либо после. Присвоение _статуса_ осуществляется посредством вопросов и соответствующих кнопок согласно [Описанию методологии GTD](#описание-методологии-gtd):

```
|--> Является ли это задачей?
|  |--> ДА  -> следующий вопрос
|  |--> НЕТ -> Удалить?
|           |--> ДА
|           |--> НЕТ -> остается в корзине
|
|--> Эта задача одношаговая?
|  |--> ДА  -> следующий вопрос
|  |--> НЕТ -> преобразование в проект
|
|--> Эта задача полностью зависит от внешних факторов?
|  |--> ДА  -> задача попадает в доску ожидания
|  |--> НЕТ -> следующий вопрос
|
|--> Эта задача неотложная?
|  |--> ДА  -> задача попадает в текущие задачи
|  |--> НЕТ -> задача попадает в отложенные задачи
```

В окне распределения присутствуют кнопки выбора _статус-таблицы_ и  _доски_.

Проекты не могут находиться в _статус-таблицах_ Отложенные задачи, Текущие задачи и Ожидание. Следовательно, все пункты, содержащиеся в перечисленных досках, являются задачами.

Существуют расширенные виды статус-таблиц, задач и проектов, в которых отображается более подробная информация.

#### Функционал задач

Окно создания задачи содержит:
- редактирование атрибутов;
- кнопку создания в корзину;
- кнопку создания;
- кнопку распределения в зависимости от вида задачи (проектная или самостоятельная).

Расширенный вид задач содержит:
- функции редактирования атрибутов;
- кнопку удаления;
- кнопку преобразования в проект;
- кнопка перемещения на другую доску.

Перенос задач по статус-таблицам осуществляется нажатием кнопки (->) или перетягиванием курсором. Нажатие кнопки (->) имеет следующий функционал:
- Если задача находится в Корзине, начинается ее распределение.
- Если задача находится в Отложенных задачах, она либо попадает в Текущие задачи, либо преобразуется в Проект.
- Если задача находится в Ожидании, она попадает в Выполнено.
- Если задача находится в Текущих задачах, она попадает в Выполнено или пользовательский этап выполнения, если таковой был создан.

Приоритет задачи влияет на ее расположение в доске по вертикали. Если у задачи нет срока, приоритет остается статичным. В противном случае, приоритет будет расти с приближением срока. Например: изначально остается месяц — приоритет 1, остается 2 недели — приоритет 3, остается неделя — приоритет 5, остается день — приоритет 10. Рост приоритета происходит процентно в зависимости от времени до срока и начинается с изначального приоритета. Если приоритет поднялся до 10, задача подсвечивается, сигнализируя о приближающемся сроке.

Задачу можно в любой момент преобразовать в проект. Преобразование переносит условия в описание проекта, и появляется возможность создавать задачи, связанные с данным проектом.

#### Функционал проектов

Расширенный вид проектов содержит:
- список задач с отображением их статуса;
- функции редактирования атрибутов.

Проекты могут находиться только в двух статус-таблицах: _Проекты_ и _Выполнено_, так как их статус выполнения

 зависит от задач, из которых они состоят. Связанные задачи попадают в _Текущие задачи_. По мере их выполнения шкала выполнения проекта будет расти. Если все задачи, связанные с проектом, выполнены, они перестают отображаться в доске _Выполнено_, и вместо них в _Выполнено_ попадает сам проект. Если какая-либо задача проекта подсвечена, проект также подсвечивается.

#### Функционал статус-таблиц

Расширенный вид статус-таблиц содержит:
- список задач с отображением их приоритета и срока;
- функции редактирования атрибутов.

Для изменения порядка пользовательских статус-таблиц необходимо редактировать таблицу.

#### Функционал досок

Расширенный вид досок содержит:
- редактор дополнительных _статус-таблиц_;
- кнопку удаления доски;
- добавление пользователей для просмотра;
- функции редактирования атрибутов.

Функция поделиться просмотром:
Пользователи, не являющиеся владельцами доски, могут просматривать ее содержание без возможности редактирования.
В настройках доски присутсвует функция добавления пользователей на просмотр по _имени пользователя_.
У приглашенного пользователя, принявшего приглашение, гостевая доска появляется в общем списке досок.

Удаление доски:
Удаление требует потверждения действия посредством ввода названия доски в поле подтверждения.


#### Функционал пользователя

- функции редактирования атрибутов;
- Telegram-оповещения.

Поле _номер телефона_ не является обязательным, но в случае подтверждения Telegram-бот будет оповещать пользователя в личных сообщениях.

## Заключение

Таким образом, конечный продукт представляет собой систему заметок, в которой реализовано распределение задач по перечисленным критериям и отслеживание их выполнения.
