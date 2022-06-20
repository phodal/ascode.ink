---
layout: default
title: UI 设计代码化
nav_order: 7
description: "UI 设计代码化，即将软件的 UI 设计与 UI 交互转换为特定的领域语言，并使用代码的方式来进行管理。它可以直接将需求转换为 UI 原型，让设计人员基于此进行设计；还负责将其转换对应的 UI 代码，方便开发人员进行编写。"
---

# UI 设计代码化：低代码式设计语言

> UI 设计代码化，即将软件的 UI 设计与 UI 交互转换为特定的领域语言，并使用代码的方式来进行管理。它可以直接将需求转换为 UI 原型，让设计人员基于此进行设计；还负责将其转换对应的 UI 代码，方便开发人员进行编写。

在 Uncode IDE 里，设计代码化是由两部分组成：架构设计（代码设计）代码化与 UI 设计代码化，这是一个相当复杂的领域。作为一个在前端领域的专家，我是在去年完成了 UI 设计代码的第一个版本的设计；作为半个架构专家，我则是在最近完成了部分架构设计代码化的工作。

在最近 ，我刚使用 Rust 将去年设计的 UI DSL 重写，于是重新命名为 Unflow。想不到一个更好的名字，于是将它与 Uncode 进行了一个简单的对应。你可以在 GitHub 上看到 Unflow 当前（早期）版本的设计：[https://github.com/inherd/unflow](https://github.com/inherd/unflow) 。

在继续往下阅读之前 ，我要做一个简单的声明：在完成了 Unflow 的设计之后，我一直在等待机会能与一些用户体验设计师合作，以完善整个 DSL。但是呢，一直没有找至一个合适的机会。所以，当前的这个 DSL 并不一定接近真实的设计师体验。

所以呢，如果你对优先这个 DSL 有兴趣，可以一起参与设计。

## UI 设计代码化

> UI 设计代码化，即将软件的 UI 设计与 UI 交互转换为特定的领域语言，并使用代码的方式来进行管理。它可以直接将需求转换为 UI 原型，让设计人员基于此进行设计；还负责将其转换对应的 UI 代码，方便开发人员进行编写。

### 为什么需要 UI 设计代码化？

在文章开头里，我们定义了一下：UI 设计代码化。但是呢，为什么我们需要一个这样的代码化工具呢？从整个云研发体系来说，将 UI 设计代码化，我们要做的这么几件事：

1. 用户交互文档化。即所有的 UI 交互过程，以明确的格式记录下来，并与文档的形式存储。
2. UI 工具无关。采用标准化的方式描述 UI 设计，让 UI 设计与 UI 设计工具脱离。
3. 双向反馈。即我们的设计与 UI 原型、代码是相绑定的，当代码与设计不一致时，我们能即时得到反馈 —— 要么修改设计，要么修改代码。
4. 连接需求与代码的胶水。从某种程度上来说，这个 DSL 还承担着作为需求与代码连接的胶水。即将需求转换为设计的描述，以便于这个描述转换为代码。

在云研发体系中，它是非常重要的一环。

### 如何进行 UI 设计代码化？

在今天来看，将 UI 设计进行代码化已经变得相当的简单。只是呢，还有一些因素，会限制我们的代码化能力：

**矢量可编程的 UI 设计**。

UI 工具是整体过程中最令人头痛的问题。对于 UI 设计而言，如果它产出的内容不是矢量图形，那么它会限制我们的转换能力 —— 一个二进制文件不适合在代码库中存储。而，如果一个 UI 工具产生的格式是可直接编程操作的，那么就再好不过了，比如 SVG。但是呢，SVG 缺少一些引用等的相关设计。不过呢，Sketch 也是一个非常不错的工具，它的格式是易于进行编程操作的。

**UI 元素可编程**。

在进行 UI 设计的时候，我们会定义出一套 Design Sytem 或者 UI Guideline，上面充满了丰富的元素，如组件、字体等。对于这些元素来说，它应该是可以由代码生成，或者直接转换为设计 DSL。以用于**核验**代码中的元素是否真的与设计**匹配**。

**对于交互的抽象**。

对于交互的抽象是一个烦人的问题，但是呢，在我深入研究与探索之后，我发现这也不是一个复杂的问题。复杂度并不高，只是呢，我们要考虑如何与我们的设计、代码进行关联，形成统一的关系。

## UI 设计代码化要素

综上所述，我们在对 UI 进行代码化时，要考虑这么一些要素。

### 要素 1：代码反馈设计

在云研发体系里，我们将所有一切代码化有两个原因：

1. 流程代码化，并实现化转换自动化。
2. 借助反馈进行自动优化。

对于 UI 设计代码化这一步来说，我们要：

1. 寻找合适的 UI 设计工具及对应的解析库，以将解析 UI 设计，转换为特定的领域语言。
2. 能解析修改过后的生成代码，将代码实现与 UI 设计进行对比。
3. 自动绑定 UI 设计与代码，自动修改、提示不合理的地方。

### 要素 2：支持增量变更

设计与代码是相似的，在开发过程中，会伴随着需求的变化，影响到 UI 设计上的变化。因此，对于 UI 设计产物来说，它们应该：

1. 可版本化。与代码库同在，能跟踪到设计的历史变化。
2. 可编码。可以由需求生成设计，由代码反馈到设计。

在有了这两个条件的情况下，我们可以进行增量变更。

### 要素 3：抽象交互 

尽管，我在本文中提出了一套交互相关的 DSL，但是它并不是那么完善。除此呢，在不同的公司里，人们也会自己的一些特定的 UI 设计模式等。所以呢，我们还需要设计一种抽象来描述系统对于用户的交互。

对于一个交互 DSL 来说，它需要做两件事：

1. 描述用户交互。
2. 能与需求进行对应。
3. 能与代码进行对应。

接下来，让我们看看 Unflow DSL 的设计。

## Unflow DSL

基于此呢，我们设计了 Unflow，它具备了如下的三个模式：

1. 三段式交互设计：SEE-DO-REACT
2. 拆分设计：原子设计
3. 布局系统：AutoLayout 与 Flex 布局

除此呢，还有一个非常重要的部分：反馈式设计，我暂时还没有去验证。

## 模式 1 —— 三段式交互设计：SEE-DO-REACT

在日常的软件开发活动中，我们经常会看到不同的三段式表达：

 - BDD 里的：Given - When - Then
 - UI 设计的：显示 - 行动 - 响应
 - HTTP 请求的：request - handle - response
 - 代码的：输入参数 - 处理 - 输出结果
 - 测试的：Arrange-Act-Assert
 - 前端开发的：展示 - 事件 - 响应

对于 UI 设计来说，也存在类似的元素。我尝试着从一堆论文中寻找经验，初始时我尝试以 BDD 的三段式来总结。直到我看到了 Basecamp 的设计师 Ryan 在『[A shorthand for designing UI flows](https://signalvnoise.com/posts/1926-a-shorthand-for-designing-ui-flows)』一文中看到几句话：

 - What the user sees
 - What them do
 - What them see next / what them do next

基于此，添加了一个 React 的选项，即系统要对他们做出什么响应。于是，有了一个简单的 DSL 原型：

```
flow 登录 {
  SEE 首页
    DO 输入密码
    DO [点击] "登录".Button
        REACT 成功: 展示 "Login Success".Toast with ANIMATE(bounce)
        REACT 失败: 展示 "Login Failure".Dialog
}
```

这里的 SEE 对应了用户的所见，DO 则是对应于用户所做，而 REACT 则是相应的可能结果。我们可以将它与需求代码化里的 Given-When-Then 进行一一应对。稍有区别的是，这里在 REACT 里进行了合并，方便后续与 UI 代码进行对应：

1. 调用接口成功的场景下，则显示 Login Success，然后再往下进行操作。
2. 调用接口失败的场景下，则显示 Login Failure 弹窗（Dialog），然后可以添加其它行为。

上述代码中的首页，可以对应到 UI 设计的场景、原型上，对应的按钮（Button） 则是组件使用上的声明。与此同时，基于上述的一系列关键描述，如 Login Success、Login Failure 还创建了对应的 UI 设计上的场景。

##  模式 2 —— 元素拆分：原子设计与元素定义

在设计人员与开发人员协作的过程中，Brad Frost 创建了原子设计的概念：原子设计是一个设计方法论，由五种不同的阶段组合，它们协同工作，以创建一个有层次、计划性的方式来界面系统。

于是，在 Unflow 中，我们依然采用了这个理念，与之对应的设计是：

 - 原子 - library。描述基础、库组件的一些要素。
 - 分子级 - component。描述组件
 - 有机体 - component。描述组件 
 - 模板 - template。
 - 页面 - page

这里的 library、component、template、page 都是 Unflow 中的定义。Unflow 的 DSL 只是提供定义，如下是一个对于颜色规范的定义：

```
library Color {
    Primary {
        label = "Primary"
        value = "#E53935"
    }
    Secondary {
        label = "Blue"
        value = "#1E88E5"
    }
    Third {
        label = "Third"
        value = "#000000"
    }
}
```

Unflow 定义的是这些要素，随后结合其它工具进行转换。在早期 ，我们结合 Node.js 里的 Sketch Constructor 进行了转换，它将转变为两部分：Sketch 里的颜色规范定义，以及前端代码库里的 SCSS 定义。

这种定义方式，对于 `component`、`page` 也是类似的。

```
page HomePage {
    LayoutGrid: 12x
    LayoutId: HomePage
    Router: "/home"      # 由开发定义
}
```

稍有不同的是，我们在设计中加入了一个路由的概念，这个后期可以由开发人员来进行补充。

顺带一说，依旧的这只是 Unflow 的第一个版本，所以在设计上会比较粗糙。

##  模式 3 —— 布局系统：Flex

起先，如果只是站在早期的布局系统的维度之下，我怕是没有胆量去设计一个 DSL。而随着不同领域对于 Flex 布局的统一化程度：

 - 移动端框架 Flutter 中的线性布局（Row、Column）
 - 原生 UI 框架 Druid 采用的 Flex 布局
 - 前端领域采用的 Flex 布局
 - Android 端的 FlexboxLayout
 - ……

那么，对于我们的布局系统来说，自然采用的是类似于 Flex 布局。如此一来，我们只需要考虑一下结合 Apple 的 Auto Layout，就能得到一个勉强可以用的 UI 系统。

而，我最早对于 Layout 体系的想法，语法来源是 [autolayout.js](https://github.com/IjzerenHein/autolayout.js)。一个在前端实现了 AutoLayout 和 Visual Format Language 的布局系统，它的语法如下：

```javascript
H:|[view1(==view2)]-10-[view2]|
V:|[view1,view2]|
```

虽是如此，我设计的第一个版本的布局系统有点不那么实用。关于这一点，我还在自我反思 ，为什么会设计出这么难写的语法：

```
Layout Navigation {
--------------------------------------
| "home" |"detail" | Button("Login") |
--------------------------------------
}
```

在设计布局的时候，想的是：

1. 以 Flex 作为实现方式
2. 以 Table 作为展示形式，方便开发人员维护
3. 支持组件上的参数传递

在这种限制的交错之下，就有了现在这种奇怪的设计。

##  其它 

Unflow 正在设计中，欢迎为 Unflow 提出您的意见：[https://github.com/inherd/unflow](https://github.com/inherd/unflow)

参考资料：

 - [A shorthand for designing UI flows](https://signalvnoise.com/posts/1926-a-shorthand-for-designing-ui-flows)
 - [https://github.com/inherd/unflow](https://github.com/inherd/unflow)
 - [https://github.com/IjzerenHein/autolayout.js](https://github.com/IjzerenHein/autolayout.js)
 - [https://github.com/dorostanian/sushi](https://github.com/dorostanian/sushi)
 - [Understanding Auto Layout](https://developer.apple.com/library/archive/documentation/UserExperience/Conceptual/AutolayoutPG/index.html)
