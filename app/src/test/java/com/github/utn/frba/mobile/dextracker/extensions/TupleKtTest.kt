package com.github.utn.frba.mobile.dextracker.extensions

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class TupleKtTest : WordSpec() {
    data class Test(
        val foo: String?,
        val bar: Int?,
    )

    init {
        "both" should {
            "be null if both a and b are null" {
                Test(foo = null, bar = null).both({ it.foo }, { it.bar }) shouldBe null
            }

            "be null if a is not null and b is null" {
                checkAll<String> { foo ->
                    Test(foo = foo, bar = null).both({ it.foo }, { it.bar }) shouldBe null
                }
            }

            "be null if a is null and b is not null" {
                checkAll<Int> { bar ->
                    Test(foo = null, bar = bar).both({ it.foo }, { it.bar }) shouldBe null
                }
            }

            "be not null if both a and b are not null" {
                checkAll<String, Int> { foo, bar ->
                    Test(foo = foo, bar = bar).both({ it.foo }, { it.bar }) shouldBe (foo to bar)
                }
            }
        }
    }
}
