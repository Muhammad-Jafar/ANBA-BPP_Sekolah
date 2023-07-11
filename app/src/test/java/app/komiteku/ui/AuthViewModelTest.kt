package app.komiteku.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.komiteku.data.model.LoginResult
import app.komiteku.data.model.RequestState
import app.komiteku.data.repo.AuthRepo
import app.komiteku.data.repo.Repository
import app.komiteku.ui.utils.Dummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repo: AuthRepo
    private lateinit var viewModel: AuthViewModel
    private val dummyLoginReq = Dummy.dummyLoginReq()
    private val dummyLoginResponse = Dummy.dummyLoginRes()

    @Before
    fun before() {
        viewModel = AuthViewModel(repo)
    }

    @Test
    fun `When login success`() = runTest {
//        val expectedLogin = Flow<RequestState<LoginResult>>()
//        expectedLogin = Result.success(dummyLoginResponse)
//
//        `when`(repo.doLogin(dummyLoginReq)).thenReturn(expectedLogin)
//        val actualData = viewModel.doLogin(dummyLoginReq)
//        verify(repo).doLogin(dummyLoginReq)
//        assertNotNull(actualData)
    }

    @Test
    fun saveSession() {
    }
}