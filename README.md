# CustomView
Android Custom View

## 自定义View
CarView.java

主要使用了 PathMeasure
* 定义：用来测量 Path 的类，可以理解成为专门对 Path 每一个点获取信息的类
* 优势：计算某一点的正切、余切，采取一小段 Path 单独拿出来进行绘制

参考

[通过Math.atan2 计算角度](https://www.jianshu.com/p/9817e267925a)  
[Matrix的set,pre,post调用顺序](https://www.cnblogs.com/dyllove98/archive/2013/06/12/3132802.html)  
[对Matrix中preTranslate()和postTranslate()的理解](https://blog.csdn.net/programchangesworld/article/details/49078387)  
[Canvas对绘制的辅助clipXXX()范围裁切和Matri几何变换](https://hencoder.com/ui-1-4/)

## 组合控件
Toolbar.java

## 流式布局
FlowLayout.java

## 手写RecyclerView

架构中核心组件：
* 回收池：能回收任意 Item 控件，并返回符合类型的Item控件；比如 onBinderViewHolder 方法中的第一个参数是从回收池中返回的。
* 适配器：Adapter 接口，经常辅助 RecyclerView 实现列表展示；适配器模式，将用户界面展示与交互分离。
* RecyclerView：是做触摸事件的交互，主要实现边界值判断；根据用户的触摸反馈，协助回收池对象与适配器对象之间的工作。

## SVG
[SVG地图下载地址](https://www.amcharts.com/download/)

## 阿里 VLayout
[Github](https://github.com/alibaba/vlayout)