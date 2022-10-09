package com.frame.basic.base.utils.json

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type


class NullObjectJsonAdapter : JsonDeserializer<Any> {
    private val gson by lazy {
        GsonBuilder().create()
    }
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Any {
        removeNull(json)
        return gson.fromJson(json, typeOfT)
    }

    private fun removeNull(json: JsonElement) {
        if (json.isJsonArray) {
            val asJsonArray = json.asJsonArray
            val nullValueJsonElement: MutableList<JsonElement> = ArrayList()
            for (jsonElement in asJsonArray) {
                if (jsonElement.isJsonNull) {
                    nullValueJsonElement.add(jsonElement)
                } else {
                    removeNull(jsonElement)
                }
            }
            for (jsonElement in nullValueJsonElement) {
                asJsonArray.remove(jsonElement)
            }
        } else if (json.isJsonObject) {
            val asJsonObject = json.asJsonObject
            val nullValueKey: MutableList<String> = ArrayList()
            for ((key, value) in asJsonObject.entrySet()) {
                if (value.isJsonNull) {
                    nullValueKey.add(key)
                } else {
                    removeNull(value)
                }
            }
            for (s in nullValueKey) {
                asJsonObject.remove(s)
            }
        }
    }
}