package com.assignment.chessboard.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import com.assignment.chessboard.data.local.dao.ChessboardStateDao
import com.assignment.chessboard.data.local.database.AppDatabase
import com.assignment.chessboard.data.local.entity.ChessboardState
import com.assignment.chessboard.domain.utils.knightPathFinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ChessboardViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ChessboardViewModel

    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var mockDao: ChessboardStateDao

    @Mock
    private lateinit var mockDatabase: AppDatabase

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(mockDatabase.chessboardStateDao()).thenReturn(mockDao)

        whenever(application.applicationContext).thenReturn(application)

        mockDatabase = Room.inMemoryDatabaseBuilder(application, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        viewModel = ChessboardViewModel(application)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testInitialization() = runBlockingTest {
        val mockState = ChessboardState(
            boardSize = 8,
            startX = 0,
            startY = 0,
            endX = 7,
            endY = 7,
            maxMoves = 3,
            isDarkTheme = false
        )

        whenever(mockDao.getLastState()).thenReturn(mockState)

        viewModel = ChessboardViewModel(application)

        Assert.assertEquals(8, viewModel.boardSize.value)
        Assert.assertEquals(3, viewModel.maxMoves.value)
        Assert.assertEquals(0, viewModel.startX)
        Assert.assertEquals(0, viewModel.startY)
        Assert.assertEquals(7, viewModel.endX)
        Assert.assertEquals(7, viewModel.endY)
        Assert.assertEquals(false, viewModel.isDarkTheme.value)
    }

    @Test
    fun testUpdateBoardSize() {
        viewModel.updateBoardSize(10)
        Assert.assertEquals(10, viewModel.boardSize.value)
    }

    @Test
    fun testUpdateMaxMoves() {
        viewModel.updateMaxMoves(5)
        Assert.assertEquals(5, viewModel.maxMoves.value)
    }

    @Test
    fun testUpdateTheme() {
        viewModel.updateTheme(true)
        Assert.assertEquals(true, viewModel.isDarkTheme.value)
    }

    @Test
    fun testOnTileSelectedStartAndEnd() {
        viewModel.onTileSelected(0, 0)
        Assert.assertEquals(0, viewModel.startX)
        Assert.assertEquals(0, viewModel.startY)

        viewModel.onTileSelected(7, 7)
        Assert.assertEquals(7, viewModel.endX)
        Assert.assertEquals(7, viewModel.endY)
    }

    @Test
    fun testResetBoard() {
        viewModel.resetBoard()
        Assert.assertEquals(8, viewModel.boardSize.value)
        Assert.assertEquals(3, viewModel.maxMoves.value)
        Assert.assertEquals(-1, viewModel.startX)
        Assert.assertEquals(-1, viewModel.startY)
        Assert.assertEquals(-1, viewModel.endX)
        Assert.assertEquals(-1, viewModel.endY)
        Assert.assertTrue(viewModel.paths.value!!.isEmpty())
        Assert.assertFalse(viewModel.noSolutionFound.value!!)
    }

    @Test
    fun testCalculatePaths() = runBlockingTest {
        val mockPath = listOf(listOf(Pair(0, 0), Pair(1, 2)))
        val pathFinder = knightPathFinder(8, 0, 0, 7, 7, 3)
        whenever(pathFinder).thenReturn(mockPath)

        Mockito.mockStatic(Log::class.java).use { mock ->
            mock.`when` { Log.d(any(), any()) }.thenReturn(0)

            viewModel.onTileSelected(0, 0)
            viewModel.onTileSelected(7, 7)

            Assert.assertFalse(viewModel.noSolutionFound.value!!)
            Assert.assertEquals(mockPath, viewModel.paths.value)
        }
    }

    @Test
    fun testExceptionHandlingInCalculatePaths() = runBlockingTest {
        val observer = Observer<Boolean> {}
        try {
            viewModel.isLoading.observeForever(observer)

            Mockito.mockStatic(Log::class.java).use { mock ->
                mock.`when` { Log.d(any(), any()) }.thenReturn(0)

                whenever(
                    knightPathFinder(
                        8,
                        -1,
                        -1,
                        -1,
                        -1,
                        3
                    )
                ).thenThrow(RuntimeException("Test Exception"))
                viewModel.onTileSelected(0, 0)
                viewModel.onTileSelected(7, 7)

                Assert.assertFalse(viewModel.isLoading.value!!)
                Assert.assertTrue(viewModel.noSolutionFound.value!!)
            }
        } finally {
            viewModel.isLoading.removeObserver(observer)
        }
    }
}
