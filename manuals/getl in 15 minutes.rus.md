# О Getl
Groovy ETL (Getl) - open source проект на Groovy, разработанный в 2012 году для автоматизации загрузки и обработки 
данных с разных источников в хранилища данных Vertica. 

Исходный код проекта располагается на [GitHub](https://github.com/ascrus/getl).

Русскоязычная документация располагается на [GitHub Pages](https://github.com/ascrus/getl). 

Изначально софт писался как замена Talend и других классических ETL для проектов, где требовалось быстро и много загружать
данные из разнообразных файловых источников с причудливыми форматами, с плавающей структурой, известной только на
момент захвата данных: ASN.1, CSV, XML, Json и т.д. с вложениями других форматов внутрь и прочими прелестями работы с данными
при автоматизации в телекоммуникационной отрасли. Помимо этого Getl решал задачу 
быстрой разработки пилотных проектов: считать структуру с РСУБД источников данных, создать таблицы с нужным типом полей
в ХД на Vertica, перелить данные в них с исходных таблиц, обеспечить расчеты аналитических витрин.
 
За последующие восемь лет Getl научился работать с инкрементальным захватом данных, с группами файлов на разных файловых 
системах, стал поддерживать язык хранимых процедур для разработки скриптов под Vertica и обзавелся 
и собственным специализированным DSL языком для работы с данными.

## Назначение
Getl полезен, если Вы разрабатываете проекты хранилищ данных, немного знаете Java или Groovy и Вам требуется:
* Скопировать данные между разными JDBC и файловыми источниками, не указывая маппинг структур или 
не имея возможности его описать из-за плавающей структуры данных;
* Сделать инкрементальный захват данных с источника и доставку порции данных в хранилище данных;
* Захватить по заданным условиям во вложенных директориях множество файлов и скопировать/распарсить их 
в указанный источник;
* Сделать быстрый пилот с переносом множества таблиц и их данных из источников в ХД;
* Упростить разработку классов тестирования процессов работы с данных и файлами.    

## Требования
1. Нужно чуть изучить Groovy, например на Хабре: [Groovy за 15 минут](http://habrahabr.ru/post/122127/).
1. Изучить любой IDE, который поддерживает Java и Groovy, например 
[JetBrains IntelliJ IDEA](https://www.jetbrains.com/ru-ru/idea/documentation/) или 
[Eclipse](https://www.ibm.com/developerworks/ru/library/os-eclipse-platform/).
 
# Подключение к Gradle проекту
Getl выкладывается в центральном Maven, поэтому в build.gradle достаточно прописать ссылку на него в разделе _dependencies_:
```groovy
dependencies {
    compile(group: 'net.sourceforge.getl', name: 'getl', version:'4.4.2')
}
``` 
 
# Использование в классах и скриптах Groovy
DSL язык Getl можно использовать внутри классов Groovy с помощью вызова статической функции Dsl:
```groovy
package demo

import getl.lang.Getl
  
class GroovyApp {
    static void main(def args) {
      helloWorld()
    }

    void helloWorld() {
        Getl.Dsl {
            logInfo 'Привет мир!'
        }
    }
}
```
Вызова программы с командной строки такой же, как для любого Java приложения. Проект компилируем в jar файл с 
помощью Gradle и вызываем:
```shell script
java -cp myproj.jar demo.GroovyApp
```
P.S. Не забудьте в class path также добавить пути к jar файлам Getl и используемых Jdbc драйверов.

Если Вам не требуется усложнять проект классами, то проще разрабатывать сценарии с помощью скриптов Groovy:
```groovy
// Файл GetlDemo.groovy
import groovy.transform.BaseScript
import getl.lang.Getl

@BaseScript Getl main

logInfo 'Привет мир!'
``` 
Аннотация _BaseScript_ указывает Groovy, что этот скрипт будет выполняться c DSL расширением Getl. Переменная main при
работе скрипта будет содержать ссылку на экземпляр самого объекта Getl, под которым был запущен скрипт. 

Для того, чтобы вызывать скрипты с командной строки Groovy или Groovy Shell потребуется скопировать по пути инсталляции 
Groovy в _libs_ jar файлы Getl и требуемые для работы дополнительные библиотеки, например JDBC драйвера:
```shell script
groovy GetlDemo.groovy
```

Если требуется вести разработку проекта с множеством скриптов, классов, ресурсными файлами с конфигурациями и SQL
скриптами, то лучше это делать в IDE, разделяя все по пакетам, компилировать в jar файл и запускать нужный скрипт проекта
с помощью класса Getl:
```shell script
java -cp myproj.jar getl.lang.Getl runclass=demo.GetlDemo
``` 
P.S. Не забудьте в class path также добавить пути к jar файлам Getl и используемых Jdbc драйверов.

# Описание объектов источников
В Getl поддерживаются следующие виды источников:
* JDBC источники: DB2, Firebird, H2, Hive, Impala, MS SQLServer, MySQL, IBM Netezza, NetSuite, Oracle, PostgreSQL, Vertica;
* Файловые форматы: CSV, Excel, Json, XML, Yaml;
* Облачные источники: SalesForce;
* Файловые системы: Локальные, FTP, SFTP, HDFS. 

## Описание соединения к JDBC источнику
Для создания соединения к JDBC источнику достаточно вызвать функцию с нужным именем типа соединения и указать ему 
параметры подключения:
```groovy
def ora = oracleConnection {
    connectHost = 'server'
    connectDatabase = 'sample'
    login = 'user'
    password = 'password'
}
def imp = impalaConnection {
    connectHost = 'server'
    connectDatabase = 'sample'
    login = 'user'
}
def ver = verticaConnection {
    connectHost = 'server-node-1'
    connectDatabase = 'sample'
    login = 'user'
    password = 'password'
}
```
В локальной переменной будет ссылка на объект управления нужным соединением. Для соединения можно выполнять 
SQL команды, в том числе внутри транзакций:
```groovy
ora.with {
    transactional {
        executeCommand 'DELETE FROM table1;'
        executeCommand "DELETE FROM table2 WHERE dt < TO_DATE('{curdate}', 'yyyy-mm-dd hh24:mi:ss');", 
                            [queryParams: [curdate: new Date()]]
    }
}
```
* В queryParams можно определить переменные, которые используются выполняемом скрипте.

Если требуется более сложная логика (ELT), то можно использовать собственный в Getl SQL парсер языка хранимых процедур:
```groovy
sql(ver) {
    vars.curdate = new Date()
    exec '''
            IF (NOT EXISTS(SELECT * FROM table1 WHERE dt = '{curdate}'::date);
                /*:count*/
                INSERT INTO table1 SELECT FROM table2;
                COMMIT;
                ECHO Добавлено {count} записей в table1 из table2
            END IF;
        '''
}
```
* Для _sql_ параметром указано, что работать код будет под соединением _ver_. Можно указать, в каком соединении работать, 
с помощью _useConnection_ внутри блока _sql_. 
* Если sql блок вызывается внутри блока соединения, то он будет выполнять команды под этим соединением, 
если явно не задано другое соединение.

## Описание таблиц JDBC источников
Для описания таблицы JDBC источника достаточно вызвать функцию с нужным именем типа таблицы и указать ее параметры хранения в БД:
```groovy
def oratab = oracleTable {
    useConnection ora
    schemaName = 'demo'
    tableName = 'table1'
} 
```
* Если все таблицы соединения находятся в одной схеме БД, то можно schemaName указать не для таблиц, а соединения.
Это также действительно для многих других параметров таблиц.

Если таблица не существует в БД, то можно явно задать ей структуру хранения данных и создать:
```groovy
def vertab = verticaTable {
    useConnection ver
    schemaName = 'demo'
    tableName = 'table1'
    if (!exists) {
        field('id') { type = integerFieldType; isKey = true }
        field('name') { length = 50; isNull = false }
        field('created') { type = datetimeType; isNull = false }
        
        createOpts {
            partitionBy = 'Year(dt)'
            segmentedBy = 'Hash(created::date)'
        }
        
        create()
    }
}
``` 
* При работе с таблицами JDBC источников Getl пытается получить их структуру из каталога метаданных БД. 
* Если структура явно описана через поля field, то будут с таблицы использоваться только те поля, которые указаны в field.
* Таблицы можно создавать (create), удалять (drop) и очищать (truncate).

**Внимание: в коде обращение к полям должно быть всегда в нижнем регистре!** 

Для таблиц можно задать опции создания (createOpts), чтения данных (readOpts) и записи данных (writeOpts), которые будут 
влиять на логику работы операций. Для разных типов JDBC источников опции будут иметь свой набор свойств:
```groovy
def imptab = impalaTable {
    useConnection impalaConnection {
      connectHost = 'hadoop1'
      connectDatabase = 'demo'
      login = 'user'
    }

    tableName = 'table1'

    field('id') { type = integerFieldType }
    field('name')
    field('created') { type = datetimeType }

    createOpts {
        type = externalTable
        storedAs = 'PARQUET'
        location = '/user/user/sample_files'
    }   
 
    drop(ifExists: true)
    create()
}
```
* Если соединение используется только внутри таблицы, его можно указать для таблицы без локальной переменной. 
* Параметры создания, удаления, чтения и записи можно указывать явно при вызове метода вместо их описания в _opts_.

## Описание наборов данных файловых источников
Аналогично работе с JDBC источниками выглядит работа с файлами:
```groovy
def csvfile = csv {
    useConnection csvConnection { path = '/data/files' }
    fileName = 'file1.txt'
    fieldDelimiter = '\t'
    codePage = 'utf-8'
    header = false
    field('id') { type = integerFieldType }
    field('name')
    field('created') { type = datetimeType }
 
    if (existsFile()) {
        drop()
    }

    writeOpts {
        splitSize = 1 * 1024 * 1024 * 1024 // 1 gb
    }
}

def excelFile = excel {
    fileName = 'c:\\documents\\excel1.xlsx'
    listName = 'sheet1'
    header = true
}

def jsonFile = json {
    fileName = '/data/files/file1.json'
    rootNode = 'data'
    field('id') { type = integerFieldType }
    field('name') { alias = 'name_object' }
    field('created') { type = datetimeType }
}

def xmlFile = xml {
    fileName = '/data/files/file1.xml'
    rootNode = 'data'
    defaultAccessMethod = DEFAULT_NODE_ACCESS

    attributeField('version') { alias = 'header.@version[0]' }

    field('id') { type = integerFieldType }
    field('name') { alias = '@type_name:name_object' }
    field('created') { type = datetimeType; format = 'yyyy-MM-dd HH:mm:ss' }
}
```
* Для файловых источников не требуется явно создавать описание соединения, если путь к файлу указан в fileName.
* При включенном header Getl пытается получить список полей набора данных из первой строки файла.
* Для файловых источников можно указывать опции чтения и записи в _readOpts_ и _writeOpts_. Они будут разными в 
зависимости от типа источника.
* Для иерархических файлов Json, Xml и Yaml требуется указать в  rootNode, откуда брать записи массива для набора данных.
* В случае, если наименование тэга в файле не совпадает с именем поля, можно указать его реальное имя в alias.
* Для десятичных и временных типов полей можно задать форматирование в свойстве _format_. 
* Для Xml файлов можно дополнительно задать получаемый из заголовка атрибут, а также вручную описать путь, как брать
данные для поля: из атрибутов или значения тэга, с учетом вложенности полей.

# Работа с источниками
## Чтение данных из источников
У всех наборов данных в Getl есть методы rows и eachRow. Первый возвращает набор записей в массиве, второй позволяет 
сделать по-записную обработку данных:
```groovy
def csv_rows = csvFile.rows(limit: 1000)
oratab.with {
  eachRow(where: 'id > 0', order: ['id'], limit: 1000) { row ->
    println "id: ${row.id}, name: ${row.name}, created: ${row.created}"
  }
}
```
* Оператор _with_ позволяет внутри блока объекта писать код, который работает из-под объекта без необходимости указывать 
к нему полное обращение.
* Для файловых источников можно указывать ограничение количества считываемых записей с помощью _limit_.
* Для JDBC источников можно указывать фильтрацию записей в _where_, сортировку в _order_ и ограничение количества
записей в _limit_. Указанные правила транслируются в SELECT и выполняются средствами РСУБД.

## Запись данных
В Getl запись данных поддерживается для всех источники, кроме следующих источников:
* Json, Xml, Yaml: рекомендуется записывать данные с помощью специализированных Builder Groovy для этих форматов;
* Hive и Impala: рекомендуется загружать данные пакетно с помощью функционала Getl bulk load;
* SalesForce и NetSuite: используемые драйверы источников работают только на чтение.

Для записи данных в Getl используется оператор _rowsTo_:
```groovy
rowsTo(csvFile) {
    writeRow { updater ->
        updater id:1, name: 'один', created: new Date()
        updater id:2, name: 'два', created: new Date()    
    }
}

vertab.with {
    writeOpts {
        batchSize = 10000
    }
    rowsTo {
        writeRow { updater ->
            (1..100000).each { num ->
                def row = [:]
                row.id = num
                row.name = getl.utils.GenerationUtils.GenerateString(50)
                row.name = getl.utils.GenerationUtils.GenerateDateTime()
                updater row
            }
        }
    }
}

rowsTo(oratab) {
    destParams.operation = 'DELETE'
    writeRow { updater ->
        (1..100).each { num ->
            row.id = num
            updater row
        }  
    }
}
``` 
* Для _rowsTo_ требуется указать параметром набор данных приёмник, кроме случая, когда он вызывается внутри блока 
объекта набора данных.
* В блок _writeRow_ передается дескриптор записи _updater_, при вызове которого добавляется запись в набор данных.  
* С помощью опции _batchSize_ для JDBC источников можно задать размер передаваемого пакета записей в БД.
* С помощью _destParams_ можно переназначить опции записи, указанные в writeOpts источника.
* По умолчанию Getl вставляет записи в таблицу. С помощью параметра operation можно указать, что требуется изменять
(UPDATE) или удалять (DELETE) записи. Для выполнения этих операций у таблицы должен быть определен первичный ключ.
* С помощью библиотеки GenerationUtils можно по заданным правилам генерировать случайные наборы данных.

## Запись данных в несколько источников
Для записи данных сразу в несколько приёмников в Getl используется оператор _rowsToMany_:
```groovy
rowsToMany(dest1: oratable, dest2: vertable) {
    writeTo { add ->
        jsonFile.eachRow { row ->
            add 'dest1', row
            add 'dest2', row
        }
    }
}
```
* Для каждого записываемого набора данных указывается имя алиаса
* При вызове дескриптора записи _add_ первым параметром нужно задать имя алиаса, куда пишутся данные, а вторым параметром
передать записываемую запись.

# Копирование данных между источниками
Для копирования записей между наборами данных в Getl используется оператор _copyRows_:
```groovy
copyRows(oratab, vertab)
```
* При вызове _copyRows_ первым параметром ему указывается источник, вторым приёмник.
* По умолчанию поля таблиц источника и приёмника связываются по одинаковым именам.
* По умолчанию при копировании данных происходит автоматическая конвертация типов между полями таблиц, кроме случаев, 
когда конвертация физически не возможна.
Если  поля таблиц имеют разное именование или требуется вручную заполнить поля приёмника, это описывается внутри _copyRows_:
```groovy
copyRows(excelFile, vertab) {
    autoMap = false
    autoConvert = true
    map.name = 'object_name'
    map.created = 'strdate:yyyy-MM-dd HH:mm:ss'
    copyRow { sourceRow, destRow ->
        assert sourceRow.id != null
        destRow.id = Integer.valueOf(sourceRow.row_id)
    }

}
```
* Свойство _autoMap_ указывает, требуется ли связывать поля источника и приёмника для автоматического заполнения полей
приёмника значениями полей записей источника.
* Свойство _autoConvert_ указывает, требуется ли автоматически конвертировать значения типов найденных связанных полей. 
* В _map_ можно явно задать связь полей приёмника с полями источника, при необходимости можно через двоеточие 
указать формат преобразования поля источника, это актуально для десятичных и временных типов.
* Блок _copyRow_ вызывается на каждую запись источника, в него передается исходная запись с источника и подготовленная 
запись приёмника. В коде блока можно изменить запись приёмника, эти изменения будут учитываться при сохранении записи.

## Копирование данных с одного источника в несколько приёмников
Оператор _copyRows_ может записывать данные в несколько приёмников. Это полезно, когда источник содержит набор 
записей master-detail и записи надо сохранить при чтении источника за один проход:
```groovy
useVerticaConnection ver
def mastertab = verticaTable {
    tableName = 'table_master'
    field('id') { type = integerFieldType; isKey = true }
}
def detailtab = verticaTable {
    tableName = 'table_detail'
    field('parent_id') { type = integerFieldType; isNull = false }
    field('sum') { type = numericFieldType; lenght = 12; precision  = 2 }
}
jsonFile {
    fileName = '/data/files/file2.json'
    field('id') { type = integerFieldType }
    field('detail_data') { type = objectFieldType }
}

copyRows(jsonFile, mastertab) {
    childs(detailtab) {
        writeRow { addDetail, sourceRow ->
            sourceRow.detail.data.each { detailRow ->
                addDetail parent_id: sourceRow.id, sum: detailRow.sum
            }       
        }
    }   
}
```
* Если объявляются таблицы в рамках одного соединения, с помощью оператора _use<Тип>Connection_ можно задать соединение 
по умолчанию вместо указания соединения для каждой объявляемой таблицы.
* С помощью _childs_ можно указать дополнительные наборы данных, куда должна вестись запись. В _writeRow_ приходит
дескриптор записи в дочерний источник и считанная запись источника.
* Для Json, Xml и Yaml источников вложенные структуры и массивы можно объявлять, как поля с объектным типом и 
работать с ними в коде напрямую (в примере выше поле json источника _detail_data_ является массивом набора десятичных
полей, который перебирается с помощью _each_ и все его значения записываются в таблицу _table_detail_).
* Если для _copyRows_ не указывается блок _copyRow_, то считается, что требуется скопировать все поля из источника
в приёмник, связать поля по их именам и сконвертировать типы данных.

# Пакетная загрузка файлов в источники
На текущий момент в Getl поддерживается пакетная загрузка файлов в таблицы следующих JDBC источников: Impala, Hive, H2 и
Vertica:
```groovy
def csvFile = csvTemp {
    field('id') { type = integerFieldType }
    field('name')

    (1..10).each { numFile ->
        fileName = "data.${numFile}.txt"
        rowsTo {
            writeRow { append ->
                (1..10000).each { id ->
                    append id: (numFile - 1) * 10000 + id, name: "Строка $id файла $numFile"
                }
            }
        }
    }
}

verticaTable {
    useConnection ver
    schemaName = 'public'
    tableName = 'demo_bulkload'
    field('id') { type = integerFieldType; isKey = true }
    field('name') { length = 100 }
    create(ifNotExists: true)
    truncate()

    bulkLoadCsv(csvFile) {
        files = 'data.{num}.txt'
        loadAsPackage = true
    }   
}
```
* В Getl можно создавать временные csv файлы с помощью _csvTemp_. В отличие от _csv_ файлов, они хранятся в temp директории
ОС и автоматически удаляются после завершения работы приложения. В примере выше генерируется 10 файлов по 10 тысяч записей.
* Для команды загрузки файлов _bulkLoadCsv_ требуется в параметре указать шаблон CSV файла, по которому будет происходить 
загрузка данных.
* В _files_ указывается имя файла, массив имен файлов или маска имен файлов для загрузки в таблицу. Если _files_ не указан,
то загружается файл из _fileName_ переданного параметра csv файла.
* Для источника Vertica можно включить загрузку всех файлов одним пакетом с помощью _loadAsPackage_. Для других источников
загрузка будет идти поочередно для каждого файла.  

# Инкрементальный захват данных
# Работа с файловыми системами
# Использование репозитория объектов
# Организация многопоточной работы
# Работа с файлами конфигураций
# Работа с ресурсными файлами проекта
# Разработка повторно используемых шаблонов
# Разработка unit-test классов 
# Создание собственного расширения функциональности Getl
