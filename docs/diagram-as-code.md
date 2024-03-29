---
layout: default
title: 图表即代码
nav_order: 9
description: "图表即代码即将图表以领域特定语言作为载体，围绕于不同的使用场景，转译生成二次产物 —— 如概念图、架构图、软件架构等。"
---

# 图表即代码

> 图表即代码即将图表以领域特定语言作为载体，围绕于不同的使用场景，转译生成二次产物 —— 如概念图、架构图、软件架构等。

这个定义从某种意义上是围绕于现有的工具而产生的，诸如于：

* 可视化架构图。在 [Coca](https://github.com/modernizing/coca) 中，我们使用 Graphviz 来生成软件的依赖关系；在 GitHub 网页上，可以使用 Mermaid 来编写 README.md。
* 生成代码。诸如于 PlantUML，利用工具可以从 UML 到代码骨架生成；如 Structurizr DSL，可以让从 C4 模型生成 PlantUML 图，进而生成代码。
* 交互的图表。如在 Ledge 中，生成的图形本身是可以调整和交互的。

对于这样的系统，我想大家都知道如何去设计了。或者说，至少在心底是有个印象。

## 领域特定语言描述

作为代码化的第一要素，它必须是采用 DSL （领域特定语言） 的设计，才能有效地进行代码化。值得注意的是不要畏惧 DSL：采用领域特定语言，并不意味着特别复杂的编译实现，哪怕是 JSON 格式描述，也可以适为一种 DSL。所以，诸如于 Graphviz 中设计的 DOT Language 就非常的简单：

```javascript
digraph G {
  a -> b
  b -> c
} 
```

简单的语法，可以生成非常有用的图形。只在我们需要一些额外的配置时，才需要去翻看对应的文档。而如果我们能提供更多的 samples，那么就能降低查看文档的成本，构建更好的开发体验。

从另外一个层面来说，图形的序列化结果，其实也算得是上一种领域特定语言。诸如于 `.excalidraw` 的 JSON 形式，`.drawio` 文件采用的编码后的 mxgraph 的 XML 格式，它们都是图形的一种类型的领域特定语言。

## 布局计算：算法生成的关系图

对于代码生成图形来说，用过 D3.js 或者是 Echart.js 的小伙伴，对于 Dagre、ForceLayout 等一系列的图形自动布局算法不陌生。在这里就不展开了 —— 主要是我也不是算法专家。

随后，布局的计算依赖于数据 + 模型，对于一个图表既代码的系统来说：

* 模型，依赖于 DSL 生成的构建的模型。其中大部分是隐式的模型，如上述 DOT 语言中的 `a` 和 `b` 是节点，而 `→` 是指向关系。
* 数据，来源于 DSL 又或者是数据源。如 Graphviz 中来源于 DSL 中的代码，而在支持 import 关系的 DSL 中，则可以通过 DSL 来导入数据。

当然了，如果能提供一个抽象的算法接口，以接入更多的布局算法，那么就可以大大提高系统的灵活性。在这一点上 [Cytoscape.js](https://js.cytoscape.org/) 就做得挺好的，提供了 ELK、CoSE、Cola、fCoSE 等算法的接入，底层的灵活性会带来更多的可扩展空间。

## 二次转译：支持后续活动

从现实的因素来考虑，并非所有的图表都应该用图表即代码的方式。人们采用图表即代码这种方式，也意味着：基于可视化的结果，进行后续的活动。诸如于：

* 采用 Structurizr DSL、PlantUML 来呈现系统的设计，会考虑用它来生成模板代码，并在后续对比实现架构与目标架构的差异。
* 采用 Graphviz 来生成系统依赖关系，用它来展示系统中的循环依赖，再通过自动化地方式检测。
* ……

也因此，与其说是图形即代码，不如说图形化只是中间的产物，作为沟通时的信息载体。在这点上，它与[设计即代码](https://www.phodal.com/blog/ui-design-as-code/)颇为相似，DSL 充当的是图形的标准化输出。

## 可选的双向绑定：代码 < - > 图形

与上述的内容相比，在代码与图形之间提供双向绑定显得非常有意思。代码化可以向程序员提供高效的输入方式，但是正如新手程序不习惯用 Terminal 一样，他们也需要图形化的方式。于是呢，如何在改变图形的同时，更新代码就变得非常有意思了。从结果上来说，图表工具在保存的时候，存储的是数据模型，而模型便是这个双向绑定的基础。如在使用 draw.io 这样的可视化工具时，当我们添加新的矩形、连接时，结果会更新到对应的数据模型中。

而图形化的编辑呢，存在一些额外的动作（action），如我们在撤销（undo）、重做（redo）的时候，要提供这种模型的重载。这些因素显然会带来一些额外的工作量。

