

# 我的第一个GitHub项目

这是项目 [domain-mapping](https://github.com/ychuangxiao/domain-mapping) 
欢迎访问。

灵感来源
-----------------------------------
  借鉴大神[WonderCsabo](https://github.com/excilys/androidannotations)，比较靠谱的注解框架，我相信你会喜欢它的。

说明：<br />
1、主要是解放生产力，让编译器去折腾吧。<br />
2、利用注解，编译器在<b>编译</b>时，让Web api pojo与View model之间实现映射.动态生成代码。下面看个例子：<br />

```java  
   
public  class DemoClass {
         //常规的做法,
		 //UserEntity(对应Api pojo)
		 UserEntity userEntity = new UserEntity();
		 userEntity.setUserName("banditcat");
		 
		 //Activity中，UserViewModel对应视图模型
		 
		 UserViewModel userViewModel = new UserViewModel();
		 userViewModel.setUserName(userEntity.getUserName());
		 
		 //明天再写吧
    }
   
```

