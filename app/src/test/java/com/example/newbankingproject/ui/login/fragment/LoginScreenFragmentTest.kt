package com.example.newbankingproject.ui.login.fragment

import com.google.common.truth.Truth
import org.junit.Test

internal class LoginScreenFragmentTest{

    @Test
    fun `empty phone returns false`() {
        val result = LoginScreenFragment().validation("", "1234567899")
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `empty phone password returns false`() {
        val result = LoginScreenFragment().validation("", "")
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `empty password  returns false`() {
        val result = LoginScreenFragment().validation("test", "")
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `none empty  phone password  returns false`() {
        val result = LoginScreenFragment().validation("test", "1234567899")
        Truth.assertThat(result).isTrue()
    }
}