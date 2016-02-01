#Результаты нагрузочного тестирования прототипа

###Конфигурация теста

* Прототип и база данных — на стандартном [DigitalOcean](https://www.digitalocean.com) дроплете (2Gb, 2 CPU, 40Gb SSD) в регионе Frankfurt 1.

* Тестирование произведено с помощью [Loadster](https://www.loadsterperformance.com).

* Для симуляции нагрузки использовался кластер **Loadster Engine** на 9 идентичных минимальных дроплетах (512Mb, 1 CPU), находящихся в разных регионах и **Loadster Local Engine** на локальной машине.
* На каждом узле кластера находилось по одной популяции виртуальных пользователей. 


### Популяции виртуальных пользователей

Одинаковые параметры популяций:

|# пользователей (пик)|Ramp Up|Peak Duration|Ramp Down|
|:-------------------:|:-----:|:-----------:|:-------:|
| 10 | 1 час | 2 часа | 1 час |

Популяции:

|Популяция|Сценарий|Узел|
|:-------:|:------:|:--:|
|Visit: no ref - 1|Visit: no ref|ubuntu-512mb-sgp1-01|
|Visit: no ref - 2|Visit: no ref|ubuntu-512mb-tor1-01|
|Visit: no ref - 3|Visit: no ref|local|
|Visit: ref - 1|Visit: ref|ubuntu-512mb-sfo1-01|
|Visit: ref - 2|Visit: ref|ubuntu-512mb-nyc2-01|
|Registration: no ref - 1|Registration: no ref|ubuntu-512mb-lon1-01|
|Registration: ref - 1|Registration: ref|ubuntu-512mb-ams2-01|
|PartnerReg: no ref - 1|PartnerReg: no ref|ubuntu-512mb-ams2-02|
|PartnerReg: ref - 1|PartnerReg: ref|ubuntu-512mb-ams2-03|
|Partner stats - 1|Partner stats|ubuntu-512mb-nyc2-02|

Сценарии:

|Сценарий|Последовательность|Проверка|
|:------:|:----------------:|:------:|
|Visit: no ref|GET `/signup`|Status Ok|
|Visit: ref|GET `/signup?ref=${code}`|Status Ok|
|Registration: no ref|GET `/signup` -> POST `/signup`|`resp == "Registred"`|
|Registration: ref|GET `/signup?ref=${code}` -> POST `/signup?ref=${code}`|`resp == "Registred"`|
|PartnerReg: no ref|GET `/signup/affiliate` -> POST `/signup/affiliate`|`resp == validCode`|
|PartnerReg: ref| GET `/signup/affiliate?ref=${code}` -> POST `/signup/affiliate?ref=${code}`|`resp == validCode`|

* До начала тестирования в БД:
    - партнеров: 100
    - действий: 0
* `${code}` выбирается случайным образом из списка из вышеупомянутых 100 партнеров.
* Одна итерация состоит из выполнения последовательности и проверки условия.
* Пауз между итерациями нет.
* Число пользователей в популяциях растет (по гауссиане) от 0 до 10 в первый час тестирования, остается постоянным следующие 2 часа и убывает в последний час.

###Результаты

Тестирование заняло 3 часа 51 минуту и 32 секунды:

##### Общие параметры:

|Page requests|Hits|Bytes|Errors|User Iterations|
|:-----------:|:--:|:---:|:----:|:-------------:|
|3569810|3569810|1.27 GB|13|2287173|

###### Пропускная способность:

|Категория|Max|Avg|Min|
|:-------:|:-:|:-:|:-:|
|Pages per Second|567.00|257.08|0.00|
|Hits per Second|567.00|257.08|0.00|
|Bytes per Second|286737.00|98475.93|0.00|

###### Время отклика:

|Путь|Max(s)|Avg(s)|Min(s)|Total(s)|
|:--:|:----:|:----:|:----:|:------:|
|`/signup`|9.38|0.39|0.02|431654.27|
|`/signup?ref=${code}`|1.97|0.32|0.01|323639.32|
|`/affiliate/${id}`|3.06|0.89|0.19|107924.24|
|`/signup/affiliate`|1.66|0.16|0.01|107835.36|
|`/signup/affiliate?ref=${code}`|1.68|0.16|0.01|107753.52|

##### Ошибки:

|Путь|Error| # |
|:--:|:---:|:--:|
|`/signup`|page load time exceeded|13|

##### База данных:

В результате тестирования:
* # партнеров в БД: 100 -> 661603
* # действий в БД:    0 -> 1664256


##### Основные графики:

Среднго времени отклика по страницам (в сек):
![Average Response Times by Page](https://s3.amazonaws.com/f.cl.ly/items/0i3z191h0r282Q313Y1H/Screen%20Shot%202016-02-01%20at%2012.36.40.png?v=a9469910)

Пропускной способности сети (в Мб/сек):
![Network Throughput](https://s3.amazonaws.com/f.cl.ly/items/3u0n270T162X3E2H1H30/Screen%20Shot%202016-02-01%20at%2012.37.08.png?v=d37c22eb)

Числа обработанных запросов (в запрос/сек):
![Transaction Throughput](https://s3.amazonaws.com/f.cl.ly/items/2M3r153h062z1q330L1N/Screen%20Shot%202016-02-01%20at%2012.37.37.png?v=8fe8cd42)

Ошибок по страницам:
![Errors by Page](https://s3.amazonaws.com/f.cl.ly/items/0Y0F1t0l1u3x103S2I2Q/Screen%20Shot%202016-02-01%20at%2012.38.03.png?v=95ec3ddc)

Эти и остальные графики доступны в интерактивной форме в [автоматически сгенерированном файле](http://htmlpreview.github.io/?https://gist.githubusercontent.com/gafiatulin/5101249c9a035758ba9c/raw/6d08cb9b593fc484aacebca0d33f752a076a9d62/Test.html).

