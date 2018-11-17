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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.mediaseed.TextHelpers.capitalizeFirstLetter
import com.raywenderlich.mediaseed.TextHelpers.trimNewLines

/**
 * Provide views to RecyclerView with data from media.
 *
 * Initialize the dataset of the Adapter.
 */
class CustomAdapter(private val media: ArrayList<Media>) :
    RecyclerView.Adapter<CustomAdapter.MediaViewHolder>() {

    /**
     * Provide a reference to the views
     */
    class MediaViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.mediaTitle)
        val type: TextView = v.findViewById(R.id.mediaType)
        val description: TextView = v.findViewById(R.id.mediaDescription)
    }

    // Create new views (invoked by layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MediaViewHolder {
        // Create a new view.
        val vCardView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_item, viewGroup, false)

        return MediaViewHolder(vCardView)
    }

    // Replace the contents of a view (invoked by layout manager)
    override fun onBindViewHolder(viewHolder: MediaViewHolder, position: Int) {

        // Get element from dataset at this position and replace the contents of the view with that element
        viewHolder.title.text = media[position].name
        viewHolder.type.text = media[position].type?.let { capitalizeFirstLetter(it) }
        viewHolder.description.text = media[position].description?.let { trimNewLines(it) }
    }

    // Return the size of your dataset (invoked by layout manager)
    override fun getItemCount() = media.size
}