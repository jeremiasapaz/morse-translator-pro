# Morse Translator Pro ProGuard rules

# Keep Room generated classes
-keep class androidx.room.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase { <init>(); }

# Keep data/entity classes used for serialization & Room
-keep class com.morsetranslatorpro.app.data.** { *; }

# Kotlin coroutines
-dontwarn kotlinx.coroutines.**

# Compose
-dontwarn androidx.compose.**

# Billing
-keep class com.android.billingclient.** { *; }
