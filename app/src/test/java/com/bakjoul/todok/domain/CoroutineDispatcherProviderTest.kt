package com.bakjoul.todok.domain

import kotlinx.coroutines.Dispatchers
import org.junit.Assert.*
import org.junit.Test

class CoroutineDispatcherProviderTest {
    @Test
    fun `verify coroutine dispatchers`() {
        // When
        val result = CoroutineDispatcherProvider()

        // Then
        assertEquals(result.main, Dispatchers.Main)
        assertEquals(result.io, Dispatchers.IO)
    }
}
