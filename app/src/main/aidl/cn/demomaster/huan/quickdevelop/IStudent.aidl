// IStudent.aidl
package cn.demomaster.huan.quickdevelop;
// 此处必须注意，一定要手动导入自定义的类
import cn.demomaster.huan.quickdevelop.aidl.StudtInfo;
// Declare any non-default types here with import statements

interface IStudent {
	void addStudentInfoReq(in StudtInfo studtInfo);
}
