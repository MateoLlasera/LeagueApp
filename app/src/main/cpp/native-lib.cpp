//
// Created by mateo on 18/04/2021.
//
#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_canonicalexamples_leagueApp_Keys_apiKey(JNIEnv *env, jobject object) {
    std::string api_key = "your_api_key_goes_here";
    return env->NewStringUTF(api_key.c_str());
}