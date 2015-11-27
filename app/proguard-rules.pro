-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod,JavascriptInterface

#忽略警告 也可以用-ignorewarnings
-dontwarn

# 保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
native <methods>;
}
# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}
# 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
public void *(android.view.View);
}

-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}


#如果引用了v4或者v7包
-dontwarn android.support.**

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#eventbus
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

#不混淆本包的R
#-keepclassmembers class com.bryan.commondemo.R$*{
#    public static final int *;
#}

#不混淆所有R
#-keepclassmembers class **.R$*{
#    public static final int *;
#}

#注解不混淆
-keep class * extends java.lang.annotation.Annotation { *; }
#不混淆model
-keep class com.bryan.commondemo.model.** { *; }

#不混淆js接口 （有了NoProguard可以不用）
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

#不混淆注解保护的类
-keep  @com.bryan.commondemo.model.NoProguard  class *{ *; }

-dontwarn okio.**


#友盟
-dontwarn com.umeng.**
-keep class com.umeng.** { *; }
-dontwarn u.aly.**
-keep class u.aly.** { *; }
-dontwarn com.tencent.**
-keep  class com.tencent.** {*;}
-dontwarn com.sina.sso.**
-keep  class com.sina.sso.** {*;}



