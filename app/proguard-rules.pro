# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/nils_reichert/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn android.support.**
-dontwarn com.ibm.icu.**
-dontwarn com.google.android.exoplayer.**
-dontwarn com.android.tv.tuner.**
-dontwarn com.android.tv.dvr.**
# This is due to legacy API katniss is referencing. Seems safe.
-dontwarn com.google.android.volley.**
-dontwarn com.google.android.common.**
-keepclasseswithmembers class com.android.tv.tuner.*DataSource {
    int readAt(long, byte[], int, int);
    long getSize();
    void close();
}

# Configuration of proguard via annotations. Apply them to
# the elements of your program not only to ensure correct proguard
# functionality, but to document non-obvious entry points to your code to make
# it survive refactorings.
# Annotations are implemented as attributes, so we have to explicitly keep them.
# Catch all which encompasses attributes like RuntimeVisibleParameterAnnotations
# and RuntimeVisibleTypeAnnotations
-keepattributes RuntimeVisible*Annotation*
# JNI is an entry point that's hard to keep track of, so there's
# an annotation to mark fields and methods used by native code.
# Keep the annotations that proguard needs to process.
-keep class com.android.tv.common.annotation.UsedBy*
# Just because native code accesses members of a class, does not mean that the
# class itself needs to be annotated - only annotate classes that are
# referenced themselves in native code.
-keep @com.android.tv.common.annotation.UsedBy* class *
-keepclassmembers class * {
    @com.android.tv.common.annotation.UsedBy* *;
}
# For tests

# Grpc used by epg via reflection
# Don't warn about checkerframework in Android proguard
-dontwarn org.checkerframework.**
