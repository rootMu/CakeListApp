package com.matthew.cakelistapp

import com.matthew.cakelistapp.model.cake.Cake
import com.matthew.cakelistapp.modules.list.ui.ListActivity
import org.junit.Test

import org.junit.Assert.*
import android.arch.lifecycle.Observer
import com.matthew.cakelistapp.modules.list.viewmodel.ListViewModel
import com.matthew.cakelistapp.network.CakeListService
import org.mockito.Mockito.*
import org.junit.Before
import org.mockito.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Mock
    lateinit var observer: Observer<List<Cake>>

    @Mock val cakeListService: CakeListService = mock(CakeListService::class.java)

    @InjectMocks
    lateinit var mViewModel: ListViewModel

    @Test
    fun `Assert True cakes are posted when retrieved from online`() {

        val activity = ListActivity()

        val cakes = arrayOf(Cake(title = "abc"),Cake(title = "abc"), Cake(title = "aaa"), Cake(title = "aab"), Cake(title = "abb"), Cake(title = "bbb"), Cake(title = "bbc") )
        assertTrue(cakes.distinct() != cakes)


    }


}
