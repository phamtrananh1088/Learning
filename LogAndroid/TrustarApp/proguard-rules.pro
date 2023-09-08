-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    public static void checkExpressionValueIsNotNull(...);
    public static void checkNotNullExpressionValue(...);
    public static void checkReturnedValueIsNotNull(...);
    public static void checkFieldIsNotNull(...);
    public static void checkParameterIsNotNull(...);
}
-assumenosideeffects class android.util.Log {
    public static <methods>;
}

-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

-keepclassmembers,allowobfuscation class * {
    void $$clinit();
}

-keep,allowobfuscation class com.bumptech.glide.GeneratedAppGlideModuleImpl
-keep,allowobfuscation public class * implements com.bumptech.glide.module.GlideModule
-keep,allowobfuscation public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keepclassmembers,allowobfuscation class * {
     @annotation.Find <init>(...);
     @annotation.Keep <init>(...);
}

## Thanks to https://github.com/ZeevoX/Pie
-keepnames class com.zeevox.pie.paint.*
