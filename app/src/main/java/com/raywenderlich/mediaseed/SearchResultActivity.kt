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

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.raywenderlich.mediaseed.TextHelpers.formattedResultTitleText
import com.raywenderlich.mediaseed.TextHelpers.replaceSpaces
import com.raywenderlich.mediaseed.models.ResponseData
import com.squareup.moshi.JsonAdapter
import kotlinx.android.synthetic.main.activity_search_result.*
import java.io.IOException

class SearchResultActivity : AppCompatActivity() {
    private lateinit var adapter: JsonAdapter<ResponseData>
    private lateinit var mediaResponse: ResponseData
    private lateinit var resultItemText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var mediaList: ArrayList<Media> = ArrayList()
    private lateinit var searchString: String
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        resultItemText = findViewById(R.id.searchItemText)

        // Retrieve the search string from the MainActivity
        searchString = replaceSpaces(intent.getStringExtra(Constants.INTENT_KEY))

        // Show progress bar while results load
        progress = findViewById(R.id.progressBar)
        progress.isIndeterminate = true
        progress.visibility = ProgressBar.VISIBLE

        viewManager = LinearLayoutManager(this)

        viewAdapter = CustomAdapter(mediaList)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

            requestMedia(searchString)
        }
    }

    private fun requestMedia(enteredMedia: String) {
        val requestQueue = Volley.newRequestQueue(this)
        val url =
            Constants.TASTE_DIVE_BASE_URL + Constants.QUERY_KEY + enteredMedia + Constants.INFO_LEVEL + Constants.TASTE_DIVE_API_KEY

        val request = StringRequest(
            Request.Method.GET
            , url
            , Listener<String> { response: String ->
                val moshi = MoshiBuilder.moshiInstance
                adapter = moshi.adapter(ResponseData::class.java)

                try {
                    mediaResponse = adapter.fromJson(response)!!

                    // Either user hasn't entered a vaild key or hourly quota has been reached
                    if (mediaResponse.similar == null) {
                        Toast.makeText(
                            this@SearchResultActivity, getString(R.string.no_result_error), Toast.LENGTH_LONG
                        ).show()
                        progressBar.visibility = ProgressBar.INVISIBLE
                    } else {
                        for ((count, item) in mediaResponse.similar?.results?.withIndex()!!) {
                            mediaList.add(
                                Media(
                                    mediaResponse.similar?.results?.get(count)?.name,
                                    mediaResponse.similar?.results?.get(count)?.type,
                                    mediaResponse.similar?.results?.get(count)?.description
                                )
                            )
                        }

                        resultItemText.text = formattedResultTitleText(
                            mediaResponse.similar?.info?.get(0)?.name,
                            mediaResponse.similar?.info?.get(0)?.type,
                            this
                        )

                        // Don't show title until it is properly formatted
                        resultItemText.visibility = View.VISIBLE

                        // Hide the progress bar now that data is loaded
                        progress.visibility = ProgressBar.INVISIBLE
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }, Response.ErrorListener { error ->
                error.printStackTrace()
            }
        )
        requestQueue.add(request)
    }
}