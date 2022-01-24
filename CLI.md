Команда: Энгель Игорь, Вавилов Марк
Диаграммы: [первый этап](https://app.diagrams.net/#G1NCKTjDAzVfH0qoTYvEjekJihJgvRG-uT), [второй этап](https://app.diagrams.net/#G1scl4B9y2_bsXD_zG2miv4eju7DePTAo5)

## Shell   
`environment: Map<String, String>` -- набор переменных окружения  
`fun execute(line: String): Int` -- с помощью `parser` преобразует поданную на вход строку в `CommandPipeline` и вызывает `execute()`

## Parser

В строке производится подстановка, возвращается`CommandPipeline`
Выбор команд осуществляется путём итерирования по `commands`, и вызову `match`
  
  
## CommandPipeline  
  
Последовательность готовых команд `commands: List<PreparedCommand>`  

## AbstractCommnad

`match` - проверяем задаёт-ли переданная строка данную команду
`execute` - выполняет команду

## SimpleCommand

`match` - проверяем что первый элемент совпадает с навзанием команды
  
## PreparedCommand  
  
 По сути пара команда + её аргументы
  
## Остальное  
  
Обработка однопоточная, команды исполняются последовательно
