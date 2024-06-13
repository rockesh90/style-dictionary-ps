/**
 * Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule
import models.Property
import models.StyleDictionaryNode
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.*

object StyleDictionaryHelper {
  private var MAPPER: ObjectMapper? = null
  private var DICTIONARY_JSON_STRING: String = ""
  var DICTIONARY_JSON: JSONObject? = null

  fun loadButtonBackgroundColor(): Color {
    val property = getProperty(path = arrayListOf("core", "colors", "white"))
    return Color(android.graphics.Color.parseColor(property.value))
  }

  fun labelTextColor(): Color {
    val property = getProperty(path = arrayListOf("color", "brand", "label", "light"))
    return Color(android.graphics.Color.parseColor(property.value))
  }

  fun loadBackgroundColor(): Color {
    val property = getProperty(path = arrayListOf("color", "base", "card"))
    return Color(android.graphics.Color.parseColor(property.value))
  }

  fun loadMediumFontSize(): TextUnit {
    val property = getProperty(path = arrayListOf("size", "font", "medium"))
    val fontUnitTextUnit = property.value.substring(0, property.value.length - 2).toFloat()
    return TextUnit(fontUnitTextUnit, TextUnitType.Sp)
  }

  fun loadLargeFontSize(): TextUnit {
    val property = getProperty(path = arrayListOf("size", "font", "large"))
    val fontUnitTextUnit = property.value.substring(0, property.value.length - 2).toFloat()
    return TextUnit(fontUnitTextUnit, TextUnitType.Sp)
  }

  fun loadJSON(context: Context) {
    DICTIONARY_JSON_STRING = loadJsonFromAsset("data/properties.json", context)
    try {
      DICTIONARY_JSON = JSONObject(DICTIONARY_JSON_STRING)
    } catch (e: JSONException) {
      e.printStackTrace()
    }
  }

  fun updateJson(json: String) {
    try {
      DICTIONARY_JSON = JSONObject(json)
    } catch (e: JSONException) {
      e.printStackTrace()
    }
  }

  fun getNodeArrayForObject(json: JSONObject?): ArrayList<StyleDictionaryNode> {
    val nodeList = ArrayList<StyleDictionaryNode>()
    val keys = json!!.keys()
    while (keys.hasNext()) {
      val key = keys.next()
      var isLeaf = false
      var count = 0
      try {
        val jsonNode = json.getJSONObject(key)
        if (jsonNode.has("value")) {
          isLeaf = true
        } else {
          count = jsonNode.length()
        }
      } catch (e: JSONException) {
        e.printStackTrace()
      }
      nodeList.add(StyleDictionaryNode(key, count, isLeaf))
    }
    return nodeList
  }

  fun getArrayAtPath(path: List<String>): ArrayList<StyleDictionaryNode> {
    val nodeList = ArrayList<StyleDictionaryNode>()
    var json = DICTIONARY_JSON
    try {
      for (pathPart in path) {
        json = json!!.getJSONObject(pathPart)
      }
      return getNodeArrayForObject(json)
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    return nodeList
  }

  private fun getProperty(path: ArrayList<String>, jsonObject: JSONObject?): Property {
    var json = jsonObject
    val property: Property
    try {
      for (pathPart in path) {
        json = json!!.getJSONObject(pathPart)
      }
      return if (json!!.has("value")) {
        property = MAPPER!!.convertValue(json, Property::class.java)
        property
      } else {
        throw RuntimeException("Property doesn't exist")
      }
    } catch (e: JSONException) {
      e.printStackTrace()
      throw e
    }
  }

  fun getProperty(path: ArrayList<String>): Property {
    return getProperty(path, DICTIONARY_JSON)
  }

  fun getArrayOfProps(path: ArrayList<String>): ArrayList<Property> {
    val propertyList = ArrayList<Property>()
    var json = DICTIONARY_JSON
    try {
      for (pathPart in path) {
        json = json!!.getJSONObject(pathPart)
      }
      val keys = json!!.keys()
      while (keys.hasNext()) {
        val key = keys.next()
        val jsonProperty = json.getJSONObject(key)
        if (jsonProperty.has("value")) {
          val property = MAPPER!!.convertValue(jsonProperty, Property::class.java)
          propertyList.add(property)
        }
      }
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    return propertyList
  }

  @Suppress("SameParameterValue")
  private fun loadJsonFromAsset(filename: String, context: Context): String {
    val inputStream = context.assets.open(filename)
    val size = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()
    return String(buffer, Charset.forName("UTF-8"))
  }

  init {
    MAPPER = ObjectMapper().registerModule(JsonOrgModule())
  }
}
