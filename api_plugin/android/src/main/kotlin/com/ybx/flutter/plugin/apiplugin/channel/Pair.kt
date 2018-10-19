/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ybx.flutter.plugin.apiplugin.channel

/**
 * Pair of two elements.
 */
internal class Pair<A, B> private constructor(val first: A?, val second: B?) {

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (first?.hashCode() ?: 0)
        result = prime * result + (second?.hashCode() ?: 0)
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        if (javaClass != obj.javaClass) {
            return false
        }
        val other = obj as Pair<*, *>?
        if (first == null) {
            if (other!!.first != null) {
                return false
            }
        } else if (first != other!!.first) {
            return false
        }
        if (second == null) {
            if (other.second != null) {
                return false
            }
        } else if (second != other.second) {
            return false
        }
        return true
    }

    companion object {

        fun <A, B> of(first: A, second: B): Pair<A, B> {
            return Pair(first, second)
        }
    }
}
