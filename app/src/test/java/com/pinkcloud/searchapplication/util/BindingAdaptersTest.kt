package com.pinkcloud.searchapplication.util

import android.app.Application
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.pinkcloud.domain.model.Document
import com.pinkcloud.searchapplication.ui.storage.DocumentAdapter
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BindingAdaptersTest {

    private lateinit var application: Application
    private val fakeDocument =
        Document("https://fake_thumbnail", System.currentTimeMillis().toString())

    @Before
    fun setup() {
        application = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun setEnableBySelectedDocumentsSize_withEmptyDocuments() {
        val button = Button(application)
        val emptyDocument = mapOf<String, Document>()
        button.setEnableBySelectedDocumentsSize(emptyDocument)
        assertEquals(button.isEnabled, false)
    }

    @Test
    fun setEnableBySelectedDocumentsSize_withNotEmptyDocuments() {
        val button = Button(application)

        val documents = mapOf(
            fakeDocument.thumbnailUrl!! to fakeDocument
        )
        button.setEnableBySelectedDocumentsSize(documents)
        assertEquals(button.isEnabled, true)
    }

    @Test
    fun setItems() {
        val list = RecyclerView(application)
        val documents = listOf(fakeDocument)
        list.adapter = DocumentAdapter()
        setItems(list, documents)
        assertEquals(documents.size, list.adapter!!.itemCount)
    }

    @Test
    fun setEmptyVisibility_withEmptyDocuments() {
        val textView = TextView(application)
        val documents = listOf<Document>()
        setEmptyVisibility(textView, documents)
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    fun setEmptyVisibility_withNotEmptyDocuments() {
        val textView = TextView(application)
        val documents = listOf(fakeDocument)
        setEmptyVisibility(textView, documents)
        assertEquals(View.GONE, textView.visibility)
    }
}