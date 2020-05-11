#include <jni.h>
#include <string>
#include <string.h>
#include "native_lib.h"


extern "C" JNIEXPORT jstring JNICALL
Java_cn_demomaster_huan_quickdeveloplibrary_jni_JNITest_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


const char *userId;
const char *packageName;
const char *serviceName;
/**
 * PATH=/dada/dada/包名/my.sock
 */
char *PATH;
int m_child;

void child_do_work() {
    //开启socket
    if (child_create_channel()) {
        child_listen_msg();
    }

}

void child_listen_msg() {
    fd_set rfds;
    //时间  3秒
    struct timeval timeout = {3, 0};
    while (1) {
        //清空内容
        FD_ZERO(&rfds);

        FD_SET(m_child, &rfds);

        //选择范围 一般+1;

        int r = select(m_child + 1, &rfds, NULL, NULL, &timeout);



        if (r > 0) {
            char pkg[256] = {0};
            //保证所读到的信息是指定apk客户端的信息
            if (FD_ISSET(m_child, &rfds)) {
                //阻塞函数
                int result = read(m_child, pkg, sizeof(pkg));
                //一旦和apk断开  就执行下面代码
                /* 连接 str1 和 str2 */
                char str[100];
                strcat (str,packageName);
                strcat (str,"/");
                strcat (str,serviceName);

                LOGE("apk 开启服务 %s" , str);
                //"cn.demomaster.huan.cn.demomaster.huan.quickdevelop/cn.demomaster.huan.quickdeveloplibrary.jni.ProcessService"
                //开启服务
                execl("am", "am", "startservice", "--user", userId,
                      str , NULL);
                break;
            }
        }
    }
}

/**
 * 开启socket 服务
 * @return
 */
int child_create_channel() {

    int listenfd = socket(AF_LOCAL, SOCK_STREAM, 0);
    struct  sockaddr_un addr;
    unlink(PATH);
    LOGE("unlink PATH=%s",PATH);
    memset(&addr, 0, sizeof(sockaddr));
    addr.sun_family = AF_LOCAL;
    strcpy(addr.sun_path, PATH);
    int connfd;
    if (bind(listenfd, (const sockaddr *) &addr, sizeof(sockaddr_un)) < 0) {
        LOGE("绑定错误");
        return 0;
    }
    LOGE("unlink nest");

    //最多同时连接5个
    listen(listenfd, 5);


    //while 保证宿主连接成功
    while (1) {
        //返回值 客户端的地址 //阻塞函数
        if ((connfd = accept(listenfd, NULL, NULL)) < 0) {
            if (errno == EINTR) {
                LOGE("读取错误EINTR");
                continue;
            } else {
                LOGE("读取错误");
                return 0;
            }
        }


        m_child = connfd;
        LOGE("apk 父进程连接上 %d" , m_child);
        break;
    }
    return 1;
}

char ch[100];
extern "C"
JNIEXPORT void JNICALL
Java_cn_demomaster_huan_quickdeveloplibrary_jni_Watcher_createWatcher(JNIEnv *env, jobject instance, jstring userId_,jstring packageName_,jstring serviceName_) {
userId = env->GetStringUTFChars(userId_, 0);
packageName = env->GetStringUTFChars(packageName_, 0);
serviceName = env->GetStringUTFChars(serviceName_, 0);
strcpy (ch,"/data/data/");
strcat (ch,packageName);
strcat (ch,"/my.sock");
PATH = ch;
LOGE("apk PATH %s,%s" , PATH,packageName);
//

//开双进程
pid_t pid = fork();
if (pid < 0) {
//fork失败
} else if (pid == 0) {
//子线程   守护进程
child_do_work();

} else if (pid > 0) {
//主线程
}
env->ReleaseStringUTFChars(userId_, userId);
}


extern "C"
JNIEXPORT void JNICALL
Java_cn_demomaster_huan_quickdeveloplibrary_jni_Watcher_connectMonitor(JNIEnv *env, jobject instance) {
int socked;
while (1) {

LOGE("客户端  开始进行连接了");
socked = socket(AF_LOCAL, SOCK_STREAM, 0);
if (socked < 0) {
LOGE("连接失败0");
return;
}
struct  sockaddr_un addr;

memset(&addr, 0, sizeof(sockaddr));
addr.sun_family = AF_LOCAL;
strcpy(addr.sun_path, PATH);

if (connect(socked, (const sockaddr *) &addr, sizeof(sockaddr_un)) < 0) {
LOGE("连接失败");
close(socked);
sleep(1);
//在来下一次尝试连接
continue;
}
LOGE("连接成功");
break;
}

}