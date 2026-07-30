package com.example.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromLeadStage(stage: LeadStage): String = stage.name

    @TypeConverter
    fun toLeadStage(value: String): LeadStage = try {
        LeadStage.valueOf(value)
    } catch (e: Exception) {
        LeadStage.NEW
    }

    @TypeConverter
    fun fromLeadSource(source: LeadSource): String = source.name

    @TypeConverter
    fun toLeadSource(value: String): LeadSource = try {
        LeadSource.valueOf(value)
    } catch (e: Exception) {
        LeadSource.WEB_FORM
    }

    @TypeConverter
    fun fromLeadPriority(priority: LeadPriority): String = priority.name

    @TypeConverter
    fun toLeadPriority(value: String): LeadPriority = try {
        LeadPriority.valueOf(value)
    } catch (e: Exception) {
        LeadPriority.MEDIUM
    }

    @TypeConverter
    fun fromActivityType(type: ActivityType): String = type.name

    @TypeConverter
    fun toActivityType(value: String): ActivityType = try {
        ActivityType.valueOf(value)
    } catch (e: Exception) {
        ActivityType.NOTE
    }
}
