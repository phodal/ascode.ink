## Class

compile

```bash
javac -cp external_libs\lib1.jar;other\lib2.jar; HelloWorld.java
```

run

```bash
java -classpath .:libs/joda-time-2.10.6.jar HelloWorld
```

## Jar

[Java creating .jar file](https://stackoverflow.com/questions/4597866/java-creating-jar-file)

```bash
echo "Main-Class: HelloWorld" >> manifest.txt
```

create jar

```bash
jar cvfm hello.jar manifest.txt HelloWorld.class libs/*
```

run jar

```bash
java -jar hello.jar
```
