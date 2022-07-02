# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
#-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
#-keep class com.google.samples.apps.nowinandroid.core.network { *; }
-keep class com.google.samples.apps.nowinandroid.di.** { *; }
-keep class com.google.samples.apps.nowinandroid.core.network.di.DispatchersModule { *; }
-keep class com.google.samples.apps.nowinandroid.core.network.** { *; }
-keep class com.google.samples.apps.nowinandroid.core.model.** { *; }
-keep class com.google.samples.apps.nowinandroid.core.network.Dispatcher { *; }
-keep class com.google.samples.apps.nowinandroid.core.network.NiaDispatchers { *; }