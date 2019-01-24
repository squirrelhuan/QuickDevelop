#ifndef NDK_GUARD_NATIVE_LIB_H
#define NDK_GUARD_NATIVE_LIB_H

#endif //NDK_GUARD_NATIVE_LIB_H
#include <sys/select.h>
#include <unistd.h>
#include <sys/socket.h>
#include <pthread.h>
#include <signal.h>
#include <sys/wait.h>
#include <android/log.h>
#include <sys/types.h>
#include <sys/un.h>
#include <errno.h>
#include <stdalign.h>
#include <linux/signal.h>
#include <unistd.h>
#include <sys/un.h>

#define LOG_TAG "tuch"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

void child_do_work();
void child_listen_msg();
int child_create_channel();