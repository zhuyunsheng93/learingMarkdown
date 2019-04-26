# 七种面向对象设计原则
* 单一职责原则
 
     一个类只负责一个功能域中的相应职责（不能将一个类的功能太过庞大，导致复用性不高）
* 开闭原则

   软件实体应对扩展开放，而对修改关闭（随着时间的推移，原有的需求可能会进行改变，在面对新增的需求时，符合开闭规则的应用程序，可以很轻松的进行扩展，而无需修改现有的代码）
   实现开闭原则的关键就是抽象化。
* 里氏代换原则

   所用引用基类对象的地方能够透明的使用其子类对象
   
   ```java
     public interface Father{
       public saySomething();
     }
     public class BigSon implement Father{
       public saySomething(){
        System.out.println("bigSon");
       }
     }
     public class SecondSon implement Father{
       public saySomething(){
       System.out.println("secondSon");
       }
     }
     public class Test{
       public static void test(Father father){
          father.saySomething();
       }
       public static void main (String[] arg){
          Father bigSon = new BigSon();
          Father secondSon = new SecondSon();
          test(bigSon);//这个就是里氏代换
          test(secondSon);
       }
     }
  ```
  * 使用注意点
     1. 子类的方法必须在父类中声明、或者说子类必须实现父类中的所有方法。
     2. 运用里氏代换原则时，尽量把父类设计为抽象或者接口，并让子类进行继承，并实现在父类中声明的方法，运行时子实例替换父实例。而无需进行源码的修改。
     3. java语言中，在编译阶段，Java编译器会检查一个程序是否符合里氏代换原则，只是一个纯语法上的检查。
* 依赖倒转原则
   * 抽象不应该依赖于细节,细节应该依赖于抽象 换言之就是尽量地采用接口编程(减少对象之间的耦合，在进行业务增加，变更时，极大程度上减少代码的修改量。)
   * 三种依赖关系（依赖注入:当一个对象要与其他对象发生依赖关系，通过`抽象`来注入多依赖地对象）： 
      1. 构造函数传递依赖对象
          ```java
           public class UserService implement IUserService{
              private IUserDao iUserDao ;
              public UserService(IUserDao iUserDao){
                 this.iUserDao = iUserDao;
              }
              public void LoginUser(){
                 iUserDao.loginUser();
              }
           }
          ```
     2. 通过setter方法传递依赖对象
        ```java
         public class UserService implement IUserService{
            private IUserDao iUserDao ;
            public setIUserDao(IUserDao iUserDao){
               this.iUserDao = iUserDao;
            }
            public void LoginUser(){
               iUserDao.loginUser();
            }
         }
         ```
      3. 接口依赖
         ```java
              
         ```
* 接口隔离原则

   使用多个专门的接口，不要设计一个群雄荟萃的大接口，降低耦合率。
    
* 合成复用原则

    尽量使用组合对象，而不是继承来达到复用的目的。
* 迪米特法则

    一个软件实体应尽可能少地与其他实体发生相互作用。
    1. 不要和“陌生人”说话
    2. 只与你的直接朋友通信（朋友的定义如下）  
    (1) 当前对象本身  
    (2) 以参数形式传入到当前对象方法中的对象  
    (3) 当前对象的成员变量  
    (4) 如果当前对象的成员对象是一个集合,那么集合中的元素也都是朋友;  
    (5) 当前对象所创建的对象
# 六个创建型模式
  ## 简单工厂模式-Simple Factory Pattern
  * 工厂三兄弟之简单工厂模式(一)
  * 工厂三兄弟之简单工厂模式(二)
  * 工厂三兄弟之简单工厂模式(三)
  * 工厂三兄弟之简单工厂模式(四)
  ## 工厂方法模式-Factory Method Pattern
  * 工厂三兄弟之工厂方法模式(一)
  * 工厂三兄弟之工厂方法模式(二)
  * 工厂三兄弟之工厂方法模式(三)
  * 工厂三兄弟之工厂方法模式(四)
  ## 抽象工厂模式-Abstract Factory Pattern
  * 工厂三兄弟之抽象工厂模式(一)
  * 工厂三兄弟之抽象工厂模式(二)
  * 工厂三兄弟之抽象工厂模式(三)
  * 工厂三兄弟之抽象工厂模式(四)
  * 工厂三兄弟之抽象工厂模式(五)
  ## 单例模式-Singleton Pattern
  * 确保对象的唯一性——单例模式 (一)
  * 确保对象的唯一性——单例模式 (二)
  * 确保对象的唯一性——单例模式 (三)
  * 确保对象的唯一性——单例模式 (四)
  * 确保对象的唯一性——单例模式 (五)
  ## 原型模式-Prototype Pattern
  * 对象的克隆——原型模式(一)
  * 对象的克隆——原型模式(二)
  * 对象的克隆——原型模式(三)
  * 对象的克隆——原型模式(四)
  ## 建造者模式-Builder Pattern
  * 复杂对象的组装与创建——建造者模式(一)
  * 复杂对象的组装与创建——建造者模式(二)
  * 复杂对象的组装与创建——建造者模式(三)
