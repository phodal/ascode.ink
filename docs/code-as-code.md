---
layout: default
title: 代码代码化
nav_order: 7
description: "文档代码化，将文档以类代码的领域特定语言的方式编写，并借鉴软件开发的方式（如源码管理、部署）进行管理。它可以借助于特定的工具进行编辑、预览、查看，又或者是通过专属的系统部署到服务器上。"
---

# 代码代码化

我们想要做的事情是：把**任意的** A 语言转换为**任意的** B 语言（PS：这里的任意 A 和任意 B 语言都是主流语言）。如此一来，我们便可以：

1. 快速重写任何的系统。
2. 与编程语言无关的领域建模。
3. 产生一个更强大的 DSL。
4. 创建新的语言。

## 引子 0：统一语言模型

> 统一语言模型，即对不同的比编程语言进行抽象，使用同一套数据结构描述编程语言。

在我使用了 Golang + Antlr 实现了 Coca 之后，我意识到这是一条可行的方案。但是，由于 Coca 的架构和用途所限，外加之 Antlr 对于 Java 的支持远比 Go 要好，我并没有继续在 Coca 上实施这个方案。

于是乎，我开始了第二个尝试，使用 Kotlin + Antlr 来实现对不同语言的模型统一，也就是我的另外一个开源项目 Chapi。但是呢，随着不断的尝试，我发现了其中的难度和工作量比较大：

1. **编写不同语言的语法解析**。社区上已经有大量的成熟的轮子，其中最出名的就是 Antlr 相关的语法解析。官方维护的代码仓库（grammars-v4）包含了大量的 Antlr 语法解析案例，可以找到市面上一些主流的和非主流的实现。
2. **设计统一语言模型**。即设计出一套能兼容不同语言的语言模式。当然了，这是一个持续完善的过程，会随着更多语言的加入，变得更加完整和复杂。
3. **解析不同语言**。即根据不同语言的语法特性，转换为上述的模型。

从难度上来说，我们可以看出技术难度主要是在步骤 1 和步骤 2。而步骤 3 呢，则是一个非常繁琐、工作量巨大的体力活。我们还需要熟悉不同的编程语言，并一一解析对应的字段，才能转换每一个语言。

因此，我尝试建立起了 Chapi 的社区，然后手把手带领一群人干活。尽管，对于不同的语言我已经建立起了统一的编写模式：TDD + Tasking。似乎，很多人对于 AST 有点担心，因此参与的人非常少。所以，对于其它语言的支持就不了了之。

