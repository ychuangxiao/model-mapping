

# 我的第一个GitHub项目

这是项目 [model-mapping](https://github.com/ychuangxiao/model-mapping)
欢迎访问。

灵感来源
-----------------------------------
  借鉴大神[WonderCsabo](https://github.com/excilys/androidannotations)，比较靠谱的注解框架，我相信你会喜欢它的。

说明：<br />
1、主要是解放生产力，让编译器去折腾吧。<br />
2、利用注解，编译器在<b>编译</b>时，让Web api pojo与View model之间实现映射.动态生成代码。下面看个例子：<br />

```java  
   
public  class DemoClass {

                 //UserEntity(对应Api pojo)
                 UserEntity userEntity = new UserEntity();

                 userEntity.setUserId(1000L);
                 userEntity.setUserName("cat");
                 userEntity.setFullName("banditcat");


                 //Activity中，UserModel对应视图模型
                 //常规的做法


                 UserModel userModel = new UserModel();

                 userModel.setStringUserId(userEntity.getUserId().toString());//还需要判断类型
                 userModel.setUserName(userEntity.getUserName());
                 userModel.setFullName2(userEntity.getFullName());


                 //新的做法


                 userModel = UserModelMapper.getInstance().transformer(userEntity);//主要是把这些硬编码的方式,通过注解的方式减轻体力
    }
   
```


-----------------------------------
骚年,继续优化吧.