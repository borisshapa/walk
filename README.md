## Walk

* Класс `RecursiveWalk` осуществляет подсчет хеш-сумм файлов в директориях.
* Формат запуска
    ```
    java Walk <входной файл> <выходной файл>
    ```
* Входной файл должен содержать список файлов, которые требуется обойти.
* Выходной файл содержит по одной строке для каждого файла. Формат строки:
    ```
    <шестнадцатеричная хеш-сумма> <путь к файлу>
    ```
* Для подсчета хеш-суммы используется алгоритм [FNV](https://ru.wikipedia.org/wiki/FNV).
* Если при чтении файла возникают ошибки, они указываются в качестве его хеш-суммы 00000000.
* Кодировка входного и выходного файлов — UTF-8.
* Если родительская директория выходного файла не существует, то соответствующий путь создаётся.
* Размеры файлов могут превышать размер оперативной памяти. Пример:
    * Входной файл
        ```
        java/info/kgeorgiy/java/advanced/walk/samples/1
        java/info/kgeorgiy/java/advanced/walk/samples/12
        java/info/kgeorgiy/java/advanced/walk/samples/123
        java/info/kgeorgiy/java/advanced/walk/samples/1234
        java/info/kgeorgiy/java/advanced/walk/samples/1
        java/info/kgeorgiy/java/advanced/walk/samples/binary
        java/info/kgeorgiy/java/advanced/walk/samples/no-such-file
        ```                    

    * Выходной файл
        ```
        050c5d2e java/info/kgeorgiy/java/advanced/walk/samples/1
        2076af58 java/info/kgeorgiy/java/advanced/walk/samples/12
        72d607bb java/info/kgeorgiy/java/advanced/walk/samples/123
        81ee2b55 java/info/kgeorgiy/java/advanced/walk/samples/1234
        050c5d2e java/info/kgeorgiy/java/advanced/walk/samples/1
        8e8881c5 java/info/kgeorgiy/java/advanced/walk/samples/binary
        00000000 java/info/kgeorgiy/java/advanced/walk/samples/no-such-file
        ```          
* Входной файл может содержать список файлов и директорий, которые требуется обойти. Обход директорий осуществляется рекурсивно. Пример:
    * Входной файл
        ```
        java/info/kgeorgiy/java/advanced/walk/samples/binary
        java/info/kgeorgiy/java/advanced/walk/samples
        ```         
    * Выходной файл
        ```
        8e8881c5 java/info/kgeorgiy/java/advanced/walk/samples/binary
        050c5d2e java/info/kgeorgiy/java/advanced/walk/samples/1
        2076af58 java/info/kgeorgiy/java/advanced/walk/samples/12
        72d607bb java/info/kgeorgiy/java/advanced/walk/samples/123
        81ee2b55 java/info/kgeorgiy/java/advanced/walk/samples/1234
        8e8881c5 java/info/kgeorgiy/java/advanced/walk/samples/binary
        ```
* Для того, чтобы протестировать программу:
   * Скачайте
      * тесты
          * [info.kgeorgiy.java.advanced.base.jar](artifacts/info.kgeorgiy.java.advanced.base.jar)
          * [info.kgeorgiy.java.advanced.walk.jar](artifacts/info.kgeorgiy.java.advanced.walk.jar)
      * и библиотеки к ним:
          * [junit-4.11.jar](lib/junit-4.11.jar)
          * [hamcrest-core-1.3.jar](lib/hamcrest-core-1.3.jar)
          * [jsoup-1.8.1.jar](lib/jsoup-1.8.1.jar)
          * [quickcheck-0.6.jar](lib/quickcheck-0.6.jar)
   * Откомпилируйте программу
   * Протестируйте программу
      * Текущая директория должна:
         * содержать все скачанные `.jar` файлы;
         * содержать скомпилированное решение;
         * __не__ содержать скомпилированные самостоятельно тесты.
      * ```java -cp . -p . -m info.kgeorgiy.java.advanced.walk RecursiveWalk ru.ifmo.rain.shaposhnikov.walk.RecursiveWalk```
