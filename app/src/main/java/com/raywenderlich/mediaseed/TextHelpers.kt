/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.mediaseed

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan


object TextHelpers {

    fun capitalizeFirstLetter(text: String?): String? {
        return text?.capitalize()
    }

    // Trim leading and trailing new lines '\n'
    // Note: New lines within the text (e.g. paragraphs) are retained
    fun trimNewLines(text: String): String {
        return text.trim('\n')
    }

    // Bold the name of the search item
    private fun boldText(fullText: String?, boldText: String?): SpannableStringBuilder {
        val builder = SpannableStringBuilder()

        if (boldText?.trim(' ')?.isNotEmpty()!!) {
            val startingIndex = boldText.let { fullText?.indexOf(it) }
            val endingIndex = startingIndex?.plus(boldText.length)

            builder.append(fullText)
            if (startingIndex != null && endingIndex != null) {
                startingIndex.let { endingIndex.let { it1 -> builder.setSpan(StyleSpan(Typeface.BOLD), it, it1, 0) } }
            }
        } else {
            builder.append(fullText)
        }
        return builder
    }

    fun formattedResultTitleText(resultName: String?, resultType: String?, context: Context): SpannableStringBuilder {
        val formattedString: SpannableStringBuilder

        val searchResultString: String = String.format(
            context.getString(R.string.results_for_text)
            , resultName
            , capitalizeFirstLetter(resultType)
        )
        formattedString = boldText(searchResultString, resultName)

        return formattedString
    }

    // Replace the spaces names with multiple words with %20 so the request url will work
    fun replaceSpaces(searchText: String): String {
        return searchText.replace(" ", "%20")
    }
}
