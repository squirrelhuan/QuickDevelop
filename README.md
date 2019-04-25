# QuickDevelop
## 引入依赖
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
```
dependencies {
	        implementation 'com.github.squirrelhuan:QuickDevelop:1.1.23'
	}
```

## 快速使用
  application类需要继承ApplicationParent.java
### SharedPreferencesHelper
```
获取 SharedPreferencesHelper.getInstance().getBoolean("test",false);
保存 SharedPreferencesHelper.getInstance().putBoolean("test",false);
```

### 小组件

![Alt](https://raw.githubusercontent.com/squirrelhuan/QuickDevelop/master/app/src/main/assets/image/component/ToogleButton_GIF.gif)


### MultiRecycleContainer(多个RecycleView的复杂界面)

![Alt](https://raw.githubusercontent.com/squirrelhuan/QuickDevelop/QuickDevelopX/app/src/main/assets/image/component/QD_MultiRecycle_GIF.gif)