相关资源：

 - 详细的设计可以参考我写的那一篇：《[如何为代码建模？](https://www.phodal.com/blog/modeling-for-code/)》
 - 详细的实现可以参照：[https://github.com/phodal/chapi](https://github.com/phodal/chapi)

## 引子 1：语法高亮的背后

与此同时，哪怕有足够的人，Antlr 并非一个完美的答案。在编写不同语言的支持时，我依旧遇到一系列的 Antlr 语法不支持的问题。如 JavaScript 的 Import，Java 的一些 Lambda 问题……。换句话来说，Antlr 官方只是维护这么一个库，真实的效果就不得而知了。

于是，我就回到了一条老路上，使用正则——当然不会自己写了。在那篇《[编程语言的 IDE 支持](https://www.phodal.com/blog/language-in-ide/)》中，我提到了**基于正则表达式来实现语法分析**，其中介绍了两个编辑器的实现方式：

 - Sublime Text 基于 YAML 形式的正则匹配方式：[Sublime Syntax files](http://www.sublimetext.com/docs/3/syntax.html#include-syntax)
 - Textmate、VS Code 基于 JSON 的正则匹配方式：[Language Grammars](https://macromates.com/manual/en/language_grammars)

所以，我们选择了 VSCode 作为了语法解析背后的语言。在这种模式之下：

1. 我们有一个成熟稳定的语言解析工具，并且也有一个巨大的团队在维护它们。
2. 它的社区是非常庞大的，经过大量的反复提升。

因此，我和我的同事从几个前开始编写：[https://github.com/phodal/scie/](https://github.com/phodal/scie/) —— 一个基于 TextMate  语法高亮的库。

## 引子 2：代码生成与 JavaPoet

在我们粗糙地完成了 Scie 之后，我开始思考着下一步：**如何从 A 语言转换为 B 语言的时候**，我从 JavaPoet 获取到了一些灵感。JavaPoet 是一个用来生成 `.java` 源文件的 Java API。如下是一个简单的 JavaPoet 代码示例：

```java
TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
    .addMethod(main)
    .build();
```

也就是说，我们可以写一个 API，以将某语言转换为 B 语言的源码。而要实现任意语言的转换，那么我们就需要实现一个 DSL：用于**描述不同语言与统一模型的差异**。后来，我意识到我还需要另外一个 DSL，用于**转换统一模型到不同的语言**。

## 引子 3：中间表示的演变

> 编译器的核心数据结构是被编译程序的中间形式。 —— 《编译器设计》

理论上，通过上述的两种方式，我们就可以直接生成不同领域的模型。但是呢，为了调试方便，可以创建一个中间语言来作为它们的承载物，可以让我们实现更有意思的事情，去统一进行编译器优化——当然，我是瞎说的。

随后项目的原因，我研究了一小段时间的 Proguard + D8 和 Android R8 的实现上。它们两做的事情是相似的，将 `.class` 字节码，编译优化，再转换成 Android 手机上的 dex。当然了，转换为 Aot 就是一个更有意思的话题了（虽然我也不熟悉）。但是呢，这期间涉及到了一系列的中间状态：`java -> .class -> .dex -> odex -> .oat`。即从 Java 代码到 JVM 虚拟机字节码 -> Dalvik 虚拟机字节码 -> 优化过后的 Dalvik 字节码 -> ART 机器码。

而我们再回过到来看，编码语言本身也是一种中间表示，因为机器运行的是靠机器码。即，那句经典的话：**代码是写给人看的**。

## 引子 4：DSL 的 DSL

对于有的编译器来说 ，它们可能有唯一的 IR（中间表示，Intermediate representation），也可能会有一系列的 IR。最常见的一些实现，便是我们看到的那些使用 LLVM 作为后端的语言，它们可以生成中间形式的 LLVM IR。同样的对于我们想做到的事来说，我们可以设计一个类似于 LLVM IR 的高级中间表示，用于承载语言的设计。

由于项目涉及到一丁点的代码优化，所以我还阅读了一下那本《高级编译器设计与实现》，书中引入了 ICAN 这个中间语言。嗯，这就是已经被论证的结果了，不再需要我去论证它的必要性。所以下一步就是：

> 自举，在计算机科学中，它是一种用于生成自编译编译器的技术，即使用打算编译的源编程语言编写的编译器。

在业内，人们往往往把自举定义在编译器领域中。但是呢，它可以在更多的领域被应用。例如 Java 的构建工具，Gradle 使用 Gradle 来构建自己 —— 当然与编程语言相比，这事要相对容易一些。

而人的自举就是把自己替换便，让工具做了自己的事，让别人做得了自己的事。所以，我们就需要 Charj 来做自己所能做的事情。

## Charj Lang

终于回到了正题上了，在有了上面的几步之后，我们就能：

1. 通过正则表达式，解析、生成不同语言的语法树。
2. 编写 Poet API 将上述的语法树，转换为某一特定语言源码。
3. 设计某一中间语言，用来作为 A 语言转换为 C 语言的载体。
4. 实现 A 语言到 C 语言，又或者 C 语言到 A 语言的自由转换。

这便是从任意语言转换为任意语言的想法和思路。于是乎，我和我的同事们开始设计一个中间语言：Charj。

当然了，开发一个语言的目的主要是为了锻炼自己的能力，不论是抽象能力，还是算法能力等等。在这个漫长的人生里， 它将会变得有意义。以后，请叫我 Charj 语言作者。PS：你也可以是 Charj 语言作者。

回过头来看，事实上应该是这样的，我已经尝试造了各式各样的工具，从各类的编辑器到各类的命令行工具。而在学习了 Rust 之后，我研究了 JVM、编辑器底层，也正在逐一尝试创建日常所使用的工具。而在上一年里，因为编写重构工具 Coca，再到随后的转换为统一语言模型的 Chapi。对于编译器前端，我已经有了相当丰富的经验。自然而然的，创造一个语言就成了下一个方向。

### 为什么叫 Charj ？

从本义上来说，Char 是更适合 Charj 的定义的，但是 Char（仓颉）的商标已经被注册了。退而求其次，我只好叫 Charj，可以引伸为中英混合式的：字符（Char）集（Ji），又或者是字符（Char）集（姬）。又或者是『字符 J』 —— 至于 J 是什么意思，我还没想清楚。我们可以再定义，再取一个新的名字。

## Charj 进展

Charj 使用的是 Rust 为主的语言编写的。Rust 的自举已经证明了：Rust 用于开发编程语言是没有问题的。当然了，主要原因还在于让我 C++，还不如让我写 Haskell。

### Charj Lang （设计中）

Charj lang 现在的工作分为两部分：

1. 完善语法设计
2. 编译器的流程设计

尽管从理论上来说，Charj 不一定需要编译 + 可运行，但是为了自举，我们需要它们。于是，我们在后端采用了 LLVM，前端使用的是 Rust 里的 LR（1）解析器生成器 [lalrpop](https://github.com/lalrpop/lalrpop) 。

GitHub：[https://github.com/charj-lang/charj](https://github.com/charj-lang/charj)

### Charj IDE（开发中）

当前已经有一个简单的语言插件，当然只有基本的高亮和跳转功能。如果你有一定的 IDEA 插件开发经验，也可以来我们一起搞搞。

GitHub：[https://github.com/charj-lang/intellij-charj](https://github.com/charj-lang/intellij-charj)

### Scie

Scie（Simple Code Identify Engine）是一个基于正则表达式的通用语言转换器。主要开发工作基本已经完成了，但是有几个问题需要解决：

1. 效率优化
2. 调用 Oniguruma FFI 时会随机出错。

GitHub：[https://github.com/charj-lang/scie](https://github.com/charj-lang/scie)

### Charj Poet（开发中）

Charj Poet 是一个是用于生成 Charj 代码的 Rust API。计划等语法设计完，再进一步完善。

GitHub：[https://github.com/charj-lang/charj-poet](https://github.com/charj-lang/charj-poet)

### Poet DSL（待定）

两部分：

1. 即设计一个新的 DSL，来描述不同语言转换为 Charj Lang 的 DSL。
2. 即设计一个新的 DSL，来描述 Charj Lang 转换为不同语言的 DSL。

### 官网

简陋和粗糙的官网：[https://charj-lang.org/](https://charj-lang.org/)

## 其它 

此时此刻，虽然我翻过几本编译相关的书籍，我也并非一个编译原理相关的专家。所以，如果你也有兴趣，欢迎来加入我们。
