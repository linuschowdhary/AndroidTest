package com.mty.demo;




/**
 * Demo列表
 * @author MaTianyu
 * 2013-12-11下午8:14:20
 */
public enum Demo {
	DanymicView("动态视图"), Scheme("scheme启动"), LoadAllClass("加载全部类"), NumLock("密码锁"),
	Http("网络连接"),
	MusicPlayer("音乐播放"),
	JsonTest("json性能测试"),
	SwipeRefresh("SwipeRefresh"),
	PullToRefresh("PullToRefresh"),
	/* 创建型模式 */
	FactoryMethod("工厂方法"), AbstractFactory("抽象工厂"), Builder("建造者"), Singleton("单例模式"), Prototype("原型模式"),
	/* 结构型模式 */
	Adapter("适配器"), Bridge("桥接模式"), Composite("组合模式"), Decorator("装饰模式"), Facade("外观模式"), Flyweight("享元模式"), Proxy(
			"代理模式"),
	/* 行为型模式 */
	ChainOfResponsibility("责任链"), Command("命令模式"), Iterator("迭代器"), Interpreter("解释器"), Mediator("中介者"), Memento("备忘录"), Observer(
			"观察者"), State("状态模式"), Strategy("策略模式"), TemplateMethod("模板方法"), Vistor("访问者");

	private int id = 0;
	private String name;
	private static int autoIncrement = 0;

	private Demo(String name) {
		this.id = autoIncrement();
		this.name = name;
	}

	private static int autoIncrement() {
		return autoIncrement++;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}