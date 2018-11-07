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
	        implementation 'com.github.squirrelhuan:QuickDevelop:Tag'
	}
```

## 快速使用
  application类需要继承ApplicationParent.java
### SharedPreferencesHelper
```
获取 SharedPreferencesHelper.getInstance().getBoolean("test",false);
保存 SharedPreferencesHelper.getInstance().putBoolean("test",false);
```
