package com.example.newbankingproject.ui.login.fragment

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class RegistrationFragmentTest {

    @Test
    fun `empty username phone password  returns false`() {
        val result = RegistrationFragment().validation("", "", "")
        assertThat(result).isFalse()
    }

    @Test
    fun `empty username  password  returns false`() {
        val result = RegistrationFragment().validation("", "1234567899", "")
        assertThat(result).isFalse()
    }

    @Test
    fun `empty username phone   returns false`() {
        val result = RegistrationFragment().validation("", "", "123456")
        assertThat(result).isFalse()
    }

    @Test
    fun `empty  phone password  returns false`() {
        val result = RegistrationFragment().validation("test", "", "")
        assertThat(result).isFalse()
    }

    @Test
    fun `none empty  phone password  returns false`() {
        val result = RegistrationFragment().validation("test", "1234567899", "123456")
        assertThat(result).isTrue()
    }

}