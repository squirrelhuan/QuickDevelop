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
  application文件继承ApplicationParent
### SharedPreferencesHelper
```
获取 SharedPreferencesHelper.getInstance().getBoolean("test",false);
保存 SharedPreferencesHelper.getInstance().putBoolean("test",false);
```
